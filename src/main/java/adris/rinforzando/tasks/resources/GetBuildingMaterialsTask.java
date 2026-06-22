package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.MiningRequirement;
import adris.rinforzando.util.helpers.StorageHelper;
import net.minecraft.item.Item;

public class GetBuildingMaterialsTask extends Task {
    private final int _count;

    public GetBuildingMaterialsTask(int count) {
        _count = count;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected Task onTick() {
        Item[] throwaways = Rinforzando.getInstance().getModSettings().getThrowawayItems(true);
        return new MineAndCollectTask(new ItemTarget[]{new ItemTarget(throwaways, _count)}, MiningRequirement.WOOD);
    }

    @Override
    protected void onStop(Task interruptTask) {

    }

    @Override
    protected boolean isEqual(Task other) {
        if (other instanceof GetBuildingMaterialsTask task) {
            return task._count == _count;
        }
        return false;
    }

    @Override
    public boolean isFinished() {
        return StorageHelper.getBuildingMaterialCount() >= _count;
    }

    @Override
    protected String toDebugString() {
        return "Collecting " + _count + " building materials.";
    }
}
