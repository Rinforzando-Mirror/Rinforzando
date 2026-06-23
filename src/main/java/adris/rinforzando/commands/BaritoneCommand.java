package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.commandsystem.StringReader;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.commandsystem.exception.CommandNotFinishedException;
import adris.rinforzando.commandsystem.args.StringArg;

import java.util.stream.Stream;

public class BaritoneCommand extends Command {

    public BaritoneCommand() throws CommandException {
        super("baritone", "Execute baritone commands directly",
                new RestArg("baritone-command")
        );
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        String baritoneCmd = parser.get(String.class);
        
        if (baritoneCmd == null || baritoneCmd.isEmpty()) {
            mod.log("Please provide a baritone command. Usage: !baritone [baritone command]");
            finish();
            return;
        }
        
        boolean success = mod.getClientBaritone().getCommandManager().execute(baritoneCmd);
        if (success) {
            mod.log("Baritone command executed: " + baritoneCmd);
        } else {
            mod.logWarning("Failed to execute baritone command: " + baritoneCmd);
        }
        finish();
    }

    private static class RestArg extends StringArg {
        public RestArg(String name) {
            super(name, "");
        }

        @Override
        protected StringParser<String> getParser() {
            return reader -> {
                StringBuilder result = new StringBuilder();
                while (reader.hasNext()) {
                    result.append(reader.next()).append(" ");
                }
                String value = result.toString().trim();
                if (value.isEmpty()) {
                    if (!hasDefault) {
                        throw new CommandNotFinishedException("Expected command");
                    }
                    return defaultValue;
                }
                return value;
            };
        }

        @Override
        public Stream<String> getSuggestions(StringReader reader) {
            return Stream.empty();
        }
    }
}