package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.Debug;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.commandsystem.args.DoubleArg;
import adris.rinforzando.multiversion.OptionsVer;

public class SetGammaCommand extends Command {

    public SetGammaCommand() throws CommandException {
        super("gamma", "Sets the brightness to a value",
                new DoubleArg("gamma", 1.0)
        );
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        double gammaValue = parser.get(Double.class);
        changeGamma(gammaValue);
    }

    public static void changeGamma(double value) {
        Debug.logMessage("Gamma set to " + value);

        OptionsVer.setGamma(value);
    }

}