package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.ui.MessagePriority;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Lists all commands");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) {
        mod.log("########## HELP: ##########", MessagePriority.OPTIONAL);
        int padSize = 10;
        for (Command c : Rinforzando.getCommandExecutor().allCommands()) {
            StringBuilder line = new StringBuilder();
            //line.append("");
            for (String name : c.getNames()) {
                line.append(name).append(": ");
                int toAdd = padSize - name.length();
                line.append(" ".repeat(Math.max(0, toAdd)));
                line.append(c.getDescription());
                mod.log(line.toString(), MessagePriority.OPTIONAL);
            }
        }
        mod.log("###########################", MessagePriority.OPTIONAL);
        finish();
    }
}