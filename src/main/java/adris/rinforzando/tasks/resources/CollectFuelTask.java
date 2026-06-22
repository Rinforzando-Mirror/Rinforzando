package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.tasks.movement.DefaultGoToDimensionTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.Dimension;
import adris.rinforzando.util.helpers.WorldHelper;
import net.minecraft.item.Items;

// TODO: Make this collect more than just coal. It should smartly pick alternative sources if coal is too far away or if we simply cannot get a wooden pick.
public class CollectFuelTask extends Task {

    private final double targetFuel;

    public CollectFuelTask(double targetFuel) {
        this.targetFuel = targetFuel;
    }

    @Override
    protected void onStart() {
        // Nothing
    }

    @Override
    protected Task onTick() {

        switch (WorldHelper.getCurrentDimension()) {
            case OVERWORLD -> {
                // Just collect coal for now.
                setDebugState("Collecting coal.");
                return TaskCatalogue.getItemTask(Items.COAL, (int) Math.ceil(targetFuel / 8));
            }
            case END -> {
                setDebugState("Going to overworld, since, well, no more fuel can be found here.");
                return new DefaultGoToDimensionTask(Dimension.OVERWORLD);
            }
            case NETHER -> {
                setDebugState("Going to overworld, since we COULD use wood but wood confuses the bot. A bug at the moment.");
                return new DefaultGoToDimensionTask(Dimension.OVERWORLD);
            }
        }
        setDebugState("INVALID DIMENSION: " + WorldHelper.getCurrentDimension());
        return null;
    }

    @Override
    protected void onStop(Task interruptTask) {
        // Nothing
    }

    @Override
    protected boolean isEqual(Task other) {
        if (other instanceof CollectFuelTask task) {
            return Math.abs(task.targetFuel - targetFuel) < 0.01;
        }
        return false;
    }

    @Override
    public boolean isFinished() {
        return Rinforzando.getInstance().getItemStorage().getItemCountInventoryOnly(Items.COAL) >= targetFuel;
    }

    @Override
    protected String toDebugString() {
        return "Collect Fuel: x" + targetFuel;
    }
}
