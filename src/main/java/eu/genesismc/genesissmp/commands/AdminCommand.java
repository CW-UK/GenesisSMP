package eu.genesismc.genesissmp.commands;

import eu.genesismc.genesissmp.GenesisSMP;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminCommand implements CommandExecutor, TabCompleter, Listener {

    private void reloadConfig() {
        GenesisSMP.getPlugin().saveConfig();
        GenesisSMP.getPlugin().reloadConfig();
        GenesisSMP.getPlugin().config = GenesisSMP.getPlugin().getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("gsmp")) {

            String pluginPrefix = GenesisSMP.getPlugin().pluginPrefix;
            FileConfiguration config = GenesisSMP.getPlugin().getConfig();

            if (args[0].equals("reload") && sender.isOp()) {
                reloadConfig();
                config = GenesisSMP.getPlugin().getConfig();
                sender.sendMessage(pluginPrefix + ChatColor.GREEN + "Configuration reloaded!");
                sender.sendMessage(pluginPrefix + ChatColor.YELLOW + "Prefix: Min " + config.getString("prefixes.min-prefix-length") + " / Max " + config.getString("prefixes.max-prefix-length"));
                sender.sendMessage(pluginPrefix + ChatColor.YELLOW + "Suffix: Min " + config.getString("suffixes.min-suffix-length") + " / Max " + config.getString("suffixes.max-suffix-length"));
                sender.sendMessage(pluginPrefix + ChatColor.YELLOW + "BanAnnounce Settings:");
                sender.sendMessage(pluginPrefix + ChatColor.YELLOW + "Strikes Enabled: " + org.bukkit.ChatColor.WHITE + config.getBoolean("BanAnnounce.strike"));
                sender.sendMessage(pluginPrefix + ChatColor.YELLOW + "Strike Quantity: " + org.bukkit.ChatColor.WHITE + config.getInt("BanAnnounce.strike-amount"));
                return true;
            }
            if (args[0].equals("setendspawn") && sender.isOp()) {
                Player p = (Player) sender;
                double x = p.getLocation().getX();
                double y = p.getLocation().getY();
                double z = p.getLocation().getZ();
                double yaw = p.getLocation().getYaw();
                double pitch = p.getLocation().getPitch();
                config.set("EndSpawnPoint.x", x);
                config.set("EndSpawnPoint.y", y);
                config.set("EndSpawnPoint.z", z);
                config.set("EndSpawnPoint.yaw", yaw);
                config.set("EndSpawnPoint.pitch", pitch);
                reloadConfig();
                sender.sendMessage(pluginPrefix + ChatColor.GREEN + "Spawn point for The End has been changed.");
                return true;
            }
            if (args[0].equals("setplotcenter") && sender.isOp()) {
                if (args.length < 2) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "You need to specify a plot number!");
                    return true;
                }
                Player p = (Player) sender;
                int plot = Integer.parseInt(args[1]);
                int x = (int) p.getLocation().getX();
                int y = (int) p.getLocation().getY();
                int z = (int) p.getLocation().getZ();
                config.set("Plots.Plot"+plot+".CenterX", x);
                config.set("Plots.Plot"+plot+".CenterY", y);
                config.set("Plots.Plot"+plot+".CenterZ", z);
                reloadConfig();
                sender.sendMessage(pluginPrefix + ChatColor.GREEN + "Plot " + plot + " has been updated with new coordinates.");
                return true;
            }
            if (args[0].equals("setplotsign") && sender.isOp()) {
                if (args.length < 2) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "You need to specify a plot number!");
                    return true;
                }
                Player p = (Player) sender;
                Block signBlock = p.getTargetBlock(null, 10);
                if (!(signBlock instanceof Sign)) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "You need to look at a sign before setting.");
                    return true;
                }
                int plot = Integer.parseInt(args[1]);
                int x = (int) signBlock.getLocation().getX();
                int y = (int) signBlock.getLocation().getY();
                int z = (int) signBlock.getLocation().getZ();
                config.set("Plots.Plot"+plot+".SignX", x);
                config.set("Plots.Plot"+plot+".SignY", y);
                config.set("Plots.Plot"+plot+".SignZ", z);
                reloadConfig();
                sender.sendMessage(pluginPrefix + ChatColor.GREEN + "Plot " + plot + "'s sign has been updated with new coordinates.");
                return true;
            }
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gsmp")) {
            if (args.length == 1) {
                final List<String> commands = Arrays.asList("reload", "setendspawn", "setplotcenter", "setplotsign");
                return StringUtil.copyPartialMatches(args[0], commands, new ArrayList<>());
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("setplotcenter") || args[0].equalsIgnoreCase("setplotsign")) {
                    final List<String> commands = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
                    return StringUtil.copyPartialMatches(args[1], commands, new ArrayList<>());
                }
                return null;
            }
            return null;
        }
        return null;
    }

}