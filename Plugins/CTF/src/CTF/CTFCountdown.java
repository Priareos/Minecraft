package CTF;

/**
 * Created by Priareos on 12.05.2015.
 */
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CTFCountdown extends BukkitRunnable {

    private final JavaPlugin plugin;

    private int counter;

    public CTFCountdown(JavaPlugin plugin, int counter) {
        this.plugin = plugin;
        if (counter < 1) {
            throw new IllegalArgumentException("counter must be greater than 1");
        } else {
            this.counter = counter;
        }
    }

    @Override
    public void run() {
        if (counter > 0) {
            plugin.getServer().broadcastMessage("Capture the Flag will start in " + counter-- + " seconds");
        } else {
            this.cancel();
        }
    }
}