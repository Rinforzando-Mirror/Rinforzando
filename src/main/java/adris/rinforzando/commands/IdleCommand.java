package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.tasks.movement.IdleTask;

public class IdleCommand extends Command {
    public IdleCommand() {
        super("idle", "Stand still");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) {
        mod.runUserTask(new IdleTask(), this::finish);
    }
}
