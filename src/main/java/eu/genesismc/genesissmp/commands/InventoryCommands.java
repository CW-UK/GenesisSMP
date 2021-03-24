package eu.genesismc.genesissmp.commands;

/*
import eu.genesismc.genesissmp.GenesisSMP;
import eu.genesismc.genesissmp.Utils;
import eu.genesismc.genesissmp.managers.InventoryManager;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryCommands implements CommandExecutor, Listener, TabCompleter {

    InventoryManager invManager = new InventoryManager();
    String pluginPrefix = ChatColor.translateAlternateColorCodes('&', "&6&lGenesisPlots > &e");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("plot")) {

            Plugin plugin = GenesisSMP.getPlugin();
            Utils utils = GenesisSMP.getUtils();
            FileConfiguration config = plugin.getConfig();

            if (args.length > 1) {

                // ****************************
                //       Store inventory
                // ****************************
                if (args[0].equals("store")) {

                    Player p = utils.getPlayer(args[1]);
                    if (p == null) {
                        sender.sendMessage(pluginPrefix + "Player " + args[1] + " is not online.");
                        return true;
                    }

                    try {
                        invManager.saveInventory(p);
                        p.sendMessage(pluginPrefix + "Your survival inventory has been stored.");
                        return true;
                    } catch (IOException e) {
                        p.sendMessage(pluginPrefix + "Your survival inventory could not be stored. Please inform a member of staff of this error.");
                        e.printStackTrace();
                    }

                    plugin.saveConfig();
                }

                // ****************************
                //     Add player to plot
                // ****************************
                if (args[0].equals("enter")) {

                    Player p = (Player) sender;
                    int plot = Integer.parseInt(args[1]);
                    if (config.getString("plots.plot" + plot) != null) {
                        String owner = config.getString("plots.plot" + plot + ".member");
                        if (owner.equalsIgnoreCase(p.getName())) {
                            p.sendMessage(pluginPrefix + "You already have access to that plot.");
                        }
                        else {
                            p.sendMessage(pluginPrefix + "That plot is in use by " + owner + ". Choose another one!");
                        }
                        return true;
                    }

                    try {
                        invManager.saveInventory(p);
                        p.sendMessage(pluginPrefix + "Your survival inventory has been stored safely. It will be returned to you when you leave the plot.");
                        p.getInventory().clear();
                        config.set("plots.plot" + plot + ".member", p.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg addmember -w flatworld Plot" + plot + "Inner " + p.getName());
                        p.sendMessage(pluginPrefix + "Permission granted - you may now access Plot " + plot + " to build.");
                        plugin.saveConfig();
                        return true;
                    } catch (IOException e) {
                        p.sendMessage(pluginPrefix + "Your survival inventory could not be stored. Please inform a member of staff of this error.");
                        e.printStackTrace();
                    }
                }

                // ****************************
                //      Restore Inventory
                // ****************************
                if (args[0].equals("restore")) {

                    Player p = utils.getPlayer(args[1]);
                    if (p == null) {
                        sender.sendMessage(pluginPrefix + "Player " + args[1] + " is not online.");
                        return true;
                    }

                    try {
                        invManager.restoreInventory(p);
                        p.sendMessage(pluginPrefix + "Your survival inventory has been restored.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException npe) {
                        p.sendMessage(pluginPrefix + "You have no inventory to restore.");
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("plot")) {
            if (args.length == 1) {
                final List<String> commands = Arrays.asList("store", "restore", "enter", "leave", "clear");
                return StringUtil.copyPartialMatches(args[0], commands, new ArrayList<>());
            }
            return null;
        }
        return null;
    }



}

 */
