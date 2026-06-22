package adris.rinforzando.tasks.speedrun.beatgame.prioritytask.tasks;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.util.RecipeTarget;

import java.util.function.Function;

public class RecraftableItemPriorityTask extends CraftItemPriorityTask{


    private final double recraftPriority;

    public RecraftableItemPriorityTask(double priority, double recraftPriority, RecipeTarget toCraft, Function<Rinforzando, Boolean> canCall ) {
        super(priority, toCraft, canCall);
        this.recraftPriority = recraftPriority;
    }


    @Override
    protected double getPriority(Rinforzando mod) {
        if (isSatisfied()) return recraftPriority;

        return super.getPriority(mod);
    }
}
