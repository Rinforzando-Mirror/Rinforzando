package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.*;
import adris.rinforzando.commandsystem.args.GoToTargetArg;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.tasks.movement.DefaultGoToDimensionTask;
import adris.rinforzando.tasks.movement.GetToBlockTask;
import adris.rinforzando.tasks.movement.GetToXZTask;
import adris.rinforzando.tasks.movement.GetToYTask;
import adris.rinforzando.tasksystem.Task;
import net.minecraft.util.math.BlockPos;

/**
 * Out of all the commands, this one probably demonstrates
 * why we need a better arg parsing system. Please.
 */
public class GotoCommand extends Command {

    public GotoCommand() {
        // x z
        // x y z
        // x y z dimension
        // (dimension)
        // (x z dimension)
        super("goto", "Tell bot to travel to a set of coordinates",
                new GoToTargetArg("[x y z dimension]/[x z dimension]/[y dimension]/[dimension]/[x y z]/[x z]/[y]")
        );
    }

    public static Task getMovementTaskFor(GotoTarget target) {
        return switch (target.getType()) {
            case XYZ -> new GetToBlockTask(new BlockPos(target.getX(), target.getY(), target.getZ()), target.getDimension());
            case XZ -> new GetToXZTask(target.getX(), target.getZ(), target.getDimension());
            case Y -> new GetToYTask(target.getY(), target.getDimension());
            case NONE -> new DefaultGoToDimensionTask(target.getDimension());
        };
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        GotoTarget target = parser.get(GotoTarget.class);
        mod.runUserTask(getMovementTaskFor(target), this::finish);
    }
}
