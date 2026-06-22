package adris.rinforzando.commands.random;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.tasks.speedrun.OneCycleTask;

public class CycleTestCommand extends Command {

    public CycleTestCommand() {
        super("cycle", "One cycles the dragon B)");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) {
        mod.runUserTask(new OneCycleTask(), this::finish);
    }
}
