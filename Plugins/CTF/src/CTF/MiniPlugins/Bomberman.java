package CTF.MiniPlugins;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by Scyther on 16.06.2015.
 */
public class Bomberman extends JavaPlugin implements Listener
{
        public static Logger log = Logger.getLogger("Minecraft");

        public static Server serv;

        @Override
        public void onEnable()
        {
            log.info("[Bomberman] has been loaded");
            serv = getServer();
            getServer().getPluginManager().registerEvents(this, this);
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
        {
            if(command.getName().equalsIgnoreCase("Bomberman"))
            {
                if (args[0].equalsIgnoreCase("Enable"))
                {
                    Bomberman(true, (Player) sender);
                    return true;
                }
                else if (args[0].equalsIgnoreCase("Disable"))
                {
                    Bomberman(false, (Player) sender);
                    return true;
                }
                else
                {
                    sender.sendMessage("Usage: /Bomberman [Enable/Disable]]");
                    return false;
                }
            }
            else
                return false;
        }

        private void Bomberman(boolean enable, Player p)
        {
            World w = p.getWorld();

            if (enable == true)




                serv.broadcastMessage("Bomberman is active");
            else
                serv.broadcastMessage("Bomberman is inactive");
        }
}
