package adris.rinforzando.tasks.misc;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.construction.DestroyBlockTask;
import adris.rinforzando.tasks.container.LootContainerTask;
import adris.rinforzando.tasksystem.Task;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.List;

public class LootDesertTempleTask extends Task {
    public final Vec3i[] CHEST_POSITIONS_RELATIVE = {
            new Vec3i(2, 0, 0),
            new Vec3i(-2, 0, 0),
            new Vec3i(0, 0, 2),
            new Vec3i(0, 0, -2)
    };
    private final BlockPos temple;
    private final List<Item> wanted;
    private Task lootTask;
    private short looted = 0;

    public LootDesertTempleTask(BlockPos temple, List<Item> wanted) {
        this.temple = temple;
        this.wanted = wanted;
    }

    @Override
    protected void onStart() {
        Rinforzando.getInstance().getClientBaritoneSettings().blocksToAvoid.value.add(Blocks.STONE_PRESSURE_PLATE);
    }

    @Override
    protected Task onTick() {
        if (lootTask != null) {
            if (!lootTask.isFinished()) {
                setDebugState("Looting a desert temple chest");
                return lootTask;
            }
            looted++;
        }
        if (Rinforzando.getInstance().getWorld().getBlockState(temple).getBlock() == Blocks.STONE_PRESSURE_PLATE) {
            setDebugState("Breaking pressure plate");
            return new DestroyBlockTask(temple);
        }
        if (looted < 4) {
            setDebugState("Looting a desert temple chest");
            lootTask = new LootContainerTask(temple.add(CHEST_POSITIONS_RELATIVE[looted]), wanted);
            return lootTask;
        }
        setDebugState("Why is this still running? Report this");
        return null;
    }

    @Override
    protected void onStop(Task task) {
        Rinforzando.getInstance().getClientBaritoneSettings().blocksToAvoid.value.remove(Blocks.STONE_PRESSURE_PLATE);
    }

    @Override
    protected boolean isEqual(Task other) {
        return other instanceof LootDesertTempleTask && ((LootDesertTempleTask) other).getTemplePos() == temple;
    }

    @Override
    public boolean isFinished() {
        return looted == 4;
    }

    @Override
    protected String toDebugString() {
        return "Looting Desert Temple";
    }

    public BlockPos getTemplePos() {
        return temple;
    }
}