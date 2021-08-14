package eu.genesismc.genesissmp.commands;


import eu.genesismc.genesissmp.GenesisSMP;
import eu.genesismc.genesissmp.Utils;
import eu.genesismc.genesissmp.managers.InventoryManager;
import eu.genesismc.genesissmp.managers.PlotManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryCommands implements CommandExecutor, Listener, TabCompleter {

    InventoryManager invManager = GenesisSMP.getInventoryManager();
    PlotManager plotManager = GenesisSMP.getPlotManager();
    String pluginPrefix = GenesisSMP.getPlugin().pluginPrefix;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("invman")) {

            if (!sender.isOp() && !(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage(pluginPrefix + "You do not have access to this command.");
                return true;
            }

            if (args.length > 1) {

                Utils utils = GenesisSMP.getUtils();

                // ****************************
                //        Save Inventory
                // ****************************
                if (args[0].equalsIgnoreCase("save")) {

                    Player p = utils.getPlayer(args[1]);
                    if (p == null) {
                        sender.sendMessage(pluginPrefix + "Player " + args[1] + " is not online.");
                        return true;
                    }

                    try {
                        invManager.saveInventory(p);
                        p.sendMessage(pluginPrefix + "Your survival inventory has been saved.");
                        sender.sendMessage(pluginPrefix + p.getName() + "'s survival inventory has been saved.");
                        p.getInventory().clear();
                        return true;
                    } catch (IOException e) {
                        p.sendMessage(pluginPrefix + "Your survival inventory could not be stored. Please inform a member of staff of this error.");
                        sender.sendMessage(pluginPrefix + p.getName() + "'s survival inventory could not be saved! (IO Error)");
                        e.printStackTrace();
                        return true;
                    }

                }

                // ****************************
                //      Restore Inventory
                // ****************************
                if (args[0].equalsIgnoreCase("restore")) {

                    Player p = utils.getPlayer(args[1]);
                    if (p == null) {
                        sender.sendMessage(pluginPrefix + "Player " + args[1] + " is not online.");
                        return true;
                    }

                    try {
                        invManager.restoreInventory(p);
                        sender.sendMessage(pluginPrefix + p.getName() + "'s survival inventory has been restored.");
                    } catch (IOException e) {
                        p.sendMessage(pluginPrefix + "An error occurred restoring your inventory. Please inform a member of staff.");
                        sender.sendMessage(pluginPrefix + p.getName() + "'s survival inventory could not be restored! (IO Error)");
                        e.printStackTrace();
                    } catch (NullPointerException npe) {
                        p.sendMessage(pluginPrefix + "You have no inventory to restore.");
                        sender.sendMessage(pluginPrefix + p.getName() + "does not have a survival inventory to restore.");
                    }
                    return true;
                }

                // ****************************
                //        Entering Plot
                // ****************************
                //             0       1     2
                // /invman plotenter plot username
                if (args[0].equalsIgnoreCase("plotenter")) {
                    Player player = Bukkit.getPlayer(args[2]);
                    if (plotManager.getAssignedPlot(player) == Integer.parseInt(args[1])) {
                        player.setGameMode(GameMode.CREATIVE);
                    }
                }

            }

            sender.sendMessage(pluginPrefix + "Usage: /invman <save|restore> <username>");
            return true;

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("invman") && commandSender.isOp()) {
            if (args.length == 1) {
                final List<String> commands = Arrays.asList("save", "restore");
                return StringUtil.copyPartialMatches(args[0], commands, new ArrayList<>());
            }
            return null;
        }
        return null;
    }

}
