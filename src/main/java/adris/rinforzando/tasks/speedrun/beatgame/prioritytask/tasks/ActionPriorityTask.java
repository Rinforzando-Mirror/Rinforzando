package adris.rinforzando.tasks.speedrun.beatgame.prioritytask.tasks;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.speedrun.beatgame.prioritytask.prioritycalculators.PriorityCalculator;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.Pair;

import java.util.function.Function;

/**
 * most general way to create priority tasks (basically all other subclasses could be replaced by this).
 * Returns a task and a priority given a PriorityCalculator and TaskProvider
 * (kinda works like the old GatherResource I guess)
 */
public class ActionPriorityTask extends PriorityTask {


    private final TaskAndPriorityProvider taskAndPriorityProvider;

    // just for making the debug string
    private Task lastTask = null;

    public ActionPriorityTask(TaskProvider taskProvider, PriorityCalculator priorityCalculator) {
        this(taskProvider, priorityCalculator, a -> true, false, true, false);
    }

    public ActionPriorityTask(TaskProvider taskProvider, PriorityCalculator priorityCalculator, Function<Rinforzando, Boolean> canCall) {
        this((mod -> new Pair<>(taskProvider.getTask(mod), priorityCalculator.getPriority())), canCall);
    }

    public ActionPriorityTask(TaskAndPriorityProvider taskAndPriorityProvider) {
        this(taskAndPriorityProvider, a -> true);
    }

    public ActionPriorityTask(TaskAndPriorityProvider taskAndPriorityProvider, Function<Rinforzando, Boolean> canCall) {
        this(taskAndPriorityProvider, canCall, false, true, false);
    }

    public ActionPriorityTask(TaskProvider taskProvider, PriorityCalculator priorityCalculator, Function<Rinforzando, Boolean> canCall, boolean shouldForce, boolean canCache, boolean bypassForceCooldown) {
       this((mod -> new Pair<>(taskProvider.getTask(mod), priorityCalculator.getPriority())), canCall, shouldForce, canCache, bypassForceCooldown);
    }

    public ActionPriorityTask(TaskAndPriorityProvider taskAndPriorityProvider, Function<Rinforzando, Boolean> canCall, boolean shouldForce, boolean canCache, boolean bypassForceCooldown) {
        super(canCall, shouldForce, canCache, bypassForceCooldown);
        this.taskAndPriorityProvider = taskAndPriorityProvider;
    }


    @Override
    public Task getTask(Rinforzando mod) {
        lastTask = getTaskAndPriority(mod).getLeft();
        return lastTask;
    }

    @Override
    public String getDebugString() {
        return "Performing an action: "+lastTask;
    }

    @Override
    protected double getPriority(Rinforzando mod) {
        return getTaskAndPriority(mod).getRight();
    }

    private Pair<Task, Double> getTaskAndPriority(Rinforzando mod) {
        Pair<Task, Double> pair = taskAndPriorityProvider.getTaskAndPriority(mod);
        if (pair == null) {
            pair = new Pair<>(null, 0d);
        }

        if (pair.getRight() <= 0 || pair.getLeft() == null) {
            pair.setLeft(null);
            pair.setRight(Double.NEGATIVE_INFINITY);
        }

        return pair;
    }


    public interface TaskProvider {
        Task getTask(Rinforzando mod);
    }


    public interface TaskAndPriorityProvider {
        Pair<Task, Double> getTaskAndPriority(Rinforzando mod);
    }


}
