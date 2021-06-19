package eu.genesismc.genesissmp.commands;

import eu.genesismc.genesissmp.GenesisSMP;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
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

public class SuffixCommand implements CommandExecutor, TabCompleter, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;
        User updatePlayer = GenesisSMP.getPlugin().api.getPlayerAdapter(Player.class).getUser(player);

        if (cmd.getName().equalsIgnoreCase("suffix")) {

            String pluginPrefix = GenesisSMP.getPlugin().pluginPrefix;
            FileConfiguration config = GenesisSMP.getInstance().config;

            if (args.length < 1) {
                sender.sendMessage(pluginPrefix + ChatColor.RED + "Usage: /suffix <confirm|remove|set <suffix>>");
                sender.sendMessage(pluginPrefix + "Suffixes must be between " + config.getInt("min-suffix-length") + " and " + config.getInt("max-suffix-length") + " characters (excluding colors)");
                sender.sendMessage(pluginPrefix + "You can use spaces, colours, #rgb, bold, underline, italic and strike.");
                return true;
            }

            if (args[0].equals("set")) {

                if (args.length < 2) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Incorrect usage: /suffix set <suffix>");
                    return true;
                }
                if (!player.hasPermission("genesisprefix.donator.suffix") && !player.hasPermission("essentials.kits.legend")) {
                    sender.sendMessage(
                            pluginPrefix + "You need to purchase a suffix token before you can set a custom suffix. " +
                                    "Tokens are available at " + ChatColor.BLUE + "https://www.genesis-mc.net/shop");
                    return true;
                }

                String suffixInput = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
                String firstCheckSuffix = GenesisSMP.getUtils().initialCheck(player, suffixInput);
                String preparedSuffix = GenesisSMP.getUtils().prepareFix(firstCheckSuffix);

                if (GenesisSMP.getUtils().getLength(preparedSuffix) > config.getInt("suffixes.max-suffix-length")) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Suffix too long - can't contain more than " + GenesisSMP.getPlugin().getConfig().getInt("suffixes.max-suffix-length") + " characters.");
                    return true;
                }
                if (GenesisSMP.getUtils().getLength(preparedSuffix) < config.getInt("suffixes.min-suffix-length")) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Suffix too short - must contain at least " + GenesisSMP.getPlugin().getConfig().getInt("suffixes.min-suffix-length") + " characters.");
                    return true;
                }

                sender.sendMessage(pluginPrefix + "Your suffix will display as:");
                sender.sendMessage(pluginPrefix + preparedSuffix);
                sender.sendMessage(pluginPrefix + ChatColor.translateAlternateColorCodes('&',"Type &a&l/suffix confirm&e to set it."));
                GenesisSMP.getPlugin().waitingSuffix.put(player, preparedSuffix);

                return true;
            }

            if (args[0].equals("confirm")) {
                if (!GenesisSMP.getPlugin().waitingSuffix.containsKey(player)) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "You do not have a suffix to confirm. Use the SET command first.");
                    return true;
                }
                if (GenesisSMP.getPlugin().waitingSuffix.get(player).equals("_remove")) {
                    /*Predicate<Node> removeSuffix = NodeType.SUFFIX.predicate(n -> n.getPriority() == 1);
                    updatePlayer.data().clear(removeSuffix);*/
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + sender.getName() + " removesuffix 1");
                    GenesisSMP.getPlugin().waitingSuffix.remove(player);
                    sender.sendMessage(pluginPrefix + "Your suffix has been removed.");
                }
                else {
                    /*Predicate<Node> removeSuffix = NodeType.SUFFIX.predicate(n -> n.getPriority() == 1);
                    updatePlayer.data().clear(removeSuffix);
                    DataMutateResult result = updatePlayer.data().remove(Node.builder("genesisprefix.donator.suffix").build());
                    DataMutateResult result2 = updatePlayer.data().add(SuffixNode.builder(GenesisSMP.getPlugin().waitingSuffix.get(player), 1).build());*/
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + sender.getName() + " setsuffix 1 " + GenesisSMP.getPlugin().waitingSuffix.get(player));
                    GenesisSMP.getPlugin().waitingSuffix.remove(player);
                    sender.sendMessage(pluginPrefix + "Your suffix has been set!");
                }
                GenesisSMP.getPlugin().api.getUserManager().saveUser(updatePlayer);
                GenesisSMP.getPlugin().api.runUpdateTask();
                return true;
            }
            if (args[0].equals("remove")) {
                sender.sendMessage(pluginPrefix + ChatColor.translateAlternateColorCodes('&',"Your suffix will be &c&lREMOVED&e. Confirm with &c/suffix confirm&a if you wish to remove it permanently."));
                GenesisSMP.getPlugin().waitingSuffix.put(player, "_remove");
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("suffix")) {
            if (args.length == 1) {
                final List<String> commands = Arrays.asList("set", "remove", "confirm");
                return StringUtil.copyPartialMatches(args[0], commands, new ArrayList<>());
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
                final List<String> commands = Arrays.asList("<suffix>");
                return commands;
            }
            else {
                final List<String> commands = Arrays.asList("-");
                return commands;
            }
        }
        return null;
    }

}
