package eu.genesismc.genesissmp;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PreventBlockXray implements Listener {

    Plugin plugin = GenesisSMP.getInstance();

    @EventHandler
    public void PlayerMove(PlayerMoveEvent e) {

        Block block = e.getPlayer().getLocation().getBlock();
        Material type = block.getType();
        if (type == Material.COMPOSTER || type == Material.CAULDRON) {
            // Run as a sync task to prevent any lag
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {

                    Location loc = block.getLocation();
                    String blockName = block.getType().toString().toLowerCase();
                    String w = loc.getWorld().getName();
                    int x = (int) loc.getX(), y = (int) loc.getY(), z = (int) loc.getZ();

                    // Remove the block and drop it as a ground entity
                    block.setType(Material.AIR);
                    ItemStack drop = new ItemStack(type, 1);
                    block.getLocation().getWorld().dropItemNaturally(loc.add(0.5, 0, 0.5), drop);

                    // Send messages
                    e.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot stand in a " + blockName + ". It has been dropped as an item.");
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (online.hasPermission("illegalstack.notify")) {
                            String msg = "&3[&eGenesisSMP&3] Prevented &e" + e.getPlayer().getName() + " &3from &estanding in a&e " + blockName.toUpperCase() + "&3 @&b " + w + " " + x + " " + y + " " + z;
                            online.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        }
                    }
                }
            }, 1L);
        }
    }



    // BlockPistonExtendEvent
    @EventHandler
    public void PistonCheck(BlockPistonExtendEvent e) {

    }

}
