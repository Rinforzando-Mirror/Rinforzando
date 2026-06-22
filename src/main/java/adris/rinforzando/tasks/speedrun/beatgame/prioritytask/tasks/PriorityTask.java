package adris.rinforzando.tasks.speedrun.beatgame.prioritytask.tasks;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasksystem.Task;

import java.util.function.Function;

public abstract class PriorityTask {

    private final Function<Rinforzando, Boolean> canCall;
    private final boolean shouldForce;
    private final boolean canCache;

    //this can be used if we are sure we would want to end this task earlier than 3 seconds after being invoked
    public final boolean bypassForceCooldown;

    public PriorityTask(Function<Rinforzando, Boolean> canCall, boolean shouldForce, boolean canCache, boolean bypassForceCooldown) {
        this.canCall = canCall;
        this.shouldForce = shouldForce;
        this.canCache = canCache;
        this.bypassForceCooldown = bypassForceCooldown;
    }

    public final double calculatePriority(Rinforzando mod) {
        if (!canCall.apply(mod)) return Double.NEGATIVE_INFINITY;

        return getPriority(mod);
    }

    @Override
    public String toString() {
        return getDebugString();
    }

    public abstract Task getTask(Rinforzando mod);

    public abstract String getDebugString();

    // maybe pass distance as well?
    protected abstract double getPriority(Rinforzando mod);

    public boolean needCraftingOnStart(Rinforzando mod) {
        return false;
    }

    public boolean shouldForce() {
        return shouldForce;
    }

    public boolean canCache() {
        return canCache;
    }
}
