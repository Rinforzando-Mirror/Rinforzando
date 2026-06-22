package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.commandsystem.args.IntArg;
import adris.rinforzando.tasks.resources.CollectFoodTask;

public class FoodCommand extends Command {
    public FoodCommand() {
        super("food", "Collects a certain amount of food",
                new IntArg( "count")
        );
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        mod.runUserTask(new CollectFoodTask(parser.get(Integer.class)), this::finish);
    }
}