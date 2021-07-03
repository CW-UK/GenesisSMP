package eu.genesismc.genesissmp.commands;

import eu.genesismc.genesissmp.GenesisSMP;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClearCrate implements CommandExecutor, Listener, TabCompleter {

    static Plugin plugin = GenesisSMP.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearcrate")) {

            if (!sender.hasPermission("clearcrate.admin")) {
                return false;
            }

            FileConfiguration config = GenesisSMP.getPlugin().config;
            if (!config.getBoolean("EndLootCrate.enabled")) {
                sender.sendMessage(ChatColor.RED + "This module is not enabled.");
                return true;
            }

            long delay = config.getLong("EndLootCrate.delay");
            int x = config.getInt("EndLootCrate.x");
            int y = config.getInt("EndLootCrate.y");
            int z = config.getInt("EndLootCrate.z");

            if (args[0].equalsIgnoreCase("now")) {
                try {
                    Bukkit.getServer().getWorld("world_the_end").getBlockAt(x, y, z).setType(Material.AIR);
                    sender.sendMessage("The Dragon Loot chest has been removed!");
                    Bukkit.getLogger().info("The Dragon Loot chest has been removed!");
                    return true;
                } catch (NullPointerException npe) { return true; }
            }

            else if (args[0].equalsIgnoreCase("later")) {
                sender.sendMessage("The Dragon Loot chest will be removed in 3 minutes!");
                Bukkit.getLogger().info("The Dragon Loot chest will be removed in 3 minutes!");
                try {
                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
                        Bukkit.getServer().getWorld("world_the_end").getBlockAt(x, y, z).setType(Material.AIR);
                    }, delay);
                } catch (NullPointerException npe) {
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearcrate")) {
            if (args.length == 1) {
                final List<String> commands = Arrays.asList("now", "later");
                return StringUtil.copyPartialMatches(args[0], commands, new ArrayList<>());
            }
            return null;
        }
        return null;
    }

}
