package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasks.entity.DoToClosestEntityTask;
import adris.rinforzando.tasks.movement.DefaultGoToDimensionTask;
import adris.rinforzando.tasks.movement.GetToEntityTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.Dimension;
import adris.rinforzando.util.helpers.WorldHelper;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Items;

public class CollectEggsTask extends ResourceTask {

    private final int _count;

    private final DoToClosestEntityTask _waitNearChickens;

    private Rinforzando _mod;

    public CollectEggsTask(int targetCount) {
        super(Items.EGG, targetCount);
        _count = targetCount;
        _waitNearChickens = new DoToClosestEntityTask(chicken -> new GetToEntityTask(chicken, 5), ChickenEntity.class);
    }

    @Override
    protected boolean shouldAvoidPickingUp(Rinforzando mod) {
        return false;
    }

    @Override
    protected void onResourceStart(Rinforzando mod) {
        _mod = mod;
    }

    @Override
    protected Task onResourceTick(Rinforzando mod) {
        // Wrong dimension check.
        if (_waitNearChickens.wasWandering() && WorldHelper.getCurrentDimension() != Dimension.OVERWORLD) {
            setDebugState("Going to right dimension.");
            return new DefaultGoToDimensionTask(Dimension.OVERWORLD);
        }
        // Just wait around chickens.
        setDebugState("Waiting around chickens. Yes.");
        return _waitNearChickens;
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {

    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        return other instanceof CollectEggsTask;
    }

    @Override
    protected String toDebugStringName() {
        return "Collecting " + _count + " eggs.";
    }
}
