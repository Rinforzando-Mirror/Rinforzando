package adris.rinforzando.tasks.slot;

import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.slots.Slot;

public class MoveItemToSlotFromContainerTask extends MoveItemToSlotTask {
    public MoveItemToSlotFromContainerTask(ItemTarget toMove, Slot destination) {
        super(toMove, destination, mod -> mod.getItemStorage().getSlotsWithItemContainer(toMove.getMatches()));
    }
}
