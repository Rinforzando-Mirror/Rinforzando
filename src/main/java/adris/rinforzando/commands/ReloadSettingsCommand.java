package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.util.helpers.ConfigHelper;

public class ReloadSettingsCommand extends Command {
    public ReloadSettingsCommand() {
        super("reload_settings", "Reloads bot settings and butler whitelist/blacklist.");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) {
        ConfigHelper.reloadAllConfigs();
        mod.log("Reload successful!");
        finish();
    }
}