package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasks.movement.DefaultGoToDimensionTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.Dimension;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.MiningRequirement;
import adris.rinforzando.util.helpers.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

public class CollectQuartzTask extends ResourceTask {

    private final int _count;

    public CollectQuartzTask(int count) {
        super(Items.QUARTZ, count);
        _count = count;
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
        if (WorldHelper.getCurrentDimension() != Dimension.NETHER) {
            setDebugState("Going to nether");
            return new DefaultGoToDimensionTask(Dimension.NETHER);
        }

        setDebugState("Mining");
        return new MineAndCollectTask(new ItemTarget(Items.QUARTZ, _count), new Block[]{Blocks.NETHER_QUARTZ_ORE}, MiningRequirement.WOOD);
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {

    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        return other instanceof CollectQuartzTask;
    }

    @Override
    protected String toDebugStringName() {
        return "Collecting " + _count + " quartz";
    }
}
