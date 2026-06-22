package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;

public class PauseCommand extends Command {
    public PauseCommand() {
        super("pause", "Pauses the currently running task");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) {
        if (mod.isPaused()) {
            log("Bot is already paused!");
        } else if (!mod.getUserTaskChain().isActive()) {
            log("Bot has no current task!");
        } else {
            mod.setStoredTask(mod.getUserTaskChain().getCurrentTask());
            mod.setPaused(true);
            mod.getUserTaskChain().stop();
            mod.getTaskRunner().disable();
            log("Pausing Bot and time");
        }
        finish();
    }
}