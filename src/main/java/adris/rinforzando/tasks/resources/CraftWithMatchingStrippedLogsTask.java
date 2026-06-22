package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.Debug;
import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.CraftingRecipe;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.helpers.ItemHelper;
import net.minecraft.item.Item;

import java.util.function.Function;

public class CraftWithMatchingStrippedLogsTask extends CraftWithMatchingMaterialsTask {

    private final ItemTarget _visualTarget;
    private final Function<ItemHelper.WoodItems, Item> _getTargetItem;

    public CraftWithMatchingStrippedLogsTask(Item[] validTargets, Function<ItemHelper.WoodItems, Item> getTargetItem, CraftingRecipe recipe, boolean[] sameMask, int count) {
        super(new ItemTarget(validTargets, count), recipe, sameMask);
        _getTargetItem = getTargetItem;
        _visualTarget = new ItemTarget(validTargets, count);
    }


    @Override
    protected Task getSpecificSameResourceTask(Rinforzando mod, Item[] toGet) {
        for (Item strippedLogToGet : toGet) {
            Item log = ItemHelper.strippedToLogs(strippedLogToGet);
            // Convert logs to stripped
            if (mod.getItemStorage().getItemCount(log) >= 1) {
                return TaskCatalogue.getItemTask(strippedLogToGet, 1);//new CraftInInventoryTask(new ItemTarget(plankToGet, 1), CraftingRecipe.newShapedRecipe("planks", new ItemTarget[]{new ItemTarget(log, 1), empty, empty, empty}, 4), false, true);
            }
        }
        Debug.logError("CraftWithMatchingStrippedLogs: Should never happen!");
        return null;
    }

    @Override
    protected Item getSpecificItemCorrespondingToMajorityResource(Item majority) {
        for (ItemHelper.WoodItems woodItems : ItemHelper.getWoodItems()) {
            if (woodItems.strippedLog == majority) {
                return _getTargetItem.apply(woodItems);
            }
        }
        return null;
    }


    @Override
    protected boolean isEqualResource(ResourceTask other) {
        if (other instanceof CraftWithMatchingStrippedLogsTask task) {
            return task._visualTarget.equals(_visualTarget);
        }
        return false;
    }

    @Override
    protected String toDebugStringName() {
        return "Getting: " + _visualTarget;
    }


    @Override
    protected boolean shouldAvoidPickingUp(Rinforzando mod) {
        return false;
    }

}
