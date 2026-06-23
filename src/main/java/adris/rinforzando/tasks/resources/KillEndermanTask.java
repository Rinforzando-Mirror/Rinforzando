package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasks.entity.KillEntitiesTask;
import adris.rinforzando.tasks.entity.KillEntityTask;
import adris.rinforzando.tasks.movement.GetWithinRangeOfBlockTask;
import adris.rinforzando.tasks.movement.TimeoutWanderTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.Dimension;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.helpers.WorldHelper;
import adris.rinforzando.util.time.TimerGame;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.function.Predicate;

public class KillEndermanTask extends ResourceTask {

    private final int _count;

    public KillEndermanTask(int count) {
        super(new ItemTarget(Items.ENDER_PEARL, count));
        _count = count;
        forceDimension(Dimension.NETHER);
    }

    @Override
    protected boolean shouldAvoidPickingUp(Rinforzando mod) {
        return false;
    }

    @Override
    protected void onResourceStart(Rinforzando mod) {

    }

    @Override
    protected Task onResourceTick(Rinforzando mod) {
        // Dimension
        if (!mod.getEntityTracker().entityFound(EndermanEntity.class)) {
            if (WorldHelper.getCurrentDimension() != Dimension.NETHER) {
                return getToCorrectDimensionTask(mod);
            }
            //nearest warped forest related block
            Optional<BlockPos> nearest = mod.getBlockScanner().getNearestBlock(Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT, Blocks.WARPED_HYPHAE, Blocks.WARPED_NYLIUM);
            if (nearest.isPresent()) {
                if (WorldHelper.inRangeXZ(nearest.get(), mod.getPlayer().getBlockPos(), 40)) {
                    setDebugState("Waiting for endermen to spawn...");
                    return null;
                }

                setDebugState("Getting to warped forest biome");
                return new GetWithinRangeOfBlockTask(nearest.get(), 35);
            }

            setDebugState("Warped forest biome not found");
            return new TimeoutWanderTask();
        }


        Predicate<Entity> belowNetherRoof = (entity) -> WorldHelper.getCurrentDimension() != Dimension.NETHER || entity.getY() < 125;
        final int TOO_FAR_AWAY = WorldHelper.getCurrentDimension() == Dimension.NETHER ? 10 : 256;


        // Kill the angry one
        for (EndermanEntity entity : mod.getEntityTracker().getTrackedEntities(EndermanEntity.class)) {

            if (belowNetherRoof.test(entity) && entity.isAngry() && entity.getPos().isInRange(mod.getPlayer().getPos(), TOO_FAR_AWAY)) {
                return new KillEntityTask(entity);
            }
        }

        // Attack the closest one
        return new KillEntitiesTask(belowNetherRoof, EndermanEntity.class);
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {

    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        if (other instanceof KillEndermanTask task) {
            return task._count == _count;
        }
        return false;
    }

    @Override
    protected String toDebugStringName() {
        return "Hunting endermen for pearls - " + Rinforzando.getInstance().getItemStorage().getItemCount(Items.ENDER_PEARL) + "/" + _count;
    }
}