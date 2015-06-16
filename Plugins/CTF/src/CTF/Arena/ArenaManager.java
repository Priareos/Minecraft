package CTF.Arena;

import CTF.CTFManager;
import CTF.Flag;
import CTF.Teams.Team;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.*;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Created by Michael on 4/30/2015.
 */
public class ArenaManager
{
    private HashMap<Location,Material> Arena;
    private org.bukkit.Location StartLoc;
    private org.bukkit.Location EndLoc;

    private Flag flag_Red = new Flag(DyeColor.RED, "Red");
    private Flag flag_Blue = new Flag(DyeColor.BLUE, "Blue");

    public ArenaManager()
    {
        Arena = new HashMap<Location,Material>();
    }

    public Flag GetFlag(String teamname)
    {
        if(teamname.equalsIgnoreCase("blue"))
        {
            return flag_Blue;
        }
        else if(teamname.equalsIgnoreCase("red"))
        {
            return flag_Red;
        }
        return null;
    }

    public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args[1].equalsIgnoreCase("Coordinates"))
        {
            Player p = (Player) sender;
            sender.sendMessage(p.getLocation().toString());
            return true;
        }
        else if(args[1].equalsIgnoreCase("Start"))
        {
            Player p = (Player) sender;
            StartLoc = p.getLocation();
            sender.sendMessage(p.getLocation().toString());
            return true;
        }
        else if(args[1].equalsIgnoreCase("End"))
        {
                Player p = (Player) sender;
                EndLoc = p.getLocation();
                sender.sendMessage(p.getLocation().toString());
                return true;
        }
        else if (args[1].equalsIgnoreCase("Flag"))
        {

            Player p = ((Player)sender);
            World w = p.getWorld();

            if (args[2].equalsIgnoreCase("Blue"))
            {

               return PlantFlag(p,w,flag_Blue);
            }
            else if (args[2].equalsIgnoreCase("Red"))
            {
                return PlantFlag(p,w,flag_Red);
            }
            else
                return false;
        }
        else if (args[1].equalsIgnoreCase("Save"))
        {
           return SaveZone((Player) sender);
        }
        else if(args[1].equalsIgnoreCase("Load"))
        {
            return LoadZone((Player)sender);
        }
        else
        {
            sender.sendMessage("Usage: /CTF Arena [Start | End | Flag | Save | Load]");
            sender.sendMessage("Start - Set arena start point");
            sender.sendMessage("End - Set arena end point");
            sender.sendMessage("Save - Saves the arena");
            sender.sendMessage("Load - Load the arena");
            sender.sendMessage("Flag [Red | Blue] - Sets team flag position");
            return false;
        }

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

    boolean PlantFlag(Player p, World w, Flag flag)
    {
        if(!flag.isHasBeenPlanted())
        {
            Location loc = p.getLocation();
            Vector dir = p.getLocation().getDirection().normalize();
            dir = dir.multiply(2);
            loc = new Location(w, loc.getX() + dir.getX(), loc.getY(), loc.getZ() + dir.getZ());

            flag.SetLocation(loc);
            flag.PlantFlag(w);

            p.sendMessage("Flag " + flag.getTeamName() + " has been placed!");
            return true;
        }
        else
        {
            flag.RemoveOldFlag();
            Location loc = p.getLocation();
            Vector dir = p.getLocation().getDirection().normalize();
            dir = dir.multiply(2);
            loc = new Location(w, loc.getX() + dir.getX(), loc.getY(), loc.getZ() + dir.getZ());

            flag.SetLocation(loc);
            flag.PlantFlag(w);

            p.sendMessage("Flag " + flag.getTeamName() + " has been replaced!");
            return true;
        }
    }
}
