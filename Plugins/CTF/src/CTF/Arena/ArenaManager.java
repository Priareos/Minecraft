package CTF.Arena;

import CTF.CTFManager;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by Michael on 4/30/2015.
 */
public class ArenaManager
{
    private HashMap<Location,Material> Arena;
    private org.bukkit.Location StartLoc;
    private org.bukkit.Location EndLoc;

    private Location flagLoc_Red;
    private Location flagLoc_Blue;

    public ArenaManager()
    {
        Arena = new HashMap<Location,Material>();
    }

    public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args[0].equalsIgnoreCase("Coordinates"))
        {
            Player p = (Player) sender;
            sender.sendMessage(p.getLocation().toString());
            return true;
        }
        else if (args[0].equalsIgnoreCase("SetZone"))
        {
            if(args[1].equalsIgnoreCase("Start"))
            {
                Player p = (Player) sender;
                StartLoc = p.getLocation();
                sender.sendMessage(p.getLocation().toString());
                return true;
            }
            if(args[1].equalsIgnoreCase("End"))
            {
                Player p = (Player) sender;
                EndLoc = p.getLocation();
                sender.sendMessage(p.getLocation().toString());
                return true;
            }
            else
                return false;
        }
        else if (args[0].equalsIgnoreCase("SetFlag"))
        {

            Player p = ((Player)sender);
            World w = p.getWorld();

            if (args[1].equalsIgnoreCase("Blue"))
            {

                flagLoc_Blue = p.getLocation();
                w.getBlockAt(flagLoc_Blue).setType(Material.GOLD_BLOCK);
                return true;
            }
            else if (args[1].equalsIgnoreCase("Red"))
            {
                flagLoc_Red = ((Player)sender).getLocation();
                w.getBlockAt(flagLoc_Blue).setType(Material.GOLD_BLOCK);
                return true;
            }
            else
                return false;
        }
        else if (args[0].equalsIgnoreCase("SaveZone"))
        {
           return SaveZone((Player) sender);
        }
        else if(args[0].equalsIgnoreCase("LoadZone"))
        {
            return LoadZone((Player)sender);
        }
        else
            return false;
    }

    public boolean LoadZone(Player p)
    {
        World w = p.getWorld();

        for(Location l : Arena.keySet())
        {
            Block b = w.getBlockAt(l);
            b.setType(Arena.get(l));
        }

        return true;
    }

    public boolean SaveZone(Player p)
    {
        Arena.clear();
        World w = p.getWorld();

        int StartX = (int)StartLoc.getX();
        int StartZ = (int)StartLoc.getZ();
        int EndX = (int)EndLoc.getX();
        int EndZ = (int)EndLoc.getZ();

        int biggerNumberX = GetBiggerNumber(StartX, EndX);
        int smallerNumberX = GetSmallerNumber(StartX, EndX);
        int biggerNumberZ = GetBiggerNumber(StartZ, EndZ);
        int smallerNumberZ = GetSmallerNumber(StartZ, EndZ);

        for (int x = smallerNumberX; x < biggerNumberX; x++)
        {
            for (int z = smallerNumberZ; z < biggerNumberZ; z++)
            {
                for (int y = 0; y <= 256; y++)
                {
                    w.getBlockAt(x, y, smallerNumberZ).setType(Material.STONE);
                    w.getBlockAt(x, y, biggerNumberZ - 1).setType(Material.CLAY);
                    w.getBlockAt(smallerNumberX, y, z).setType(Material.STONE);
                    w.getBlockAt(biggerNumberX - 1, y, z).setType(Material.CLAY);
                    Block b = w.getBlockAt(x,y,z);
                    if(b.getType() == Material.GOLD_ORE)
                    {
                        b.setType(Material.STONE);
                    }
                    Arena.put(b.getLocation(), b.getType());
                }
            }
        }


        return true;

    }

    int GetBiggerNumber(int x, int y)
    {
        return x > y ? x : y;
    }

    int GetSmallerNumber(int x, int y)
    {
        return x < y ? x : y;
    }
}
