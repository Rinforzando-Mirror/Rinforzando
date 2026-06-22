package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasks.entity.KillEntitiesTask;
import adris.rinforzando.tasks.movement.TimeoutWanderTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.ItemTarget;
import net.minecraft.entity.Entity;

import java.util.function.Predicate;

public class KillAndLootTask extends ResourceTask {

    private final Class<?> _toKill;

    private final Task _killTask;

    public KillAndLootTask(Class<?> toKill, Predicate<Entity> shouldKill, ItemTarget... itemTargets) {
        super(itemTargets.clone());
        _toKill = toKill;
        _killTask = new KillEntitiesTask(shouldKill, _toKill);
    }

    public KillAndLootTask(Class<?> toKill, ItemTarget... itemTargets) {
        super(itemTargets.clone());
        _toKill = toKill;
        _killTask = new KillEntitiesTask(_toKill);
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
        if (!mod.getEntityTracker().entityFound(_toKill)) {
            if (isInWrongDimension(mod)) {
                setDebugState("Going to correct dimension.");
                return getToCorrectDimensionTask(mod);
            }
            setDebugState("Searching for mob...");
            return new TimeoutWanderTask();
        }
        // We found the mob!
        return _killTask;
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {

    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        if (other instanceof KillAndLootTask task) {
            return task._toKill.equals(_toKill);
        }
        return false;
    }

    @Override
    protected String toDebugStringName() {
        return "Collect items from " + _toKill.toGenericString();
    }
}
