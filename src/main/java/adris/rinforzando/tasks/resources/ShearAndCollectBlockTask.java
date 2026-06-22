package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.BotBehaviour;
import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.MiningRequirement;
import adris.rinforzando.util.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class ShearAndCollectBlockTask extends MineAndCollectTask {

    public ShearAndCollectBlockTask(ItemTarget[] itemTargets, Block... blocksToMine) {
        super(itemTargets, blocksToMine, MiningRequirement.HAND);
    }

    public ShearAndCollectBlockTask(Item[] items, int count, Block... blocksToMine) {
        this(new ItemTarget[]{new ItemTarget(items, count)}, blocksToMine);
    }

    public ShearAndCollectBlockTask(Item item, int count, Block... blocksToMine) {
        this(new Item[]{item}, count, blocksToMine);
    }

    @Override
    protected void onStart() {
        BotBehaviour botBehaviour = Rinforzando.getInstance().getBehaviour();

        botBehaviour.push();
        botBehaviour.forceUseTool((blockState, itemStack) ->
                itemStack.getItem() == Items.SHEARS && ItemHelper.areShearsEffective(blockState.getBlock())
        );
        super.onStart();
    }

    @Override
    protected void onStop(Task interruptTask) {
        Rinforzando.getInstance().getBehaviour().pop();
        super.onStop(interruptTask);
    }

    @Override
    protected Task onResourceTick(Rinforzando mod) {
        if (!mod.getItemStorage().hasItem(Items.SHEARS)) {
            return TaskCatalogue.getItemTask(Items.SHEARS, 1);
        }
        return super.onResourceTick(mod);
    }
}
