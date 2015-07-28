package CTF.MiniPlugins;

import CTF.CTFManager;
import CTF.Teams.TeamManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.DyeColor;
import org.bukkit.material.Wool;

/**
 * Created by Scyther on 16.06.2015.
 */
public class Bomberman implements Listener
{
    private CTFManager CTFM;
    private TeamManager TM;
    private int schedulerID;

    public Bomberman(CTFManager C)
    {
        CTFM = C;
    }

    public void EnableDisable(TeamManager teamMngr, boolean enable)
    {
        TM = teamMngr;
        if (enable == true)
        {
            CTFManager.serv.broadcastMessage("Bomberman is active");
            schedulerID =  CTFManager.serv.getScheduler().scheduleAsyncRepeatingTask(CTFM, new Runnable()
            {
                Wool wool = new Wool(DyeColor.ORANGE);
                ItemStack stack = wool.toItemStack(1);

                public void run()
                {
                    CTFManager.serv.broadcastMessage("NewBombs!");
                    for(Player p :  CTFManager.serv.getOnlinePlayers())
                    {
                        p.getInventory().addItem(stack);
                    }
                }
            }, 200L, 200L);
        }
        else
        {
            CTFManager.serv.getScheduler().cancelTask(schedulerID);
            CTFManager.serv.broadcastMessage("Bomberman is inactive");
        }
    }

    public void BombHasBeenPlaced(BlockPlaceEvent event)
    {
        Player p = event.getPlayer();
        World w = p.getWorld();
        int bombRange = 10;
        Block block = event.getBlock();
        Player pKill;

        p.sendMessage(event.getBlock().getType().name());
        Block b = event.getBlock();
        w.createExplosion(b.getLocation(), 1f, true);

        for (int x = 1; x < bombRange; x++)
        {
            for (int y = 1; y < bombRange; y++)
            {
                for (int z = 1; z < bombRange; z++)
                {
                    b = w.getBlockAt((int)block.getLocation().getX() + x, (int)block.getLocation().getY() + y, (int)block.getLocation().getZ());
                    b.setType(Material.AIR);
                    pKill = getPlayerAt(b.getLocation(), w, b);
                    if (pKill != null)
                    {
                        pKill.setHealth(0);
                    }
                    b = w.getBlockAt((int)block.getLocation().getX(), (int)block.getLocation().getY() + y, (int)block.getLocation().getZ() + z);
                    b.setType(Material.AIR);
                    pKill = getPlayerAt(b.getLocation(), w, b);
                    if (pKill != null)
                    {
                        pKill.setHealth(0);
                    }
                    b = w.getBlockAt((int)block.getLocation().getX() + x, (int)block.getLocation().getY() - y, (int)block.getLocation().getZ());
                    b.setType(Material.AIR);
                    pKill = getPlayerAt(b.getLocation(), w, b);
                    if (pKill != null)
                    {
                        pKill.setHealth(0);
                    }
                    b = w.getBlockAt((int)block.getLocation().getX(), (int)block.getLocation().getY() - y, (int)block.getLocation().getZ() + z);
                    b.setType(Material.AIR);
                    pKill = getPlayerAt(b.getLocation(), w, b);
                    if (pKill != null)
                    {
                        pKill.setHealth(0);
                    }
                    b = w.getBlockAt((int)block.getLocation().getX() - x, (int)block.getLocation().getY() + y, (int)block.getLocation().getZ());
                    b.setType(Material.AIR);
                    pKill = getPlayerAt(b.getLocation(), w, b);
                    if (pKill != null)
                    {
                        pKill.setHealth(0);
                    }
                    b = w.getBlockAt((int)block.getLocation().getX(), (int)block.getLocation().getY() + y, (int)block.getLocation().getZ() - z);
                    b.setType(Material.AIR);
                    pKill = getPlayerAt(b.getLocation(), w, b);
                    if (pKill != null)
                    {
                        pKill.setHealth(0);
                    }
                    b = w.getBlockAt((int)block.getLocation().getX() - x, (int)block.getLocation().getY() - y, (int)block.getLocation().getZ());
                    b.setType(Material.AIR);
                    pKill = getPlayerAt(b.getLocation(), w, b);
                    if (pKill != null)
                    {
                        pKill.setHealth(0);
                    }
                    b = w.getBlockAt((int)block.getLocation().getX(), (int)block.getLocation().getY() - y, (int)block.getLocation().getZ() - z);
                    b.setType(Material.AIR);
                    pKill = getPlayerAt(b.getLocation(), w, b);
                    if (pKill != null)
                    {
                        pKill.setHealth(0);
                    }
                }
//                        CTFManager.serv.broadcastMessage("Bombing");
            }
        }
    }

    public Player getPlayerAt(org.bukkit.Location loc, World w, Block b)
    {
        for (Entity e : w.getChunkAt(b).getEntities()) {
            if (e instanceof Player)
                if ((e.getLocation().getX() >= loc.getX() - 0.5 && e.getLocation().getX() <= loc.getX() + 0.5) &&
                        (e.getLocation().getY() >= loc.getY() - 0.5 && e.getLocation().getY() <= loc.getY() + 0.5) &&
                        (e.getLocation().getZ() >= loc.getZ() - 0.5 && e.getLocation().getZ() <= loc.getZ() + 0.5)) {
                    return (Player) e;
                }
        }
        return null;
    }
}
