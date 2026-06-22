package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.Debug;
import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasks.entity.AbstractDoToEntityTask;
import adris.rinforzando.tasksystem.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.Optional;

public class CollectMilkTask extends ResourceTask {

    private final int count;

    public CollectMilkTask(int targetCount) {
        super(Items.MILK_BUCKET, targetCount);
        count = targetCount;
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
        // Make sure we have a bucket.
        if (!mod.getItemStorage().hasItem(Items.BUCKET)) {
            return TaskCatalogue.getItemTask(Items.BUCKET, 1);
        }
        // Dimension
        if (!mod.getEntityTracker().entityFound(CowEntity.class) && isInWrongDimension(mod)) {
            return getToCorrectDimensionTask(mod);
        }
        return new MilkCowTask();
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {

    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        return other instanceof CollectMilkTask;
    }

    @Override
    protected String toDebugStringName() {
        return "Collecting " + count + " milk buckets.";
    }

    static class MilkCowTask extends AbstractDoToEntityTask {

        public MilkCowTask() {
            super(0, -1, -1);
        }

        @Override
        protected boolean isSubEqual(AbstractDoToEntityTask other) {
            return other instanceof MilkCowTask;
        }

        @Override
        protected Task onEntityInteract(Rinforzando mod, Entity entity) {
            if (!mod.getItemStorage().hasItem(Items.BUCKET)) {
                Debug.logWarning("Failed to milk cow because you have no bucket.");
                return null;
            }
            if (mod.getSlotHandler().forceEquipItem(Items.BUCKET)) {
                mod.getController().interactEntity(mod.getPlayer(), entity, Hand.MAIN_HAND);
            }


            return null;
        }

        @Override
        protected Optional<Entity> getEntityTarget(Rinforzando mod) {
            return mod.getEntityTracker().getClosestEntity(mod.getPlayer().getPos(), CowEntity.class);
        }

        @Override
        protected String toDebugString() {
            return "Milking Cow";
        }
    }
}
