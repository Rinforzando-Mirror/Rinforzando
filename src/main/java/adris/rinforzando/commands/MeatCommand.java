package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.commandsystem.args.IntArg;
import adris.rinforzando.tasks.resources.CollectMeatTask;

public class MeatCommand extends Command {
    public MeatCommand() throws CommandException {
        super("meat", "Collects a certain amount of meat",
                new IntArg( "count")
        );
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        mod.runUserTask(new CollectMeatTask(parser.get(Integer.class)), this::finish);
    }
}