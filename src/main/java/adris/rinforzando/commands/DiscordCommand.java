package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;

public class DiscordCommand extends Command {
    public DiscordCommand() {
        super("discord", "Prints the Discord invite link");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) {
        mod.log("https://discord.gg/Qu2DGVuqW7");
    }
}
