package adris.rinforzando.tasks.slot;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.control.SlotHandler;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.slots.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class ClickSlotTask extends Task {

    private final Slot slot;
    private final int mouseButton;
    private final SlotActionType type;

    private boolean clicked = false;

    public ClickSlotTask(Slot slot, int mouseButton, SlotActionType type) {
        this.slot = slot;
        this.mouseButton = mouseButton;
        this.type = type;
    }

    public ClickSlotTask(Slot slot, SlotActionType type) {
        this(slot, 0, type);
    }

    public ClickSlotTask(Slot slot, int mouseButton) {
        this(slot, mouseButton, SlotActionType.PICKUP);
    }

    public ClickSlotTask(Slot slot) {
        this(slot, SlotActionType.PICKUP);
    }

    @Override
    protected void onStart() {
        clicked = false;
    }

    @Override
    protected Task onTick() {
        SlotHandler slotHandler = Rinforzando.getInstance().getSlotHandler();

        if (slotHandler.canDoSlotAction()) {
            slotHandler.clickSlot(slot, mouseButton, type);
            slotHandler.registerSlotAction();
            clicked = true;
        }
        return null;
    }

    @Override
    protected void onStop(Task interruptTask) {

    }

    @Override
    protected boolean isEqual(Task obj) {
        if (obj instanceof ClickSlotTask task) {
            return task.mouseButton == mouseButton && task.type == type && task.slot.equals(slot);
        }
        return false;
    }

    @Override
    protected String toDebugString() {
        return "Clicking " + slot.toString();
    }

    @Override
    public boolean isFinished() {
        return clicked;
    }
}
