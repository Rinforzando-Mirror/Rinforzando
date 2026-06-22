package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.*;
import adris.rinforzando.commandsystem.args.ItemTargetArg;
import adris.rinforzando.commandsystem.args.ListArg;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.tasks.container.StoreInAnyContainerTask;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.helpers.StorageHelper;
import adris.rinforzando.util.slots.PlayerSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class DepositCommand extends Command {
    public DepositCommand() {
        super("deposit", "Deposit ALL of our items",
                new ListArg<>(new ItemTargetArg("itemStack"), "Item list", null, false)
        );
    }

    public static ItemTarget[] getAllNonEquippedOrToolItemsAsTarget(Rinforzando mod) {
        return StorageHelper.getAllInventoryItemsAsTargets(slot -> {
            // Ignore armor
            if (ArrayUtils.contains(PlayerSlot.ARMOR_SLOTS, slot))
                return false;
            ItemStack stack = StorageHelper.getItemStackInSlot(slot);
            // Ignore tools
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                return !(item instanceof ToolItem);
            }
            return false;
        });
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        List<ItemTarget> itemList = parser.get(List.class);

        ItemTarget[] items;
        if (itemList == null) {
            items = getAllNonEquippedOrToolItemsAsTarget(mod);
        } else {
            items = itemList.toArray(ItemTarget[]::new);
        }

        mod.runUserTask(new StoreInAnyContainerTask(false, items), this::finish);
    }
}
