package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.multiversion.versionedfields.Blocks;
import adris.rinforzando.multiversion.versionedfields.Items;
import adris.rinforzando.tasks.CraftInInventoryTask;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public class CollectAmethystBlockTask extends ResourceTask {

    private final int _count;

    public CollectAmethystBlockTask(int targetCount) {
        super(Items.AMETHYST_BLOCK, targetCount);
        _count = targetCount;
    }

    @Override
    protected boolean shouldAvoidPickingUp(Rinforzando mod) {
        return false;
    }

    @Override
    protected void onResourceStart(Rinforzando mod) {
        // Bot will not break Budding Amethyst
        mod.getBehaviour().push();
        mod.getBehaviour().avoidBlockBreaking(blockPos -> {
            BlockState s = mod.getWorld().getBlockState(blockPos);
            return s.getBlock() == Blocks.BUDDING_AMETHYST;
        });
    }

    @Override
    protected Task onResourceTick(Rinforzando mod) {
        if (mod.getItemStorage().getItemCount(Items.AMETHYST_SHARD) >= 4) {
            int target = mod.getItemStorage().getItemCount(Items.AMETHYST_BLOCK) + 1;
            ItemTarget s = new ItemTarget(Items.AMETHYST_SHARD, 1);
            return new CraftInInventoryTask(new RecipeTarget(Items.AMETHYST_BLOCK, target, CraftingRecipe.newShapedRecipe("amethyst_block", new ItemTarget[]{s, s, s, s}, 1)));
        }
        return new MineAndCollectTask(new ItemTarget(Items.AMETHYST_BLOCK, Items.AMETHYST_SHARD), new Block[]{Blocks.AMETHYST_BLOCK, Blocks.AMETHYST_CLUSTER}, MiningRequirement.WOOD).forceDimension(Dimension.OVERWORLD);
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {
        mod.getBehaviour().pop();
    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        return other instanceof CollectAmethystBlockTask;
    }

    @Override
    protected String toDebugStringName() {
        return "Collecting " + _count + " Amethyst Blocks.";
    }
}
