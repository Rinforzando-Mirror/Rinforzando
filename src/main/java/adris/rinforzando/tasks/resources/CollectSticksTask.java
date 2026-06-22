package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.CraftInInventoryTask;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.trackers.storage.ItemStorageTracker;
import adris.rinforzando.util.CraftingRecipe;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.MiningRequirement;
import adris.rinforzando.util.RecipeTarget;
import adris.rinforzando.util.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class CollectSticksTask extends ResourceTask {

    private final int _targetCount;

    public CollectSticksTask(int targetCount) {
        super(Items.STICK, targetCount);
        _targetCount = targetCount;
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
    protected double getPickupRange(Rinforzando mod) {
        ItemStorageTracker storage = mod.getItemStorage();
        if (storage.getItemCount(ItemHelper.PLANKS)*4+storage.getItemCount(ItemHelper.LOG)*4*4 > _targetCount) return 10;

        return 35;
    }

    @Override
    protected Task onResourceTick(Rinforzando mod) {
        // try to craft sticks from bamboo
        if (mod.getItemStorage().getItemCount(Items.BAMBOO) >= 2) {
            return new CraftInInventoryTask(new RecipeTarget(Items.STICK, Math.min(mod.getItemStorage().getItemCount(Items.BAMBOO)/2,_targetCount), CraftingRecipe.newShapedRecipe("sticks", new ItemTarget[]{new ItemTarget("bamboo"), null, new ItemTarget("bamboo"), null}, 1)));
        }

        Optional<BlockPos> nearestBush = mod.getBlockScanner().getNearestBlock(Blocks.DEAD_BUSH);
        // If there's a dead bush within range, go get it
        if (nearestBush.isPresent() && nearestBush.get().isWithinDistance(mod.getPlayer().getPos(), 20)) {
            ResourceTask task = new MineAndCollectTask(Items.DEAD_BUSH, 1, new Block[]{Blocks.DEAD_BUSH}, MiningRequirement.HAND);
            task.setAllowContainers(false);

            return task;
        }
        // else craft from wood
        return new CraftInInventoryTask(new RecipeTarget(Items.STICK, _targetCount, CraftingRecipe.newShapedRecipe("sticks", new ItemTarget[]{new ItemTarget("planks"), null, new ItemTarget("planks"), null}, 4)));
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {
        mod.getBehaviour().pop();
    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        return other instanceof CollectSticksTask;
    }

    @Override
    protected String toDebugStringName() {
        return "Crafting " + _targetCount + " sticks";
    }
}
