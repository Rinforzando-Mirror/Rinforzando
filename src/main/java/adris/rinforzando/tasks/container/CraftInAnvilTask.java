package adris.rinforzando.tasks.container;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.ItemTarget;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.apache.commons.lang3.NotImplementedException;

public class CraftInAnvilTask extends DoStuffInContainerTask {
    public CraftInAnvilTask() {
        super(new Block[]{Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL}, new ItemTarget("anvil"));
    }

    @Override
    protected boolean isSubTaskEqual(DoStuffInContainerTask other) {
        throw new NotImplementedException("Anvil Not Implemented, whoops");
    }

    @Override
    protected boolean isContainerOpen(Rinforzando mod) {
        throw new NotImplementedException("Anvil Not Implemented, whoops");
    }

    @Override
    protected Task containerSubTask(Rinforzando mod) {
        throw new NotImplementedException("Anvil Not Implemented, whoops");
    }

    @Override
    protected double getCostToMakeNew(Rinforzando mod) {
        throw new NotImplementedException("Anvil Not Implemented, whoops");
    }
}
