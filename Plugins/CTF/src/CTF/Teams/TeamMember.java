package CTF.Teams;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * Created by Michael on 4/28/2015.
 */
public class TeamMember {
    private Player player;

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
}
