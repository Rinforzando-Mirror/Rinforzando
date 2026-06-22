package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.ui.MessagePriority;

import java.util.Arrays;

public class ListCommand extends Command {
    public ListCommand() {
        super("list", "List all obtainable items");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        mod.log("#### LIST OF ALL OBTAINABLE ITEMS ####", MessagePriority.OPTIONAL);
        mod.log(Arrays.toString(TaskCatalogue.resourceNames().toArray()), MessagePriority.OPTIONAL);
        mod.log("############# END LIST ###############", MessagePriority.OPTIONAL);
    }
}
