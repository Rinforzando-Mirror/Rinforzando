package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.Debug;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;

import java.util.List;

public class UnPauseCommand extends Command {
    public UnPauseCommand() {
        super(List.of("unpause", "resume"), "Unpauses the bot");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) {
        if (!mod.isPaused()) {
            mod.log("Bot isn't paused");
        } else {
            if (mod.getStoredTask() == null) {
                Debug.logError("Stored task is null!");
            } else {
                mod.runUserTask(mod.getStoredTask());
                mod.getTaskRunner().enable();
                mod.log("Unpausing Bot and time");
            }
            mod.setPaused(false);
        }
        finish();
    }
}