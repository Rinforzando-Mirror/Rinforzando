package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;

public class MarvionCommand extends Command {

    public MarvionCommand() {
        super("marvion", "Unsupported leaving it here in case anyone uses it");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) {
        mod.logWarning("This command does not exist, if you want to beat the game use '@gamer'");
    }

}
