package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.tasks.speedrun.beatgame.BeatMinecraftTask;

public class GamerCommand extends Command {
    public GamerCommand() {
        super("gamer", "Beats the game (Miran version)");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) {
        mod.runUserTask(new BeatMinecraftTask(mod), this::finish);
    }
}