package adris.rinforzando.chains;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.Debug;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.tasksystem.TaskChain;
import adris.rinforzando.tasksystem.TaskRunner;

public abstract class SingleTaskChain extends TaskChain {

    protected Task mainTask = null;
    private boolean interrupted = false;

    private final Rinforzando mod;

    public SingleTaskChain(TaskRunner runner) {
        super(runner);
        mod = runner.getMod();
    }

    @Override
    protected void onTick() {
        if (!isActive()) return;

        if (interrupted) {
            interrupted = false;
            if (mainTask != null) {
                mainTask.reset();
            }
        }

        if (mainTask != null) {
            if ((mainTask.isFinished()) || mainTask.stopped()) {
                onTaskFinish(mod);
            } else {
                mainTask.tick(this);
            }
        }
    }

    protected void onStop() {
        if (isActive() && mainTask != null) {
            mainTask.stop();
            mainTask = null;
        }
    }

    public void setTask(Task task) {
        if (mainTask == null || !mainTask.equals(task)) {
            if (mainTask != null) {
                mainTask.stop(task);
            }
            mainTask = task;
            if (task != null) task.reset();
        }
    }


    @Override
    public boolean isActive() {
        return mainTask != null;
    }

    protected abstract void onTaskFinish(Rinforzando mod);

    @Override
    public void onInterrupt(TaskChain other) {
        if (other != null) {
            Debug.logInternal("Chain Interrupted: " + this + " by " + other);
        }
        // Stop our task. When we're started up again, let our task know we need to run.
        interrupted = true;
        if (mainTask != null && mainTask.isActive()) {
            mainTask.interrupt(null);
        }
    }

    protected boolean isCurrentlyRunning(Rinforzando mod) {
        return !interrupted && mainTask.isActive() && !mainTask.isFinished();
    }

    public Task getCurrentTask() {
        return mainTask;
    }
}
