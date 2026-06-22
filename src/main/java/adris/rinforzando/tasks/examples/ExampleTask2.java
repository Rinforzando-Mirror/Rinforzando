package adris.rinforzando.tasks.examples;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.movement.GetToBlockTask;
import adris.rinforzando.tasks.movement.TimeoutWanderTask;
import adris.rinforzando.tasksystem.Task;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class ExampleTask2 extends Task {

    private BlockPos target = null;

    @Override
    protected void onStart() {
        // Extra credit: Bot will NOT damage trees.
        Rinforzando mod = Rinforzando.getInstance();

        mod.getBehaviour().push();
        mod.getBehaviour().avoidBlockBreaking(blockPos -> {
            BlockState s = mod.getWorld().getBlockState(blockPos);
            return s.getBlock() == Blocks.OAK_LEAVES || s.getBlock() == Blocks.OAK_LOG;
        });
    }

    @Override
    protected Task onTick() {
        Rinforzando mod = Rinforzando.getInstance();

        /*
         * Find a tree
         * Go to its top (above the last leaf block)
         *
         * Locate the nearest log
         * Stand on top of its last leaf
         */

        if (target != null) {
            return new GetToBlockTask(target);
        }

        if (mod.getBlockScanner().anyFound(Blocks.OAK_LOG)) {
            Optional<BlockPos> nearest = mod.getBlockScanner().getNearestBlock(Blocks.OAK_LOG);
            if (nearest.isPresent()) {
                // Figure out leaves
                BlockPos check = new BlockPos(nearest.get());
                while (mod.getWorld().getBlockState(check).getBlock() == Blocks.OAK_LOG ||
                        mod.getWorld().getBlockState(check).getBlock() == Blocks.OAK_LEAVES) {
                    check = check.up();
                }
                target = check;
            }
            return null;
        }

        return new TimeoutWanderTask();
    }

    @Override
    protected void onStop(Task interruptTask) {
        Rinforzando.getInstance().getBehaviour().pop();
    }

    @Override
    protected boolean isEqual(Task other) {
        return other instanceof ExampleTask2;
    }

    @Override
    public boolean isFinished() {
        if (target != null) {
            return Rinforzando.getInstance().getPlayer().getBlockPos().equals(target);
        }
        return super.isFinished();
    }

    @Override
    protected String toDebugString() {
        return "Standing on a tree";
    }
}
