package adris.rinforzando.commands;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.commandsystem.args.StringArg;
import adris.rinforzando.tasks.movement.FollowPlayerTask;

public class FollowCommand extends Command {
    public FollowCommand() {
        super("follow", "Follows you or someone else",
                new StringArg("username", null)
        );
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        String username = parser.get(String.class);

        if (username == null) {
            if (mod.getButler().hasCurrentUser()) {
                username = mod.getButler().getCurrentUser();
            } else {
                mod.logWarning("No butler user currently present. Running this command with no user argument can ONLY be done via butler.");
                finish();
                return;
            }
        }

        mod.runUserTask(new FollowPlayerTask(username), this::finish);
    }
}