package adris.rinforzando;

import adris.rinforzando.commands.BaritoneCommand;
import adris.rinforzando.commands.HelpCommand;
import adris.rinforzando.commands.GetCommand;
import adris.rinforzando.commands.ListCommand;
import adris.rinforzando.commands.EquipCommand;
import adris.rinforzando.commands.DepositCommand;
import adris.rinforzando.commands.StashCommand;
import adris.rinforzando.commands.GotoCommand;
import adris.rinforzando.commands.IdleCommand;
import adris.rinforzando.commands.HeroCommand;
import adris.rinforzando.commands.CoordsCommand;
import adris.rinforzando.commands.StatusCommand;
import adris.rinforzando.commands.InventoryCommand;
import adris.rinforzando.commands.LocateStructureCommand;
import adris.rinforzando.commands.StopCommand;
import adris.rinforzando.commands.PauseCommand;
import adris.rinforzando.commands.UnPauseCommand;
import adris.rinforzando.commands.SetGammaCommand;
import adris.rinforzando.commands.TestCommand;
import adris.rinforzando.commands.FoodCommand;
import adris.rinforzando.commands.MeatCommand;
import adris.rinforzando.commands.ReloadSettingsCommand;
import adris.rinforzando.commands.GamerCommand;
import adris.rinforzando.commands.DiscordCommand;
import adris.rinforzando.commands.random.DummyTaskCommand;
import adris.rinforzando.commands.FollowCommand;
import adris.rinforzando.commands.random.ScanCommand;
import adris.rinforzando.commands.GiveCommand;
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
                new DiscordCommand(),
                new DummyTaskCommand(),
                new FollowCommand(),
                new ScanCommand(),
                new GiveCommand(),
                new BaritoneCommand()
        );
    }
}
