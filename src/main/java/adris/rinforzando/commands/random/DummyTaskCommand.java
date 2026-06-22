package adris.rinforzando.commands.random;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.commandsystem.ArgParser;
import adris.rinforzando.commandsystem.Command;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.tasksystem.Task;

public class DummyTaskCommand extends Command {
    public DummyTaskCommand() {
        super("dummy", "Doesnt do anything");
    }

    @Override
    protected void call(Rinforzando mod, ArgParser parser) throws CommandException {
        mod.runUserTask(new DummyTask(), this::finish);
    }

    private class DummyTask extends Task {

        @Override
        protected void onStart() {

        }

        @Override
        protected Task onTick() {
            return null;
        }

        @Override
        protected void onStop(Task interruptTask) {

        }

        @Override
        protected boolean isEqual(Task other) {
            return false;
        }

        @Override
        protected String toDebugString() {
            return null;
        }
    }
}
