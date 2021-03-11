package eu.genesismc.genesissmp;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class EndLootCrate implements CommandExecutor, Listener {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearcrate")) {

            if (!sender.hasPermission("clearcrate.admin")) {
                return false;
            }

            FileConfiguration config = GenesisSMP.getPlugin().getConfig();
            if (!config.getBoolean("EndLootCrate.enabled")) {
                sender.sendMessage(ChatColor.RED + "This module is not enabled.");
                return true;
            }

            int x = config.getInt("EndLootCrate.x");
            int y = config.getInt("EndLootCrate.y");
            int z = config.getInt("EndLootCrate.z");
            long delay = config.getLong("EndLootCrate.delay");

            if (args[0].equalsIgnoreCase("now")) {
                try {
                    Bukkit.getServer().getWorld("world_the_end").getBlockAt(x, y, z).setType(Material.AIR);
                    sender.sendMessage("The Dragon Loot chest has been removed!");
                    Bukkit.getLogger().info("The Dragon Loot chest has been removed!");
                    return true;
                } catch (NullPointerException npe) { return true; }
            }

            else if (args[0].equalsIgnoreCase("later")) {
                Plugin plugin = GenesisSMP.getInstance();
                sender.sendMessage("The Dragon Loot chest will be removed in 3 minutes!");
                Bukkit.getLogger().info("The Dragon Loot chest will be removed in 3 minutes!");
                BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bukkit.getServer().getWorld("world_the_end").getBlockAt(x, y, z).setType(Material.AIR);
                        } catch (NullPointerException npe) { return; }
                    }
                }, delay);
                return true;
            }
        }
        return false;
    }

}
