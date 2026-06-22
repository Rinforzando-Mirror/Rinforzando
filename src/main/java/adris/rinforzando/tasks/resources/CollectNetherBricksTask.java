package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.CraftInInventoryTask;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.CraftingRecipe;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.MiningRequirement;
import adris.rinforzando.util.RecipeTarget;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

public class CollectNetherBricksTask extends ResourceTask {

    private final int _count;

    public CollectNetherBricksTask(int count) {
        super(Items.NETHER_BRICKS, count);
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

        /*
         * If we find nether bricks, mine them.
         *
         * Otherwise craft them from the "nether_brick" item.
         */

        if (mod.getBlockScanner().anyFound(Blocks.NETHER_BRICKS)) {
            return new MineAndCollectTask(Items.NETHER_BRICKS, _count, new Block[]{Blocks.NETHER_BRICKS}, MiningRequirement.WOOD);
        }

        ItemTarget b = new ItemTarget(Items.NETHER_BRICK, 1);
        return new CraftInInventoryTask(new RecipeTarget(Items.NETHER_BRICK, _count, CraftingRecipe.newShapedRecipe("nether_brick", new ItemTarget[]{b, b, b, b}, 1)));
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {

    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        return other instanceof CollectNetherBricksTask;
    }

    @Override
    protected String toDebugStringName() {
        return "Collecting " + _count + " nether bricks.";
    }
}
