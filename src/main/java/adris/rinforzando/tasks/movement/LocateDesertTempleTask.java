package adris.rinforzando.tasks.movement;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.helpers.WorldHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeKeys;

public class LocateDesertTempleTask extends Task {

    private BlockPos _finalPos;

    @Override
    protected void onStart() {
    }

    @Override
    protected Task onTick() {
        BlockPos desertTemplePos = WorldHelper.getADesertTemple();
        if (desertTemplePos != null) {
            _finalPos = desertTemplePos.up(14);
        }
        if (_finalPos != null) {
            setDebugState("Going to found desert temple");
            return new GetToBlockTask(_finalPos, false);
        }
        return new SearchWithinBiomeTask(BiomeKeys.DESERT);
    }

    @Override
    protected void onStop(Task interruptTask) {
    }

    @Override
    protected boolean isEqual(Task other) {
        return other instanceof LocateDesertTempleTask;
    }

    @Override
    protected String toDebugString() {
        return "Searchin' for temples";
    }

    @Override
    public boolean isFinished() {
        return Rinforzando.getInstance().getPlayer().getBlockPos().equals(_finalPos);
    }
}
