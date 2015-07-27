package CTF.MiniPlugins;

import CTF.CTFManager;
import CTF.Teams.Team;
import CTF.Teams.TeamManager;
import CTF.Teams.TeamMember;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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
            CTFManager.serv.getScheduler().cancelTask(schedulerID);
            CTFManager.serv.broadcastMessage("Bomberman is inactive");
    }

    public void onBlockPlace(BlockPlaceEvent event)
    {
            Player p = event.getPlayer();
            World w = p.getWorld();
            int bombRange = 10;
            Block block = event.getBlock();

            p.sendMessage(event.getBlock().getType().name());
            Block b;

            if (block.getState().getData() instanceof Wool)
            {
                DyeColor color = ((Wool) block.getState().getData()).getColor();
                if (color == DyeColor.ORANGE)
                {
                    for (int x = 1; x < bombRange; x++)
                    {
                        for (int y = 1; y < bombRange; x++)
                        {
                            for (int z = 1; z < bombRange; x++)
                            {
                                b = w.getBlockAt(x, (int)block.getLocation().getY(), (int)block.getLocation().getZ());
                                if(b.getType() != Material.AIR ){
                                    b.setType(Material.AIR);                                }

                                b = w.getBlockAt((int)block.getLocation().getX(), y, (int)block.getLocation().getZ());
                                if(b.getType() != Material.AIR ){
                                    b.setType(Material.AIR);
                                }
                                b = w.getBlockAt((int) block.getLocation().getX(), (int) block.getLocation().getY(), z);
                                if(b.getType() != Material.AIR ){
                                    b.setType(Material.AIR);
                                }
//                                w.getBlockAt((int)block.getLocation().getX(), y, (int)block.getLocation().getZ()).setType(Material.AIR);
//                                w.getBlockAt((int)block.getLocation().getX(), (int)block.getLocation().getY(), z).setType(Material.AIR);
                            }
                        }

                    }
                }
        }
    }
}
