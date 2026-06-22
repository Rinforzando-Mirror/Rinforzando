package adris.rinforzando.tasks.resources;

import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.MiningRequirement;
import adris.rinforzando.util.helpers.ItemHelper;

public class CollectSaplingsTask extends MineAndCollectTask {
    public CollectSaplingsTask(int count) {
        super(new ItemTarget(ItemHelper.SAPLINGS, count), ItemHelper.SAPLING_SOURCES,
                MiningRequirement.HAND);
    }
}
