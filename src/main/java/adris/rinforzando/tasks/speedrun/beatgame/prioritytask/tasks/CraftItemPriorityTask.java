package adris.rinforzando.tasks.speedrun.beatgame.prioritytask.tasks;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.Debug;
import adris.rinforzando.tasks.CraftInInventoryTask;
import adris.rinforzando.tasks.container.CraftInTableTask;
import adris.rinforzando.tasks.speedrun.beatgame.BeatMinecraftTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.RecipeTarget;
import adris.rinforzando.util.helpers.CraftingHelper;

import java.util.function.Function;

/**
 crafts an item with constant priority...
 */
public class CraftItemPriorityTask extends PriorityTask{

    public final double priority;
    public final RecipeTarget recipeTarget;
    private boolean satisfied = false;

    // note: bypassForceCooldown is set to true, because we usually don't want to wait when crafting
    public CraftItemPriorityTask(double priority, RecipeTarget toCraft) {
        this(priority, toCraft, mod -> true);
    }

    public CraftItemPriorityTask(double priority, RecipeTarget toCraft, Function<Rinforzando, Boolean> canCall) {
        this(priority, toCraft, canCall, false, true, true);
    }

    public CraftItemPriorityTask(double priority, RecipeTarget toCraft, boolean shouldForce, boolean canCache, boolean bypassForceCooldown) {
        this(priority, toCraft, mod -> true, shouldForce, canCache, bypassForceCooldown);
    }

    public CraftItemPriorityTask(double priority, RecipeTarget toCraft, Function<Rinforzando, Boolean> canCall, boolean shouldForce, boolean canCache, boolean bypassForceCooldown) {
        super(canCall, shouldForce, canCache, bypassForceCooldown);
        this.priority = priority;
        this.recipeTarget = toCraft;
    }

    @Override
    public Task getTask(Rinforzando mod) {
        if (recipeTarget.getRecipe().isBig()) {
            return new CraftInTableTask(recipeTarget);
        }

        return new CraftInInventoryTask(recipeTarget);
    }

    @Override
    public String getDebugString() {
        return "Crafting "+recipeTarget;
    }

    @Override
    protected double getPriority(Rinforzando mod) {
        if (BeatMinecraftTask.hasItem(mod, recipeTarget.getOutputItem())) {
            Debug.logInternal("THIS IS SATISFIED "+recipeTarget.getOutputItem());
            satisfied = true;
        }
        Debug.logInternal("NOT SATISFIED");

        if (satisfied) return Double.NEGATIVE_INFINITY;

        return priority;
    }


    @Override
    public boolean needCraftingOnStart(Rinforzando mod) {
        return CraftingHelper.canCraftItemNow(mod, recipeTarget.getOutputItem());
    }

    public boolean isSatisfied() {
        return satisfied;
    }
}
