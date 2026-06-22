package adris.rinforzando;

import adris.rinforzando.commands.*;
import adris.rinforzando.commands.random.ScanCommand;
import adris.rinforzando.commands.random.DummyTaskCommand;
import adris.rinforzando.commandsystem.exception.CommandException;

/**
 * Initializes rinforzando's built in commands.
 */
public class RinforzandoCommands {

    public static void init() throws CommandException {
        // List commands here
        Rinforzando.getCommandExecutor().registerNewCommand(
                new HelpCommand(),
                new GetCommand(),
                new ListCommand(),
                new EquipCommand(),
                new DepositCommand(),
                new StashCommand(),
                new GotoCommand(),
                new IdleCommand(),
                new HeroCommand(),
                new CoordsCommand(),
                new StatusCommand(),
                new InventoryCommand(),
                new LocateStructureCommand(),
                new StopCommand(),
                new PauseCommand(),
                new UnPauseCommand(),
                new SetGammaCommand(),
                new TestCommand(),
                new FoodCommand(),
                new MeatCommand(),
                new ReloadSettingsCommand(),
                new GamerCommand(),
                new MarvionCommand(),
                new DummyTaskCommand(),
                new FollowCommand(),
                new ScanCommand(),
                new GiveCommand()
        );
    }
}
