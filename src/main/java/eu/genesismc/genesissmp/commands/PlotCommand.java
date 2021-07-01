package eu.genesismc.genesissmp.commands;

import eu.genesismc.genesissmp.GenesisSMP;
import eu.genesismc.genesissmp.managers.InventoryManager;
import eu.genesismc.genesissmp.managers.PlotManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlotCommand implements CommandExecutor, Listener, TabCompleter {

    InventoryManager invManager = new InventoryManager();
    PlotManager plotManager = new PlotManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("plot")) {

            String pluginPrefix = GenesisSMP.getPlugin().pluginPrefix;
            Player player = (Player) sender;
            String playerName = player.getName();

            if (args.length < 1) {
                sender.sendMessage(pluginPrefix + ChatColor.RED + "Usage: /plot <enter [number]|leave>");
                return true;
            }

            // ****************
            //    ENTER PLOT
            // ****************
            if (args[0].equalsIgnoreCase("enter")) {

                if (plotManager.hasAssignedPlot(player.getUniqueId())) {
                    sender.sendMessage(pluginPrefix + "You have already been assigned Plot " + plotManager.getAssignedPlot(player.getUniqueId()));
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage(pluginPrefix + "You need to specify a plot number: /plot enter <x>");
                    return true;
                }

                try {
                    int plotNumber = Integer.parseInt(args[1]);
                    if (plotNumber > 8 || plotNumber < 1) {
                        sender.sendMessage(pluginPrefix + "You need to specify a plot between 1 and 8 inclusive.");
                        return true;
                    }

                    if (plotManager.plotIsLocked(plotNumber)) {
                        sender.sendMessage(pluginPrefix + "This plot has been locked by an administrator.");
                        return true;
                    }

                    sender.sendMessage(pluginPrefix + "Success! Please wait while we assign this plot to you..");

                    // Save inventory
                    try {
                        invManager.saveInventory(player);
                        player.sendMessage(pluginPrefix + "Your survival inventory has been saved.");
                        //sender.sendMessage(pluginPrefix + player.getName() + "'s survival inventory has been saved.");
                        player.getInventory().clear();
                    } catch (IOException e) {
                        player.sendMessage(pluginPrefix + "Your survival inventory could not be stored. Please inform a member of staff of this error.");
                        sender.sendMessage(pluginPrefix + player.getName() + "'s survival inventory could not be saved! (IO Error)");
                        e.printStackTrace();
                        return true;
                    }
                    // Add player as member of plot
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg addmember -w smphub plot" + plotNumber + " " + playerName);
                    // Add player to config for the plot
                    plotManager.assignPlotToPlayer(plotNumber, player);
                    plotManager.plotSignClaimed(player, plotNumber);

                } catch (NumberFormatException e) {
                    sender.sendMessage(pluginPrefix + "You need to specify a plot number: /plot enter <x>");
                    return true;
                }

                return true;
            }

            // ****************
            //    LEAVE PLOT
            // ****************
            if (args[0].equalsIgnoreCase("leave")) {

                if (!plotManager.hasAssignedPlot(player.getUniqueId())) {
                    sender.sendMessage(pluginPrefix + "You do not have a plot assigned.");
                    return true;
                }

                int plot = plotManager.getAssignedPlot(player.getUniqueId());
                // Remove player from plot members
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg removemember -w smphub plot" + plot + " " + playerName);
                // Remove player from config for the plot
                plotManager.unassignPlotFromPlayer(plot, player);
                plotManager.plotSignClaimed(player, plot);
                plotManager.plotSignExpired(plot);

                // Restore inventory
                try {
                    invManager.restoreInventory(player);
                    player.sendMessage(pluginPrefix + "Your survival inventory has been restored.");
                    //sender.sendMessage(pluginPrefix + player.getName() + "'s survival inventory has been saved.");
                } catch (IOException e) {
                    player.sendMessage(pluginPrefix + "Your survival inventory could not be restored. Please inform a member of staff of this error.");
                    sender.sendMessage(pluginPrefix + player.getName() + "'s survival inventory could not be restored! (IO Error)");
                    e.printStackTrace();
                }

                sender.sendMessage(pluginPrefix + ChatColor.GREEN + "You have been successfully removed from Plot " + plot);

                return true;
            }

            // ****************
            //    CLEAR PLOT
            // ****************
            if (args[0].equalsIgnoreCase("clear")) {
                if (!plotManager.hasAssignedPlot(player.getUniqueId())) {
                    sender.sendMessage(pluginPrefix + "You need to have an assigned plot before you can clear it.");
                    return true;
                }
                try {
                    int plot = plotManager.getAssignedPlot(player.getUniqueId());
                    sender.sendMessage(pluginPrefix + "Clearing the area at Plot " + plot + " for you.");
                    plotManager.clearPlot(plot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            // *****************
            //   FLOOR COMMAND
            // *****************
            if (args[0].equalsIgnoreCase("floor")) {
                if (!plotManager.hasAssignedPlot(player.getUniqueId())) {
                    sender.sendMessage(pluginPrefix + "You need to have an assigned plot before you can change it.");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(pluginPrefix + "You need to specify a floor type: /plot floor <floor>");
                    return true;
                }
                try {
                    int plot = plotManager.getAssignedPlot(player.getUniqueId());
                    sender.sendMessage(pluginPrefix + "Changing the floor of Plot " + plot + " to " + args[1]);
                    plotManager.plotFloor(plot, args[1]);
                } catch (IOException e) {
                    sender.sendMessage(pluginPrefix + "Invalid floor option.");
                }
                return true;
            }

            // ****************
            //    LOCK PLOT
            // ****************
            if (args[0].equalsIgnoreCase("lock")) {

                if (!sender.isOp() && !sender.hasPermission("genesissmp.plots.lock")) {
                    sender.sendMessage(pluginPrefix + "You do not have permission to do this.");
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage(pluginPrefix + "You need to specify a plot number: /plot lock <x>");
                    return true;
                }

                if (plotManager.allPlotsLocked()) {
                    sender.sendMessage(pluginPrefix + "All plots are currently locked.");
                    return true;
                }

                try {

                    int plotNumber = Integer.parseInt(args[1]);
                    if (plotNumber > 8 || plotNumber < 1) {
                        sender.sendMessage(pluginPrefix + "You need to specify a plot between 1 and 8 inclusive.");
                        return true;
                    }
                    if (plotManager.plotIsLocked(plotNumber)) {
                        sender.sendMessage(pluginPrefix + "Plot " + plotNumber + " is already locked.");
                        return true;
                    }

                    if (plotManager.lockPlot(plotNumber)) {
                        sender.sendMessage(pluginPrefix + "Plot " + plotNumber + " has been locked.");
                        return true;
                    }

                } catch (NumberFormatException e) {
                    sender.sendMessage(pluginPrefix + "You need to specify a plot number: /plot lock <x>");
                    return true;
                }

                return true;
            }

            // ******************
            //    UNLOCK PLOT
            // ******************
            if (args[0].equalsIgnoreCase("unlock")) {

                if (!sender.isOp() && !sender.hasPermission("genesissmp.plots.lock")) {
                    sender.sendMessage(pluginPrefix + "You do not have permission to do this.");
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage(pluginPrefix + "You need to specify a plot number: /plot unlock <x>");
                    return true;
                }

                if (plotManager.allPlotsLocked()) {
                    sender.sendMessage(pluginPrefix + "All plots are currently locked.");
                    return true;
                }

                try {

                    int plotNumber = Integer.parseInt(args[1]);
                    if (plotNumber > 8 || plotNumber < 1) {
                        sender.sendMessage(pluginPrefix + "You need to specify a plot between 1 and 8 inclusive.");
                        return true;
                    }

                    if (!plotManager.plotIsLocked(plotNumber)) {
                        sender.sendMessage(pluginPrefix + "Plot " + plotNumber + " is not locked.");
                        return true;
                    }

                    if (plotManager.unlockPlot(plotNumber)) {
                        sender.sendMessage(pluginPrefix + "Plot " + plotNumber + " has been unlocked.");
                        return true;
                    }

                } catch (NumberFormatException e) {
                    sender.sendMessage(pluginPrefix + "You need to specify a plot number: /plot unlock <x>");
                    return true;
                }

                return true;
            }

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("plot")) {
            if (args.length == 1) {
                final List<String> commands = Arrays.asList(
                        "clear",
                        "enter",
                        "floor",
                        "leave",
                        "lock",
                        "unlock"
                );
                return StringUtil.copyPartialMatches(args[0], commands, new ArrayList<>());
            }
            if (args.length == 2) {
                final List<String> commands;
                if (!args[0].equalsIgnoreCase("floor")) {
                    commands = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
                }
                else {
                    commands = Arrays.asList(
                            "Copper_Block",
                            "Crimson_Nylium",
                            "Deepslate",
                            "Deepslate_Bricks",
                            "Deepslate_Tiles",
                            "Dripstone_Block",
                            "Flowering_Azalea",
                            "Grass_Block",
                            "Lava",
                            "Moss_Block",
                            "Netherrack",
                            "Polished_Deepslate",
                            "Rooted_Dirt",
                            "Sand",
                            "Snow_Block",
                            "Stone",
                            "Tuff",
                            "Warped_Nylium",
                            "Water"
                    );
                }
                return StringUtil.copyPartialMatches(args[1], commands, new ArrayList<>());
            }
            return null;
        }
        return null;
    }


}

