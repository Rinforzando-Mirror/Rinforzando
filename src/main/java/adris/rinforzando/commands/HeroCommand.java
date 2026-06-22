package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.tasks.entity.HeroTask;

public class HeroCommand extends Command {
    public HeroCommand() {
        super("hero", "Kill all hostile mobs");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        mod.runUserTask(new HeroTask(), this::finish);
    }
}
