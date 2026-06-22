package adris.rinforzando.tasks.construction;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.InteractWithBlockTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.ItemTarget;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;

/**
 * Removes a liquid source block at a position.
 */
public class ClearLiquidTask extends Task {

    private final BlockPos _liquidPos;

    public ClearLiquidTask(BlockPos liquidPos) {
        this._liquidPos = liquidPos;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected Task onTick() {
        if (Rinforzando.getInstance().getItemStorage().hasItem(Items.BUCKET)) {
            Rinforzando.getInstance().getBehaviour().setRayTracingFluidHandling(RaycastContext.FluidHandling.SOURCE_ONLY);
            return new InteractWithBlockTask(new ItemTarget(Items.BUCKET, 1), _liquidPos, false);
        }

        return new PlaceStructureBlockTask(_liquidPos);
    }

    @Override
    protected void onStop(Task interruptTask) {

    }

    @Override
    public boolean isFinished() {
        if (Rinforzando.getInstance().getChunkTracker().isChunkLoaded(_liquidPos)) {
            return Rinforzando.getInstance().getWorld().getBlockState(_liquidPos).getFluidState().isEmpty();
        }
        return false;
    }

    @Override
    protected boolean isEqual(Task other) {
        if (other instanceof ClearLiquidTask task) {
            return task._liquidPos.equals(_liquidPos);
        }
        return false;
    }

    @Override
    protected String toDebugString() {
        return "Clear liquid at " + _liquidPos;
    }
}
