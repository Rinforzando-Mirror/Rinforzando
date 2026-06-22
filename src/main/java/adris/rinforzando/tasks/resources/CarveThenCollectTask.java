package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.tasks.DoToClosestBlockTask;
import adris.rinforzando.tasks.InteractWithBlockTask;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasks.construction.DestroyBlockTask;
import adris.rinforzando.tasks.construction.PlaceBlockNearbyTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.helpers.StorageHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.Arrays;

public class CarveThenCollectTask extends ResourceTask {

    private final ItemTarget _target;
    private final Block[] _targetBlocks;
    private final ItemTarget _toCarve;
    private final Block[] _toCarveBlocks;
    private final ItemTarget _carveWith;

    public CarveThenCollectTask(ItemTarget target, Block[] targetBlocks, ItemTarget toCarve, Block[] toCarveBlocks, ItemTarget carveWith) {
        super(target);
        _target = target;
        _targetBlocks = targetBlocks;
        _toCarve = toCarve;
        _toCarveBlocks = toCarveBlocks;
        _carveWith = carveWith;
    }

    public CarveThenCollectTask(Item target, int targetCount, Block targetBlock, Item toCarve, Block toCarveBlock, Item carveWith) {
        this(new ItemTarget(target, targetCount), new Block[]{targetBlock}, new ItemTarget(toCarve, targetCount), new Block[]{toCarveBlock}, new ItemTarget(carveWith, 1));
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
        // If target block spotted, break it!
        // If toCarve block spotted, carve it!
        // neededCarve = (neededTarget - currentTarget)
        // If neededCarve > currentCarveItems:
        //      collect carve items!
        // ELSE:
        //      Place carved items down

        // If our target block is placed, break it!
        if (mod.getBlockScanner().anyFound(_targetBlocks)) {
            setDebugState("Breaking carved/target block");
            return new DoToClosestBlockTask(DestroyBlockTask::new, _targetBlocks);
        }
        // Collect our "carve with" item (can be shears, axe, whatever)
        if (!StorageHelper.itemTargetsMetInventory(_carveWith)) {
            setDebugState("Collect our carve tool");
            return TaskCatalogue.getItemTask(_carveWith);
        }
        // If our carve block is spotted, carve it.
        if (mod.getBlockScanner().anyFound(_toCarveBlocks)) {
            setDebugState("Carving block");
            return new DoToClosestBlockTask(blockPos -> new InteractWithBlockTask(_carveWith, blockPos, false), _toCarveBlocks);
        }
        // Collect carve blocks if we don't have enough, or place them down if we do.
        int neededCarveItems = _target.getTargetCount() - mod.getItemStorage().getItemCount(_target);
        int currentCarveItems = mod.getItemStorage().getItemCount(_toCarve);
        if (neededCarveItems > currentCarveItems) {
            setDebugState("Collecting more blocks to carve");
            return TaskCatalogue.getItemTask(_toCarve);
        } else {
            setDebugState("Placing blocks to carve down");
            return new PlaceBlockNearbyTask(_toCarveBlocks);
        }
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {

    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        if (other instanceof CarveThenCollectTask task) {
            return (task._target.equals(_target) && task._toCarve.equals(_toCarve) && Arrays.equals(task._targetBlocks, _targetBlocks) && Arrays.equals(task._toCarveBlocks, _toCarveBlocks));
        }
        return false;
    }

    @Override
    protected String toDebugStringName() {
        return "Getting after carving: " + _target;
    }
}
