package eu.genesismc.genesissmp.commands;

import eu.genesismc.genesissmp.GenesisSMP;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.function.Predicate;

public class PrefixCommand implements CommandExecutor, TabCompleter, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;
        User updatePlayer = GenesisSMP.getPlugin().api.getPlayerAdapter(Player.class).getUser(player);

        if (cmd.getName().equalsIgnoreCase("prefix")) {

            String pluginPrefix = GenesisSMP.getPlugin().pluginPrefix;
            FileConfiguration config = GenesisSMP.getInstance().config;

            if (args.length < 1) {
                sender.sendMessage(pluginPrefix + ChatColor.RED + "Usage: /prefix <confirm|remove|set <prefix>>");
                sender.sendMessage(pluginPrefix + "Prefixes must be between " + config.getInt("min-prefix-length") + " and " + config.getInt("max-prefix-length") + " characters (excluding colors)");
                sender.sendMessage(pluginPrefix + "You can use spaces, colours, #rgb, bold, underline, italic and strike.");
                return true;
            }

            if (args[0].equals("set")) {

                if (args.length < 2) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Incorrect usage: /prefix set <prefix>");
                    return true;
                }
                if (!player.hasPermission("genesisprefix.donator.prefix")) {
                    sender.sendMessage(
                            pluginPrefix + "You need to purchase a prefix token before you can set a custom prefix. " +
                            "Tokens are available at " + ChatColor.BLUE + "https://www.genesis-mc.net/shop");
                    return true;
                }

                String prefixInput = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
                String firstCheckPrefix = GenesisSMP.getUtils().initialCheck(player, prefixInput);
                String preparedPrefix = GenesisSMP.getUtils().prepareFix(firstCheckPrefix);

                if (GenesisSMP.getUtils().getLength(preparedPrefix) > config.getInt("prefixes.max-prefix-length")) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Prefix too long - can't contain more than " + GenesisSMP.getPlugin().getConfig().getInt("prefixes.max-prefix-length") + " characters.");
                    return true;
                }
                if (GenesisSMP.getUtils().getLength(preparedPrefix) < config.getInt("prefixes.min-prefix-length")) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Prefix too short - must contain at least " + GenesisSMP.getPlugin().getConfig().getInt("prefixes.min-prefix-length") + " characters.");
                    return true;
                }

                sender.sendMessage(pluginPrefix + "Your prefix will display as:");
                sender.sendMessage(pluginPrefix + preparedPrefix);
                sender.sendMessage(pluginPrefix + ChatColor.translateAlternateColorCodes('&',"Type &a&l/prefix confirm&e to set it."));
                GenesisSMP.getPlugin().waitingPrefix.put(player, preparedPrefix);

                return true;
            }

            if (args[0].equals("confirm")) {
                if (!GenesisSMP.getPlugin().waitingPrefix.containsKey(player)) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "You do not have a prefix to confirm. Use the SET command first.");
                    return true;
                }
                if (GenesisSMP.getPlugin().waitingPrefix.get(player).equals("_remove")) {
                    Predicate<Node> removePrefix = NodeType.PREFIX.predicate(n -> n.getPriority() == 61);
                    updatePlayer.data().clear(removePrefix);
                    GenesisSMP.getPlugin().waitingPrefix.remove(player);
                    sender.sendMessage(pluginPrefix + "Your prefix has been removed.");
                }
                else {
                    Predicate<Node> removePrefix = NodeType.PREFIX.predicate(n -> n.getPriority() == 61);
                    updatePlayer.data().clear(removePrefix);
                    DataMutateResult result = updatePlayer.data().remove(Node.builder("genesisprefix.donator.prefix").build());
                    DataMutateResult result2 = updatePlayer.data().add(PrefixNode.builder(GenesisSMP.getPlugin().waitingPrefix.get(player), 61).build());
                    GenesisSMP.getPlugin().waitingPrefix.remove(player);
                    sender.sendMessage(pluginPrefix + "Your prefix has been set!");
                }
                GenesisSMP.getPlugin().api.getUserManager().saveUser(updatePlayer);
                GenesisSMP.getPlugin().api.runUpdateTask();
                return true;
            }
            if (args[0].equals("remove")) {
                sender.sendMessage(pluginPrefix + ChatColor.translateAlternateColorCodes('&',"Your prefix will be &c&lREMOVED&e. Confirm with &c/prefix confirm&a if you wish to remove it permanently."));
                GenesisSMP.getPlugin().waitingPrefix.put(player, "_remove");
                return true;
            }

            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("prefix")) {
            if (args.length == 1) {
                final List<String> commands = Arrays.asList("set", "remove", "confirm");
                return StringUtil.copyPartialMatches(args[0], commands, new ArrayList<>());
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
                final List<String> commands = Arrays.asList("<prefix>");
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
