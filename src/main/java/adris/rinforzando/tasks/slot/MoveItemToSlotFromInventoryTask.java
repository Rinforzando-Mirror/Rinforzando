package adris.rinforzando.tasks.slot;

import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.slots.Slot;

public class MoveItemToSlotFromInventoryTask extends MoveItemToSlotTask {
    public MoveItemToSlotFromInventoryTask(ItemTarget toMove, Slot destination) {
        super(toMove, destination, mod -> mod.getItemStorage().getSlotsWithItemPlayerInventory(false, toMove.getMatches()));
    }
}
