package eu.genesismc.genesissmp;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PreventBlockXray implements Listener {
/*
    Plugin plugin = GenesisSMP.getInstance();
    FileConfiguration config = GenesisSMP.getPlugin().getConfig();

    @EventHandler
    public void PlayerMove(PlayerMoveEvent e) {

        Block block = e.getPlayer().getLocation().getBlock();
        Material type = block.getType();

        if (type == Material.COMPOSTER || type == Material.CAULDRON) {
            if (!config.getBoolean("PreventBlockXray.enabled")) { return; }

            // Run as a sync task to prevent any lag
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Location loc = block.getLocation();
                    Player player = e.getPlayer();
                    String blockName = block.getType().toString().toLowerCase();
                    String w = loc.getWorld().getName();
                    int x = (int) loc.getX(), y = (int) loc.getY(), z = (int) loc.getZ();
                    int highestAbove = loc.getWorld().getHighestBlockYAt(x, z);
                    try {
                        for (int j = y; j <= highestAbove; j++) {
                            Block b = Bukkit.getWorld(w).getBlockAt(x, j, z);
                            if (b.getType().toString().contains("PISTON")) {
                                // Remove the piston and drop it
                                Bukkit.getWorld(w).dropItemNaturally(loc, new ItemStack(b.getType(), 1));
                                b.setType(Material.AIR);
                                // Send messages
                                player.sendMessage(ChatColor.YELLOW + "You cannot stand in a " + blockName + " with a piston above. You have been moved to a nearby location.");
                                for (Player online : Bukkit.getOnlinePlayers()) {
                                    if (online.hasPermission("illegalstack.notify")) {
                                        String msg = "&3[&eGenesisSMP&3] Prevented &e" + player.getName() + " &3from &estanding in a&e " + blockName.toUpperCase() + "&3 with a piston above @&b " + w + " " + x + " " + y + " " + z;
                                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    }
                                }
                            }
                        }
                    } catch (NullPointerException npe) {
                        Bukkit.getLogger().info("NPE encountered in PreventBlockXray#PlayerMove");
                    }
                }
            }, 1L);

        }
    }

    @EventHandler
    private void FallingBlock(EntityChangeBlockEvent e) {
        if (e.getEntityType() == EntityType.FALLING_BLOCK && e.getTo().isAir()) {
            if (e.getBlock().getType() == Material.SAND || e.getBlock().getType() == Material.GRAVEL || e.getBlock().getType() == Material.SOUL_SAND || e.getBlock().getType().toString().contains("CONCRETE_POWDER")) {
                int checkBelow = e.getBlock().getY();
                Location l = e.getBlock().getLocation();
                Block b = Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l);

                for (int count = 1; count <= checkBelow; count++) {
                    Location loc = b.getLocation().subtract(0, count, 0);
                    Material check = loc.getBlock().getType();
                    if (check.toString().endsWith("AIR")) {
                        continue;
                    }
                    if (check == Material.COMPOSTER || check == Material.CAULDRON) {
                        try {
                            ItemStack fallingBlock = new ItemStack(e.getBlock().getType(), 1);
                            e.getBlock().setType(Material.AIR);
                            e.setCancelled(true);
                            loc.getWorld().dropItemNaturally(loc, fallingBlock);
                            // Send messages
                            String w = loc.getWorld().getName();
                            int x = (int) loc.getX(), y = (int) loc.getY(), z = (int) loc.getZ();
                            for (Player online : Bukkit.getOnlinePlayers()) {
                                if (online.hasPermission("illegalstack.notify")) {
                                    String msg = "&3[&eGenesisSMP&3] Prevented a &efalling block &3from being placed &eabove a composter/cauldron &3@&b " + w + " " + x + " " + y + " " + z;
                                    online.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                }
                            }
                        } catch (NullPointerException npe) {
                        Bukkit.getLogger().info("NPE encountered in PreventBlockXray#FallingBlock");
                    }
                    }
                    break;
                }
            }
        }
    }

 */

}
