package adris.rinforzando.tasks.resources;

import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.MiningRequirement;
import adris.rinforzando.util.helpers.ItemHelper;

public class CollectFlowerTask extends MineAndCollectTask {
    public CollectFlowerTask(int count) {
        super(new ItemTarget(ItemHelper.FLOWER, count), ItemHelper.itemsToBlocks(ItemHelper.FLOWER), MiningRequirement.HAND);
    }
}
