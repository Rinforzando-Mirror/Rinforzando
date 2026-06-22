package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.commandsystem.args.EnumArg;
import adris.rinforzando.tasks.movement.GoToStrongholdPortalTask;
import adris.rinforzando.tasks.movement.LocateDesertTempleTask;

public class LocateStructureCommand extends Command {

    public LocateStructureCommand() {
        super("locate_structure", "Locate a world generated structure.",
                new EnumArg<>("structure", Structure.class)
        );
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        Structure structure = parser.get(Structure.class);
        switch (structure) {
            case STRONGHOLD:
                mod.runUserTask(new GoToStrongholdPortalTask(1), this::finish);
                break;
            case DESERT_TEMPLE:
                mod.runUserTask(new LocateDesertTempleTask(), this::finish);
                break;
        }
    }

    public enum Structure {
        DESERT_TEMPLE,
        STRONGHOLD
    }
}