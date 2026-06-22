package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.*;
import adris.rinforzando.commandsystem.args.IntArg;
import adris.rinforzando.commandsystem.args.ItemTargetArg;
import adris.rinforzando.commandsystem.args.ListArg;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.tasks.container.StoreInStashTask;
import adris.rinforzando.util.BlockRange;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.helpers.WorldHelper;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class StashCommand extends Command {
    public StashCommand() throws CommandException {
        // stash <stash_x> <stash_y> <stash_z> <stash_radius> [item list]
        super("stash", "Store an item in a chest/container stash. Will deposit ALL non-equipped items if item list is empty.",
                new IntArg("x_start"),
                new IntArg("y_start"),
                new IntArg("z_start"),
                new IntArg("x_end"),
                new IntArg("y_end"),
                new IntArg("z_end"),
                new ListArg<>(new ItemTargetArg("stack"), "items (empty for ALL)", null, false)
        );
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        BlockPos start = new BlockPos(
                parser.get(Integer.class),
                parser.get(Integer.class),
                parser.get(Integer.class)
        );
        BlockPos end = new BlockPos(
                parser.get(Integer.class),
                parser.get(Integer.class),
                parser.get(Integer.class)
        );

        List<ItemTarget> itemList = parser.get(List.class);

        ItemTarget[] items;
        if (itemList == null) {
            items = DepositCommand.getAllNonEquippedOrToolItemsAsTarget(mod);
        } else {
            items = itemList.toArray(new ItemTarget[0]);
        }

        mod.runUserTask(new StoreInStashTask(true, new BlockRange(start, end, WorldHelper.getCurrentDimension()), items), this::finish);
    }
}
