package adris.rinforzando.tasks;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.resources.CollectBucketLiquidTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.ItemTarget;
import net.minecraft.item.Items;

/**
 * use when bot has too many water buckets but you dont want to throw them out
 */
public class GetRidOfExtraWaterBucketTask extends Task {

    private boolean needsPickup = false;

    @Override
    protected void onStart() {

    }

    @Override
    protected Task onTick() {
        Rinforzando mod = Rinforzando.getInstance();

        if (mod.getItemStorage().getItemCount(Items.WATER_BUCKET) != 0 && !needsPickup) {
            return new InteractWithBlockTask(new ItemTarget(Items.WATER_BUCKET, 1),mod.getPlayer().getBlockPos().down(), false);
        }

        needsPickup = true;
        if (mod.getItemStorage().getItemCount(Items.WATER_BUCKET) < 1) {
            return new CollectBucketLiquidTask.CollectWaterBucketTask(1);
        }

        return null;
    }

    @Override
    public boolean isFinished() {
        return Rinforzando.getInstance().getItemStorage().getItemCount(Items.WATER_BUCKET) == 1 && needsPickup;
    }

    @Override
    protected void onStop(Task interruptTask) {

    }

    @Override
    protected boolean isEqual(Task other) {
        return other instanceof GetRidOfExtraWaterBucketTask;
    }

    @Override
    protected String toDebugString() {
        return null;
    }
}
