package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.util.helpers.WorldHelper;

public class CoordsCommand extends Command {
    public CoordsCommand() {
        super("coords", "Get the bot's current coordinates");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) {
        mod.log("CURRENT COORDINATES: " + mod.getPlayer().getBlockPos().toShortString() + " (Current dimension: " + WorldHelper.getCurrentDimension() + ")");
        finish();
    }
}
