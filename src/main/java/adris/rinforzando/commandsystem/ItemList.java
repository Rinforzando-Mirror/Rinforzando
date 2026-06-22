package adris.rinforzando.commandsystem;

import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.util.ItemTarget;

import java.util.HashMap;

public class ItemList {
    public ItemTarget[] items;

    public ItemList(ItemTarget[] items) {
        this.items = items;
    }

}
