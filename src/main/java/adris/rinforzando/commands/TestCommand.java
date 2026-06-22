package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.Playground;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.commandsystem.args.StringArg;

public class TestCommand extends Command {

    public TestCommand() {
        super("test", "Generic command for testing",
                new StringArg("extra", "")
        );
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        Playground.TEMP_TEST_FUNCTION(mod, parser.get(String.class));
        finish();
    }
}