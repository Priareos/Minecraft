package CTF.Teams;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 4/28/2015.
 */
public class TeamMember {
    private Player player;

    private ItemStack[] inventoryContents;

    private ItemStack[] CTFInventory;
    private Location savedLocation;

    public TeamMember(Player p){
        player = p;
    }

    public String GetPlayerName()
    {
        return player.getDisplayName();
    }

    public Player GetPlayer()
    {
        return player;
    }

    public void Unequip()
    {
        player.getEquipment().setBoots(new ItemStack(Material.AIR));
        player.getEquipment().setChestplate(new ItemStack(Material.AIR));
        player.getEquipment().setLeggings(new ItemStack(Material.AIR));
        player.getEquipment().setHelmet(new ItemStack(Material.AIR));
    }

    public void Equip(Color c)
    {
        player.getEquipment().setBoots(ColorizeEquipment(new ItemStack(Material.LEATHER_BOOTS, 1), c));
        player.getEquipment().setChestplate(ColorizeEquipment(new ItemStack(Material.LEATHER_CHESTPLATE, 1), c));
        player.getEquipment().setLeggings(ColorizeEquipment(new ItemStack(Material.LEATHER_LEGGINGS, 1), c));
        player.getEquipment().setHelmet(ColorizeEquipment(new ItemStack(Material.LEATHER_HELMET, 1), c));
    }

    public void RemoveEquipment()
    {

    }

    public ItemStack ColorizeEquipment(ItemStack stack, Color c)
    {
        ItemStack itemStack = stack;
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(c); //oder z.B. meta.setColor(Color.fromRGB(255, 255, 255));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void SaveInventory()
    {
        inventoryContents = player.getInventory().getContents();
    }

    public void EmptyInventory()
    {
        player.getInventory().clear();
        player.updateInventory();
    }

    public void LoadInventory()
    {
        player.getInventory().setContents(inventoryContents);
    }

    public void TeleportToStartLocation(Location startLocation, boolean respawn)
    {
        if(!respawn)
        {
            SaveInventory();
            EmptyInventory();
            GiveCTFMaterials();
            savedLocation = player.getLocation();
        }

        GiveCTFEquipment();
        player.teleport(startLocation);
        SetFullHealth();
    }

    public void TeleportToOriginalLocation()
    {
        LoadInventory();
        player.teleport(savedLocation);
    }

    public void GiveCTFMaterials()
    {
        List<ItemStack> is = GetCTFMaterials();

        for(ItemStack i : is)
        {
            player.getInventory().addItem(i);
        }

        player.updateInventory();

    }

    public void GiveCTFEquipment()
    {
        List<ItemStack> is = GetCTFEquipment();
        for(ItemStack i : is)
        {
            player.getInventory().addItem(i);
        }

        player.updateInventory();

    }

    public List<ItemStack> GetCTFEquipment()
    {
        List<ItemStack> stack = new ArrayList<>();

        stack.add(new ItemStack(Material.IRON_SWORD, 1));
        stack.add(new ItemStack(Material.DIAMOND_AXE, 1));
        stack.add(new ItemStack(Material.DIAMOND_SPADE, 1));
        stack.add(new ItemStack(Material.DIAMOND_PICKAXE, 1));

        return stack;
    }


    public List<ItemStack> GetCTFMaterials()
    {
        List<ItemStack> stack = new ArrayList<>();

        stack.add(new ItemStack(Material.STONE, 64));
        stack.add(new ItemStack(Material.WOOD, 64));
        return stack;
    }

    public void SetFullHealth()
    {
        player.setHealth(player.getMaxHealth());
    }
}
