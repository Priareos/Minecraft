package CTF;

import CTF.Arena.ArenaManager;
import CTF.Teams.Team;
import CTF.Teams.TeamManager;
import CTF.Teams.TeamMember;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
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
import org.bukkit.Location;
import java.lang.reflect.Member;
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
    private boolean initializingCTF = false;

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
            if (args[0].equalsIgnoreCase("Start"))
            {
                if (initializingCTF == false){
                    initializingCTF = true;
                    if (args.length > 1)
                        initializeCTF(Long.valueOf(args[1]));
                    else
                        initializeCTF(5L);
                    return true;
                }
                else {
                    sender.sendMessage("CTF has already been initialized and will start soon");
                    return false;
                }

            }
            else if (args[0].equalsIgnoreCase("End"))
            {
                EndCTF();
                return true;
            }
            else if (args[0].equalsIgnoreCase("Team"))
            {
                if (enableCTF)
                    return TM.executeCommand(sender, command, label, args);
                else
                {
                    sender.sendMessage("Start round first to use team commands!");
                    return false;
                }
            }
            else if (args[0].equalsIgnoreCase("Arena"))
            {
                return AM.executeCommand(sender, command, label, args);
            }
            else
            {
                sender.sendMessage("Usage: /CTF [Arena | Start | Team]");
                sender.sendMessage("Arena - Arena settings");
                sender.sendMessage("Start - Starts a new round");
                sender.sendMessage("Team - Team settings");
                return false;
            }
        }
        else
            return false;

    }

    // Initialize Capture the Flag with a Timer
    private void initializeCTF(Long minutes) {
        if (minutes > 1)
         serv.broadcastMessage("Capture the Flag will begin in " + minutes + " minutes");
        else if (minutes == 1)
            serv.broadcastMessage("Capture the Flag will begin in " + minutes + " minute");
        enableCTF = true;
        BukkitTask task = new CTFCountdown(this, 10).runTaskTimer(this, (20L * 60L * minutes), 20L);
        // Hack - task should do this when finished
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
        serv.broadcastMessage("Teleporting players to their flags...");
        Flag flagRed = AM.GetFlag("red");
        Flag flagBlue = AM.GetFlag("blue");
        for (TeamMember memberR : TM.TeamRed.members)
        {
            TeleportPlayers(flagRed, memberR);
        }
        for (TeamMember memberB : TM.TeamBlue.members)
        {
            TeleportPlayers(flagBlue, memberB);
        }
    }

    private void TeleportPlayers (Flag flag, TeamMember member) {
        Player p = member.GetPlayer();
        World w = p.getWorld();
        boolean canTeleport = false;
        for(double x = -2; x <= 2; x++ )
        {
            for(double z = -2; z <= 2; z++ )
            {
                for(double y = 2; y >= -2; y-- )
                {
                    Location loc = new Location(w, flag.GetLocation().getX() + x, flag.GetLocation().getY() + y, flag.GetLocation().getZ() + z);
                    Block b = w.getBlockAt(loc);
                    if (b.getType() == Material.AIR) {
                        Location l = b.getLocation();
                        l.add(0,1,0);
                        member.TeleportToStartLocation(l);
                        canTeleport = true;
                        break;
                    }
                }

            }
        }
        if(!canTeleport)serv.broadcastMessage("Can't teleport " + member.GetPlayerName() + "! no space available");
    }

    public void EndCTF()
    {
        serv.broadcastMessage("Capture the Flag is Over");
        for (TeamMember memberR : TM.TeamRed.members)
        {
            memberR.TeleportToOriginalLocation();
        }
        for (TeamMember memberB : TM.TeamBlue.members)
        {
            memberB.TeleportToOriginalLocation();
        }
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
