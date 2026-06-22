package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.commandsystem.*;
import adris.rinforzando.commandsystem.args.ItemTargetArg;
import adris.rinforzando.commandsystem.args.ListArg;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.ItemTarget;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GetCommand extends Command {

    public GetCommand() throws CommandException {
        super("get", "Get an item/resource",
                new ListArg<>(new ItemTargetArg("stack"), "items")
        );
    }


    private void getItems(Rinforzando mod, List<ItemTarget> items) {
        Task targetTask;
        if (items == null || items.isEmpty()) {
            mod.log("You must specify at least one item!");
            finish();
            return;
        }
        if (items.size() == 1) {
            targetTask = TaskCatalogue.getItemTask(items.getFirst());
        } else {
            targetTask = TaskCatalogue.getSquashedItemTask(items.toArray(new ItemTarget[0]));
        }
        if (targetTask != null) {
            mod.runUserTask(targetTask, this::finish);
        } else {
            finish();
        }
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        List<ItemTarget> items = parser.get(List.class);

        getItems(mod, items);
    }
}