package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.multiversion.versionedfields.Blocks;
import adris.rinforzando.multiversion.versionedfields.Items;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasks.construction.DestroyBlockTask;
import adris.rinforzando.tasks.movement.DefaultGoToDimensionTask;
import adris.rinforzando.tasks.movement.SearchChunkForBlockTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.Dimension;
import adris.rinforzando.util.helpers.WorldHelper;
import net.minecraft.util.math.BlockPos;

public class GetSmithingTemplateTask extends ResourceTask {

    private final Task _searcher = new SearchChunkForBlockTask(Blocks.BLACKSTONE);
    private final int _count;
    private BlockPos _chestloc = null;

    public GetSmithingTemplateTask(int count) {
        super(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, count);
        _count = count;
    }

    @Override
    protected void onResourceStart(Rinforzando mod) {

    }

    @Override
    protected Task onResourceTick(Rinforzando mod) {
        // We must go to the nether.
        if (WorldHelper.getCurrentDimension() != Dimension.NETHER) {
            setDebugState("Going to nether");
            return new DefaultGoToDimensionTask(Dimension.NETHER);
        }
        //if (_bastionloc != null && !mod.getChunkTracker().isChunkLoaded(_bastionloc)) {
        //    Debug.logMessage("Bastion at " + _bastionloc + " too far away. Re-searching.");
        //    _bastionloc = null;
        // }
        if (_chestloc == null) {
            for (BlockPos pos : mod.getBlockScanner().getKnownLocations(Blocks.CHEST)) {
                if (WorldHelper.isInteractableBlock(pos)) {
                    _chestloc = pos;
                    break;
                }
            }
        }
        if (_chestloc != null) {
            //if (!_chestloc.isWithinDistance(mod.getPlayer().getPos(), 150)) {
            setDebugState("Destroying Chest"); // TODO: Make It check the chest instead of destroying it
            if (WorldHelper.isInteractableBlock(_chestloc)) {
                return new DestroyBlockTask(_chestloc);
            } else {
                _chestloc = null;
                for (BlockPos pos : mod.getBlockScanner().getKnownLocations(Blocks.CHEST)) {
                    if (WorldHelper.isInteractableBlock(pos)) {
                        _chestloc = pos;
                        break;
                    }
                }
            }
            //}
        }
        setDebugState("Searching for/Traveling around bastion");
        return _searcher;
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {
    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        return other instanceof GetSmithingTemplateTask;
    }

    @Override
    protected String toDebugStringName() {
        return "Collect " + _count + " smithing templates";
    }

    @Override
    protected boolean shouldAvoidPickingUp(Rinforzando mod) {
        return false;
    }
}
