package adris.rinforzando.tasks.container;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.InteractWithBlockTask;
import adris.rinforzando.tasks.construction.DestroyBlockTask;
import adris.rinforzando.tasks.movement.TimeoutWanderTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.trackers.storage.ContainerCache;
import adris.rinforzando.trackers.storage.ContainerType;
import adris.rinforzando.util.helpers.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

/**
 * Opens a STORAGE container and does whatever you want inside of it
 */
public abstract class AbstractDoToStorageContainerTask extends Task {

    private ContainerType currentContainerType = null;

    @Override
    protected void onStart() {

    }

    @Override
    protected Task onTick() {
        Optional<BlockPos> containerTarget = getContainerTarget();

        Rinforzando mod = Rinforzando.getInstance();
        // No container found
        if (containerTarget.isEmpty()) {
            setDebugState("Wandering");
            currentContainerType = null;
            return onSearchWander();
        }

        BlockPos targetPos = containerTarget.get();

        // We're open
        if (currentContainerType != null && ContainerType.screenHandlerMatches(currentContainerType)) {

            Optional<ContainerCache> cache = mod.getItemStorage().getContainerAtPosition(targetPos);
            if (cache.isPresent()) {
                return onContainerOpenSubtask(mod, cache.get());
            }
        }

        // Get to the container
        if (mod.getChunkTracker().isChunkLoaded(targetPos)) {
            Block type = mod.getWorld().getBlockState(targetPos).getBlock();
            currentContainerType = ContainerType.getFromBlock(type);
        }
        if (WorldHelper.isChest(targetPos) && WorldHelper.isSolidBlock(targetPos.up()) && WorldHelper.canBreak(targetPos.up())) {
            setDebugState("Clearing block above chest");
            return new DestroyBlockTask(targetPos.up());
        }
        setDebugState("Opening container: " + targetPos.toShortString());
        return new InteractWithBlockTask(targetPos);
    }

    @Override
    protected void onStop(Task interruptTask) {

    }

    protected abstract Optional<BlockPos> getContainerTarget();

    protected abstract Task onContainerOpenSubtask(Rinforzando mod, ContainerCache containerCache);

    // Virtual
    // TODO: Interface this
    protected Task onSearchWander() {
        return new TimeoutWanderTask();
    }
}
