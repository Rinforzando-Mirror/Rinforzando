package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.CraftInInventoryTask;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CollectRedSandstoneTask extends ResourceTask {

    private final int _count;

    public CollectRedSandstoneTask(int targetCount) {
        super(Items.RED_SANDSTONE, targetCount);
        _count = targetCount;
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
        if (mod.getItemStorage().getItemCount(Items.RED_SAND) >= 4) {
            int target = mod.getItemStorage().getItemCount(Items.RED_SANDSTONE) + 1;
            ItemTarget s = new ItemTarget(Items.RED_SAND, 1);
            return new CraftInInventoryTask(new RecipeTarget(Items.RED_SANDSTONE, target, CraftingRecipe.newShapedRecipe("red_sandstone", new ItemTarget[]{s, s, s, s}, 1)));
        }
        return new MineAndCollectTask(new ItemTarget(Items.RED_SANDSTONE, Items.RED_SAND), new Block[]{Blocks.RED_SANDSTONE, Blocks.RED_SAND}, MiningRequirement.WOOD).forceDimension(Dimension.OVERWORLD);
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {

    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        return other instanceof CollectRedSandstoneTask;
    }

    @Override
    protected String toDebugStringName() {
        return "Collecting " + _count + " red sandstone.";
    }
}
