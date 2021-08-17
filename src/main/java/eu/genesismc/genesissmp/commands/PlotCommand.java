package eu.genesismc.genesissmp.commands;

import eu.genesismc.genesissmp.GenesisSMP;
import eu.genesismc.genesissmp.managers.InventoryManager;
import eu.genesismc.genesissmp.managers.PlotManager;
import eu.genesismc.genesissmp.managers.WorldGuardManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlotCommand implements CommandExecutor, Listener, TabCompleter {

    InventoryManager invManager = GenesisSMP.getInventoryManager();
    PlotManager plotManager = GenesisSMP.getPlotManager();

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
            //   EXPIRE PLOT
            // ****************
            if (args[0].equalsIgnoreCase("expire") && (sender.hasPermission("genesissmp.plots.expire") || sender.isOp())) {
                if (args.length < 2) {
                    sender.sendMessage(pluginPrefix + "Please specify a plot with /plot expire <plot>");
                    return true;
                }

                int plot = Integer.parseInt(args[1]);
                plotManager.expirePlot(plot);
                sender.sendMessage(pluginPrefix + "Plot " + args[1] + " has been expired.");
                GenesisSMP.getUtils().addLogEntry(sender.getName() + " expired plot " + args[1]);
                return true;

            }

            // ****************
            //    ENTER PLOT
            // ****************
            if (args[0].equalsIgnoreCase("enter")) {

                if (!WorldGuardManager.getInstance().regionIsCreativeArena(player)) {
                    sender.sendMessage(pluginPrefix + "You must be in a creative plot to use this command.");
                    return true;
                }

                if (plotManager.hasAssignedPlot(player)) {
                    if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                        try {
                            invManager.saveInventory(player);
                            player.sendMessage(pluginPrefix + "Your survival inventory has been saved.");
                            player.getInventory().clear();
                            player.setGameMode(GameMode.CREATIVE);
                            GenesisSMP.getUtils().addLogEntry(player.getName() + " had their inventory saved to re-enter their assigned plot " + args[1]);
                            return true;
                        } catch (IOException e) {
                            player.sendMessage(pluginPrefix + "Your survival inventory could not be stored. Please inform a member of staff of this error.");
                            sender.sendMessage(pluginPrefix + player.getName() + "'s survival inventory could not be saved! (IO Error)");
                            e.printStackTrace();
                            return true;
                        }
                    }
                    sender.sendMessage(pluginPrefix + "You have already been assigned Plot " + plotManager.getAssignedPlot(player));
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

                    if (plotManager.plotInUse(plotNumber)) {
                        sender.sendMessage(pluginPrefix + "This plot is currently in use by another player.");
                        return true;
                    }

                    if (plotManager.plotIsLocked(plotNumber)) {
                        sender.sendMessage(pluginPrefix + "This plot has been locked by an administrator.");
                        return true;
                    }

                    if (plotManager.allPlotsLocked()) {
                        sender.sendMessage(pluginPrefix + "All plots are currently locked by an administrator.");
                        return true;
                    }

                    sender.sendMessage(pluginPrefix + "You have claimed plot " + plotNumber);

                    // Save inventory
                    try {
                        invManager.saveInventory(player);
                        player.sendMessage(pluginPrefix + "Your survival inventory has been saved.");
                        //sender.sendMessage(pluginPrefix + player.getName() + "'s survival inventory has been saved.");
                        player.getInventory().clear();
                        player.setGameMode(GameMode.CREATIVE);
                        GenesisSMP.getUtils().addLogEntry(player.getName() + " had their inventory saved upon claiming plot " + args[1]);
                    } catch (IOException e) {
                        player.sendMessage(pluginPrefix + "Your survival inventory could not be stored. Please inform a member of staff of this error.");
                        sender.sendMessage(pluginPrefix + player.getName() + "'s survival inventory could not be saved! (IO Error)");
                        e.printStackTrace();
                        return true;
                    }
                    // Add player as member of plot
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg addmember -w smphub creative_plot" + plotNumber + " " + playerName);
                    // Add player to config for the plot
                    plotManager.assignPlotToPlayer(plotNumber, player);
                    GenesisSMP.getUtils().addLogEntry(player.getName() + " claimed plot " + plotNumber);
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

                if (!plotManager.hasAssignedPlot(player)) {
                    sender.sendMessage(pluginPrefix + "You do not have a plot assigned.");
                    return true;
                }

                int plot = plotManager.getAssignedPlot(player);
                // Remove player from plot members
                player.setGameMode(GameMode.SURVIVAL);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg removemember -w smphub creative_plot" + plot + " " + playerName);
                // Remove player from config for the plot
                plotManager.unassignPlotFromPlayer(plot, player.getName());
                plotManager.plotSignExpired(plot);

                // Restore inventory
                try {
                    invManager.restoreInventory(player);
                    //player.sendMessage(pluginPrefix + "Your survival inventory has been restored.");
                    GenesisSMP.getUtils().addLogEntry(player.getName() + " had their inventory restored after leaving plot " + plot);
                } catch (IOException e) {
                    player.sendMessage(pluginPrefix + "Your survival inventory could not be restored. Please inform a member of staff of this error.");
                    return true;
                }

                sender.sendMessage(pluginPrefix + ChatColor.GREEN + "You have been successfully removed from Plot " + plot);
                GenesisSMP.getUtils().addLogEntry(player.getName() + " left plot " + plot);

                return true;
            }

            // ****************
            //    CLEAR PLOT
            // ****************
            if (args[0].equalsIgnoreCase("clear")) {

                if (sender.hasPermission("genesissmp.plots.clear") || sender.isOp()) {
                    if (args.length < 2) {
                        sender.sendMessage(pluginPrefix + "Please specify a plot with /plot clear <plot>");
                        return true;
                    }
                    else {
                        try {
                            int plot = Integer.parseInt(args[1]);
                            sender.sendMessage(pluginPrefix + "Clearing the area at Plot " + plot + " for you.");
                            plotManager.clearPlot(plot);
                            GenesisSMP.getUtils().addLogEntry(player.getName() + " forced plot " + plot + " to be cleared");
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (!plotManager.hasAssignedPlot(player)) {
                    sender.sendMessage(pluginPrefix + "You need to have an assigned plot before you can clear it.");
                    return true;
                }
                try {
                    int plot = plotManager.getAssignedPlot(player);
                    sender.sendMessage(pluginPrefix + "Clearing the area at Plot " + plot + " for you.");
                    plotManager.clearPlot(plot);
                    GenesisSMP.getUtils().addLogEntry(player.getName() + " cleared plot " + plot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            // *****************
            //   FLOOR COMMAND
            // *****************
            if (args[0].equalsIgnoreCase("floor")) {
                if (!plotManager.hasAssignedPlot(player)) {
                    sender.sendMessage(pluginPrefix + "You need to have an assigned plot before you can change it.");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(pluginPrefix + "You need to specify a floor type: /plot floor <floor>");
                    return true;
                }
                try {
                    int plot = plotManager.getAssignedPlot(player);
                    sender.sendMessage(pluginPrefix + "Changing the floor of Plot " + plot + " to " + args[1]);
                    plotManager.plotFloor(plot, args[1]);
                    GenesisSMP.getUtils().addLogEntry(player.getName() + " changed the floor of plot " + plot + " to " + args[1]);
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
                    sender.sendMessage(pluginPrefix + "You need to specify a plot number: /plot lock <x|all>");
                    return true;
                }

                if (plotManager.allPlotsLocked()) {
                    sender.sendMessage(pluginPrefix + "All plots are currently locked.");
                    return true;
                }

                if (args[1].equalsIgnoreCase("all")) {
                    if (plotManager.lockAllPlots()) {
                        sender.sendMessage(pluginPrefix + "All unclaimed plots have been locked.");
                        GenesisSMP.getUtils().addLogEntry(player.getName() + " locked all plots");
                        return true;
                    }
                    sender.sendMessage(pluginPrefix + "Something went wrong trying to lock all unclaimed plots.");
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
                        GenesisSMP.getUtils().addLogEntry(player.getName() + " locked plot " + plotNumber);
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
                    sender.sendMessage(pluginPrefix + "You need to specify a plot number: /plot unlock <x|all>");
                    return true;
                }

                if (args[1].equalsIgnoreCase("all")) {
                    if (plotManager.unlockAllPlots()) {
                        sender.sendMessage(pluginPrefix + "All unclaimed plots have been unlocked.");
                        GenesisSMP.getUtils().addLogEntry(player.getName() + " unlocked all plots");
                        return true;
                    }
                    sender.sendMessage(pluginPrefix + "Something went wrong trying to unlock all unclaimed plots.");
                    return true;
                }

                try {

                    int plotNumber = Integer.parseInt(args[1]);
                    if (plotNumber > 8 || plotNumber < 1) {
                        sender.sendMessage(pluginPrefix + "You need to specify a plot between 1 and 8 inclusive.");
                        return true;
                    }

                    if (plotManager.allPlotsLocked()) {
                        sender.sendMessage(pluginPrefix + "You cannot unlock individual plots while all plots are locked.");
                        return true;
                    }

                    if (!plotManager.plotIsLocked(plotNumber)) {
                        sender.sendMessage(pluginPrefix + "Plot " + plotNumber + " is not locked.");
                        return true;
                    }

                    if (plotManager.unlockPlot(plotNumber)) {
                        sender.sendMessage(pluginPrefix + "Plot " + plotNumber + " has been unlocked.");
                        GenesisSMP.getUtils().addLogEntry(player.getName() + " unlocked plot " + plotNumber);
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
                List<String> commands = Arrays.asList(
                        "clear",
                        "enter",
                        "floor",
                        "leave"
                );
                if (commandSender.isOp() || commandSender.hasPermission("genesissmp.plots.lock")) {
                    commands = Arrays.asList(
                            "clear",
                            "enter",
                            "floor",
                            "leave",
                            "lock",
                            "unlock"
                    );
                }
                return StringUtil.copyPartialMatches(args[0], commands, new ArrayList<>());
            }
            if (args.length == 2) {
                final List<String> commands;
                if (!args[0].equalsIgnoreCase("floor") && !args[0].equalsIgnoreCase("leave")) {
                    if (args[0].equalsIgnoreCase("lock") || args[1].equalsIgnoreCase("unlock")) {
                        commands = Arrays.asList("all","1", "2", "3", "4", "5", "6", "7", "8");
                    }
                    else {
                        commands = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
                    }
                }
                else {
                    commands = GenesisSMP.getPlugin().getConfig().getStringList("Plots.Floors");
                }
                return StringUtil.copyPartialMatches(args[1], commands, new ArrayList<>());
            }
            return null;
        }
        return null;
    }

    @EventHandler
    public void playerLogin(PlayerJoinEvent e) {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        String pluginPrefix = GenesisSMP.getPlugin().pluginPrefix;
        Bukkit.getLogger().info("CONNECTION FROM " + e.getPlayer().getName());
        Player p = e.getPlayer();
        if (config.getBoolean("Plots.RestoreNextLogin." + p.getName())) {
            p.setGameMode(GameMode.SURVIVAL);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GenesisSMP.getPlugin(), () -> {
                p.sendMessage(pluginPrefix + "Your creative plot expired while you were offline.");
                try {
                    GenesisSMP.getInventoryManager().restoreInventory(p);
                    config.set("Plots.RestoreNextLogin." + p.getName(), null);
                    GenesisSMP.getUtils().addLogEntry(p.getName() + " had their inventory restored as their plot expired while they were offline");
                    //p.sendMessage(pluginPrefix + "Your survival inventory has been restored.");
                } catch (Exception ex) {
                    Bukkit.getLogger().info("Could not restore inventory of " + p.getName());
                }
            }, 60L);
        }
    }

}
