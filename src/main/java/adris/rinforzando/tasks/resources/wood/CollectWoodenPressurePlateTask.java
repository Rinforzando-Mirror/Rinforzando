package adris.rinforzando.tasks.resources.wood;

import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.tasks.resources.CraftWithMatchingPlanksTask;
import adris.rinforzando.util.CraftingRecipe;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.helpers.ItemHelper;
import net.minecraft.item.Item;

public class CollectWoodenPressurePlateTask extends CraftWithMatchingPlanksTask {

    public CollectWoodenPressurePlateTask(Item[] targets, ItemTarget planks, int count) {
        super(targets, woodItems -> woodItems.pressurePlate, createRecipe(planks), new boolean[]{true, true, false, false}, count);
    }

    public CollectWoodenPressurePlateTask(Item target, String plankCatalogueName, int count) {
        this(new Item[]{target}, new ItemTarget(plankCatalogueName, 1), count);
    }

    public CollectWoodenPressurePlateTask(int count) {
        this(ItemHelper.WOOD_PRESSURE_PLATE, TaskCatalogue.getItemTarget("planks", 1), count);
    }


    private static CraftingRecipe createRecipe(ItemTarget planks) {
        ItemTarget p = planks;
        return CraftingRecipe.newShapedRecipe(new ItemTarget[]{p, p, null, null}, 1);
    }
}
