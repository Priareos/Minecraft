package CTF;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.Wool;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Michael on 5/26/2015.
 */
public class Flag
{
    private Location location;
    private DyeColor color;

    private Location locationFlag;

    private Boolean hasBeenPlanted = false;

    private HashMap<Location, Material> savedBlocks = new HashMap<>();

    private String teamName;

    private List<Location> plantArea = new ArrayList<Location>();

    public Flag(DyeColor c, String name)
    {
        setColor(c);
        setTeamName(name);
    }

    public DyeColor getColor()
    {
        return color;
    }

    public void setColor(DyeColor color)
    {
        this.color = color;
    }

    public void SetLocation(Location l)
    {
        location = l;
        locationFlag = l.add(new Vector(0,1,0));
    }

    public Location GetLocation()
    {
        return location;
    }

    public String getTeamName()
    {
        return teamName;
    }

    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }

    public void PlantFlag(World w)
    {
        PlantAtLocation(locationFlag, w);
    }

    public Boolean isHasBeenPlanted()
    {
        return hasBeenPlanted;
    }

    public void setHasBeenPlanted(Boolean hasBeenPlanted)
    {
        this.hasBeenPlanted = hasBeenPlanted;
    }

    private void PlantAtLocation(Location l, World w)
    {
        savedBlocks.clear();
        plantArea.clear();

        for(double x = -1; x <= 1; x++ )
        {
            for(double z = -1; z <= 1; z++ )
            {
                for(double y = 0; y >= -2; y-- )
                {
                    Location loc = new Location(w, l.getX() + x, l.getY() + y, l.getZ() + z);
                    // loc.add(new Vector(x,0,z));
                    savedBlocks.put(loc, loc.getBlock().getType());
                    Block b = w.getBlockAt(loc);
                    b.setType(Material.AIR);
                    plantArea.add(loc);
                }
            }
        }

        Block block = w.getBlockAt(l);
        block.setType(Material.WOOL);
        block.setData(color.getData());


        Location pedestalLoc = l;
        pedestalLoc.add(new Vector(0, -1, 0));


        BuildPedestal(pedestalLoc, w);
        this.setHasBeenPlanted(true);
    }

    private void BuildPedestal(Location l, World w)
    {
        Block block = w.getBlockAt(l);
        block.setType(Material.GOLD_BLOCK);

        l.add(new Vector(0, -1, 0));

        for(double x = -1; x <= 1; x++ )
        {
            for(double z = -1; z <= 1; z++ )
            {
                Location loc = new Location(w,l.getX()+x, l.getY(),l.getZ()+z);
               // loc.add(new Vector(x,0,z));
                block = w.getBlockAt(loc);
                block.setType(Material.GOLD_BLOCK);
            }
        }

    }

    public boolean CheckLocations(Location l)
    {
        boolean retVal = false;

        for(Location location : plantArea)
        {
            if(location == l)
            {
                retVal = true;
            }
        }
        return retVal;
    }

    public boolean CheckBlocks(World w, DyeColor dyeColor)
    {
        boolean retVal = false;

        for(Location location : plantArea)
        {
            Block b = w.getBlockAt(location);
            if(b.getState().getData() instanceof Wool)
            {
                DyeColor color = ((Wool) b.getState().getData()).getColor();
                if (color == dyeColor)
                    retVal = true;
            }
        }
        return retVal;
    }

    public void RemoveOldFlag()
    {
        if(!savedBlocks.isEmpty())
        {
            for(Location loc : savedBlocks.keySet())
            {
                loc.getBlock().setType(savedBlocks.get(loc));
            }
        }

    }


}
