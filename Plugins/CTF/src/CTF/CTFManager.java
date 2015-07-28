package CTF;

import CTF.Arena.ArenaManager;
import CTF.MiniPlugins.Bomberman;
import CTF.Teams.Team;
import CTF.Teams.TeamManager;
import CTF.Teams.TeamMember;
import javafx.scene.layout.Priority;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Created by Michael on 4/28/2015.
 */
public class CTFManager extends JavaPlugin implements Listener
{
    public static Logger log = Logger.getLogger("Minecraft");
    private TeamManager TM;
    private ArenaManager AM;
    private ScoreSystem SCRSYS;
    private Bomberman BM;
    public static Server serv;

    public CTFManager()
    {
        TM = new TeamManager();
        AM = new ArenaManager();
        BM = new Bomberman(this);
        SCRSYS = new ScoreSystem(this, TM, AM, BM);
    }

    private boolean enableCTF = false;
    private boolean initializingCTF = false;
    private boolean CTFGameInProgress = false;


    @Override
    public void onEnable() {
        log.info("[Capture the Flag Pluggin] has been loaded");
        super.onEnable();
        TM.onEnable();
        serv = getServer();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(SCRSYS,this);
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
        else if(command.getName().equalsIgnoreCase("Bomberman"))
        {
            if (args[0].equalsIgnoreCase("Enable"))
            {
                BM.EnableDisable(TM, true);
                return true;
            }
            else if (args[1].equalsIgnoreCase("Disable"))
            {
                BM.EnableDisable(TM, false);
                return true;
            }
            else
            {
                return false;
            }
        }
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
            TeleportPlayers(flagRed, memberR, false);
        }
        for (TeamMember memberB : TM.TeamBlue.members)
        {
            TeleportPlayers(flagBlue, memberB, false);
        }
        CTFGameInProgress = true;
        SCRSYS.SetCheckScore(true);
    }

    private void TeleportPlayers (Flag flag, TeamMember member, boolean respawn) {
        Player p = member.GetPlayer();
        World w = p.getWorld();
        boolean canTeleport = false;
        Location l = p.getLocation();
        for(double x = -2; x <= 2; x++ )
        {
            for(double z = -2; z <= 2; z++ )
            {
                for(double y = 2; y >= -2; y-- )
                {
                    Location loc = new Location(w, flag.GetLocation().getX() + x, flag.GetLocation().getY() + y, flag.GetLocation().getZ() + z);
                    Block b = w.getBlockAt(loc);
                    if (b.getType() == Material.AIR) {
                        l = b.getLocation();
                        l.add(0, 1, 0);

                        canTeleport = true;
                    }
                }

            }
        }
        if(canTeleport) member.TeleportToStartLocation(l, respawn);
        if(!canTeleport)serv.broadcastMessage("Can't teleport " + member.GetPlayerName() + "! no space available");
        else serv.broadcastMessage("Teleporting");
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

        enableCTF = false;
        CTFGameInProgress = false;

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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        if(CTFGameInProgress)
        {
            final Player p = event.getPlayer();
            if (TM.GetPlayerTeam(p) != null)
            {
                Team team = TM.GetPlayerTeam(p);

                final Flag f = AM.GetFlag(team.name);
                serv.broadcastMessage("Respawned " + team.name + " " + f.getTeamName());
                final TeamMember memb = team.GetMember(p);
                final CTFManager ctfmanager = this;
                memb.Equip(team.color);

                @SuppressWarnings("unused") int taskId= Bukkit.getScheduler().scheduleSyncDelayedTask(this,new Runnable(){
                    @Override public void run()
                    {
                        TeleportPlayers(f, memb, true);
                    }
                }
                        ,10L);


            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        ArrayList<ItemStack> itemsToRemove = new ArrayList();

        World w = event.getEntity().getWorld();
        Block blockAtDeathLoc = w.getBlockAt(event.getEntity().getLocation());

       for(ItemStack drop : event.getDrops())
       {
           if(drop.getType() == Material.WOOL)
           {
               MaterialData md = drop.getData();
               itemsToRemove.add(drop);
               blockAtDeathLoc.setType(Material.WOOL);
               blockAtDeathLoc.setData(md.getData());
           }
           else if(drop.getType() == Material.LEATHER_BOOTS
                   || drop.getType() == Material.LEATHER_CHESTPLATE
                   || drop.getType() == Material.LEATHER_LEGGINGS
                   || drop.getType() == Material.LEATHER_HELMET
                   || drop.getType() == Material.IRON_SWORD
                   || drop.getType() == Material.DIAMOND_AXE
                   || drop.getType() == Material.DIAMOND_SPADE
                   || drop.getType() == Material.DIAMOND_PICKAXE
                   )
           {
               itemsToRemove.add(drop);
           }
       }

        for(ItemStack is : itemsToRemove)
        {
            event.getDrops().remove(is);
        }
    }
}
