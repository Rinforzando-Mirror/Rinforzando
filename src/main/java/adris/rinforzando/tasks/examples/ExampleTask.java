package adris.rinforzando.tasks.examples;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.tasks.construction.PlaceBlockTask;
import adris.rinforzando.tasks.movement.GetToBlockTask;
import adris.rinforzando.tasksystem.Task;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class ExampleTask extends Task {

    private final int numberOfStonePickaxesToGrab;
    private final BlockPos whereToPlaceCobblestone;

    public ExampleTask(int numberOfStonePickaxesToGrab, BlockPos whereToPlaceCobblestone) {
        this.numberOfStonePickaxesToGrab = numberOfStonePickaxesToGrab;
        this.whereToPlaceCobblestone = whereToPlaceCobblestone;
    }

    @Override
    protected void onStart() {
        Rinforzando mod = Rinforzando.getInstance();

        mod.getBehaviour().push();
        mod.getBehaviour().addProtectedItems(Items.COBBLESTONE);
    }

    @Override
    protected Task onTick() {

        /*
         * Grab X stone pickaxes
         * Make sure we have a block
         * Then, place the block.
         */
        Rinforzando mod = Rinforzando.getInstance();

        if (mod.getItemStorage().getItemCount(Items.STONE_PICKAXE) < numberOfStonePickaxesToGrab) {
            return TaskCatalogue.getItemTask(Items.STONE_PICKAXE, numberOfStonePickaxesToGrab);
        }

        if (!mod.getItemStorage().hasItem(Items.COBBLESTONE)) {
            return TaskCatalogue.getItemTask(Items.COBBLESTONE, 1);
        }

        if (mod.getChunkTracker().isChunkLoaded(whereToPlaceCobblestone)) {
            if (mod.getWorld().getBlockState(whereToPlaceCobblestone).getBlock() != Blocks.COBBLESTONE) {
                return new PlaceBlockTask(whereToPlaceCobblestone, Blocks.COBBLESTONE); ///new PlaceStructureBlockTask(_whereToPlaceCobblestone);
            }
            return null;
        } else {
            return new GetToBlockTask(whereToPlaceCobblestone);
        }
    }

    @Override
    protected void onStop(Task interruptTask) {
        Rinforzando.getInstance().getBehaviour().pop();
    }

    @Override
    public boolean isFinished() {
        Rinforzando mod = Rinforzando.getInstance();

        return mod.getItemStorage().getItemCount(Items.STONE_PICKAXE) >= numberOfStonePickaxesToGrab &&
                mod.getWorld().getBlockState(whereToPlaceCobblestone).getBlock() == Blocks.COBBLESTONE;
    }

    @Override
    protected boolean isEqual(Task other) {
        if (other instanceof ExampleTask task) {
            return task.numberOfStonePickaxesToGrab == numberOfStonePickaxesToGrab
                    && task.whereToPlaceCobblestone.equals(whereToPlaceCobblestone);
        }
        return false;
    }

    @Override
    protected String toDebugString() {
        return "Boofin";
    }
}
