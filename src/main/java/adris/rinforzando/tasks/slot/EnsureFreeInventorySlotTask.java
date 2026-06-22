package adris.rinforzando.tasks.slot;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.helpers.LookHelper;
import adris.rinforzando.util.helpers.StorageHelper;
import adris.rinforzando.util.slots.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Optional;

public class EnsureFreeInventorySlotTask extends Task {
    @Override
    protected void onStart() {

    }

    @Override
    protected Task onTick() {
        Rinforzando mod = Rinforzando.getInstance();

        ItemStack cursorStack = StorageHelper.getItemStackInCursorSlot();
        Optional<Slot> garbage = StorageHelper.getGarbageSlot(mod);
        if (cursorStack.isEmpty()) {
            if (garbage.isPresent()) {
                mod.getSlotHandler().clickSlot(garbage.get(), 0, SlotActionType.PICKUP);
                return null;
            }
        }
        if (!cursorStack.isEmpty()) {
            LookHelper.randomOrientation();
            mod.getSlotHandler().clickSlot(Slot.UNDEFINED, 0, SlotActionType.PICKUP);
            return null;
        }
        setDebugState("All items are protected.");
        return null;
    }

    @Override
    protected void onStop(Task interruptTask) {

    }

    @Override
    protected boolean isEqual(Task obj) {
        return obj instanceof EnsureFreeInventorySlotTask;
    }

    @Override
    protected String toDebugString() {
        return "Ensuring inventory is free";
    }
}
