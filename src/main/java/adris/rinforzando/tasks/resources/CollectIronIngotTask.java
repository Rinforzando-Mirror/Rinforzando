package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasks.container.SmeltInBlastFurnaceTask;
import adris.rinforzando.tasks.container.SmeltInFurnaceTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.SmeltTarget;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class CollectIronIngotTask extends ResourceTask {

    private final int count;

    public CollectIronIngotTask(int count) {
        super(Items.IRON_INGOT, count);
        this.count = count;
    }

    @Override
    protected boolean shouldAvoidPickingUp(Rinforzando mod) {
        return false;
    }

    @Override
    protected void onResourceStart(Rinforzando mod) {
        mod.getBehaviour().push();
    }

    @Override
    protected Task onResourceTick(Rinforzando mod) {
        return new SmeltInFurnaceTask(new SmeltTarget(new ItemTarget(Items.IRON_INGOT, count), new ItemTarget(Items.RAW_IRON, count)));
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {
        mod.getBehaviour().pop();
    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        return other instanceof CollectIronIngotTask same && same.count == count;
    }

    @Override
    protected String toDebugStringName() {
        return "Collecting " + count + " iron.";
    }
}
