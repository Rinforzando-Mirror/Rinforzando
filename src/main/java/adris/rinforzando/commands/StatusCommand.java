package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.tasksystem.Task;

import java.util.List;

public class StatusCommand extends Command {
    public StatusCommand() {
        super("status", "Get status of currently executing command");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) {
        List<Task> tasks = mod.getUserTaskChain().getTasks();
        if (tasks.isEmpty()) {
            mod.log("No tasks currently running.");
        } else {
            mod.log("CURRENT TASK: " + tasks.get(0).toString());
        }
        finish();
    }
}