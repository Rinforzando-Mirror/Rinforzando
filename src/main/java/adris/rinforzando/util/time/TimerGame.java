package adris.rinforzando.util.time;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.Debug;
import adris.rinforzando.mixins.ClientConnectionAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;

// Simple timer
public class TimerGame extends BaseTimer {

    private ClientConnection lastConnection;

    public TimerGame(double intervalSeconds) {
        super(intervalSeconds);
    }

    private static double getTime(ClientConnection connection) {
        if (connection == null) return 0;
        return (double) ((ClientConnectionAccessor) connection).getTicks() / 20.0;
    }

    @Override
    protected double currentTime() {
        if (!Rinforzando.inGame()) {
            Debug.logError("Running game timer while not in game.");
            return 0;
        }
        // If we change connections, our game time will also be reset. In that case, offset our time to reflect that change.
        ClientConnection currentConnection = null;
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            currentConnection = MinecraftClient.getInstance().getNetworkHandler().getConnection();
        }
        if (currentConnection != lastConnection) {
            if (lastConnection != null) {
                double prevTimeTotal = getTime(lastConnection);
                Debug.logInternal("(TimerGame: New connection detected, offsetting by " + prevTimeTotal + " seconds)");
                setPrevTimeForce(getPrevTime() - prevTimeTotal);
            }
            lastConnection = currentConnection;
        }
        // Use ticks for timing. 20TPS is normal, if we go slower that's fine.
        // Adding a "mod" argument here would be hell across the board. Not happening.
        return getTime(currentConnection);
    }
}
