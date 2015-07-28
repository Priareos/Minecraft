package CTF;

import CTF.Arena.ArenaManager;
import CTF.MiniPlugins.Bomberman;
import CTF.Teams.Team;
import CTF.Teams.TeamManager;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;

import java.lang.reflect.Member;

/**
 * Created by diene_000 on 7/18/2015.
 */
public class ScoreSystem implements Listener
{
    private CTFManager CTM;
    private TeamManager TM;
    private ArenaManager AM;
    private Bomberman BM;

    private boolean CheckScore = false;



    public ScoreSystem(CTFManager c, TeamManager t, ArenaManager a, Bomberman b)
    {
        CTM = c;
        TM = t;
        AM = a;
        BM = b;
    }

    public void SetCheckScore(boolean enabled)
    {
        CheckScore = enabled;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
         if(CheckScore)
         {
            Block block = event.getBlock();
            Team team = TM.GetPlayerTeam(event.getPlayer());
            event.getPlayer().sendMessage(event.getBlock().getType().name());

            if (block.getState().getData() instanceof Wool && team != null)
            {
                DyeColor color = ((Wool) block.getState().getData()).getColor();
                if (color == DyeColor.RED && team.color == Color.BLUE)
                {
                    if(AM.GetFlag("Blue").CheckBlocks(event.getPlayer().getWorld(), DyeColor.RED))
                    {
                        CTM.getServer().broadcastMessage("Blue Has Won!");
                        CTM.EndCTF();
                    }
                }
                else if (color == DyeColor.BLUE && team.color == Color.RED)
                {
                    if(AM.GetFlag("red").CheckBlocks(event.getPlayer().getWorld(), DyeColor.BLUE))
                    {
                        CTM.getServer().broadcastMessage("Red Has Won!");
                        CTM.EndCTF();
                    }
                }
                else if (color == DyeColor.ORANGE)
                {
                    BM.BombHasBeenPlaced(event);
                }
            }
        }
    }
}
