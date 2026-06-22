package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasks.container.CraftInTableTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.CraftingRecipe;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.MiningRequirement;
import adris.rinforzando.util.RecipeTarget;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

// TODO: This can technically be removed, as it's a mine task followed by a collect task.
public class CollectHayBlockTask extends ResourceTask {

    private final int count;

    public CollectHayBlockTask(int count) {
        super(Items.HAY_BLOCK, count);
        this.count = count;
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

        if (mod.getBlockScanner().anyFound(Blocks.HAY_BLOCK)) {
            return new MineAndCollectTask(Items.HAY_BLOCK, count, new Block[]{Blocks.HAY_BLOCK}, MiningRequirement.HAND);
        }

        ItemTarget w = new ItemTarget(Items.WHEAT, 1);
        return new CraftInTableTask(new RecipeTarget(Items.HAY_BLOCK, count, CraftingRecipe.newShapedRecipe("hay_block", new ItemTarget[]{w, w, w, w, w, w, w, w, w}, 1)));
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {

    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        return other instanceof CollectHayBlockTask;
    }

    @Override
    protected String toDebugStringName() {
        return "Collecting " + count + " hay blocks.";
    }
}
