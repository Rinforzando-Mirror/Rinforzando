package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;

import java.util.List;

public class StopCommand extends Command {

    public StopCommand() {
        super(List.of("stop", "cancel"), "Stop task runner (stops all automation)");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) {
        mod.stopTasks();
        finish();
    }
}
