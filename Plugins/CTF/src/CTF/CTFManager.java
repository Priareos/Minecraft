package CTF;

import CTF.Arena.ArenaManager;
import CTF.Teams.TeamManager;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Logger;

/**
 * Created by Michael on 4/28/2015.
 */
public class CTFManager extends JavaPlugin implements Listener
{
    public static Logger log = Logger.getLogger("Minecraft");
    private TeamManager TM;
    private ArenaManager AM;

    public static Server serv;

    public CTFManager()
    {
        TM = new TeamManager();
        AM = new ArenaManager();
    }

    private boolean enableCTF = false;

    @Override
    public void onEnable() {
        log.info("[Capture the Flag Pluggin] has been loaded");
        super.onEnable();
        TM.onEnable();
        serv = getServer();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(command.getName().equalsIgnoreCase("CTF"))
        {
            if(args.length != 0)
                initializeCTF(Long.valueOf(args[0]));
            else
                initializeCTF(5L);
            return true;
        }
        else if(command.getName().equalsIgnoreCase("Team"))
        {
            if(enableCTF)
                return TM.executeCommand(sender, command, label, args);
            else
                return false;
        }
        else if(command.getName().equalsIgnoreCase("Arena"))
        {
            return AM.executeCommand(sender, command, label, args);
        }
        else if(command.getName().equalsIgnoreCase("Start"))
        {
            startCTF();
            return true;
        }
        else
            return false;
    }

    private void initializeCTF(Long minutes) {
        if (minutes > 1)
         serv.broadcastMessage("Capture the Flag will begin in " + minutes + " minutes");
        else if (minutes == 1)
            serv.broadcastMessage("Capture the Flag will begin in " + minutes + " minute");
        enableCTF = true;
        BukkitTask task = new CTFCountdown(this, 10).runTaskTimer(this, (20L * 60L * minutes), 20L);
        //hack - task should do this when finished
        BukkitScheduler scheduler = serv.getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                startCTF();
            }
        }, ((20L * 60L * minutes) + (10 * 20L)));
    }

    public void startCTF(){
        serv.broadcastMessage("Capture the Flag will start");
        serv.broadcastMessage("Teleporting players");
    }

    public static Server ReturnServer()
    {
        return serv;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {

       TM.onQuit(event);
        //getServer().broadcastMessage("Bye!");
    }

    @EventHandler (ignoreCancelled = true)
    public void onArmorSlot(InventoryClickEvent event) {

        Player p = getServer().getPlayerExact(event.getWhoClicked().getName());
        if ((event.getSlotType().equals(InventoryType.SlotType.ARMOR) && !event.getCurrentItem().getType().equals(Material.AIR))&&TM.GetPlayerTeam(p) != null)
            event.setCancelled(true);
    }
}
