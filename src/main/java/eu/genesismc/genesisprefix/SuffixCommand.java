package eu.genesismc.genesisprefix;

import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.SuffixNode;
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

public class SuffixCommand implements CommandExecutor, TabCompleter, Listener {

    private String pluginPrefix = GenesisPrefix.getPlugin().pluginPrefix;
    FileConfiguration config = GenesisPrefix.getPlugin().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;
        User updatePlayer = GenesisPrefix.getPlugin().api.getPlayerAdapter(Player.class).getUser(player);

        if (cmd.getName().equalsIgnoreCase("suffix")) {

            if (args.length < 1) {
                sender.sendMessage(pluginPrefix + ChatColor.RED + "Usage: /suffix <confirm|remove|set <suffix>>");
                sender.sendMessage(pluginPrefix + "Suffixes must be between " + config.getInt("min-suffix-length") + " and " + config.getInt("max-suffix-length") + " characters (excluding colors)");
                sender.sendMessage(pluginPrefix + "You can use spaces.");
                sender.sendMessage(pluginPrefix + "You can use colours, #rgb, bold, underline, italic and strike.");
                return true;
            }

            if (args[0].equals("set")) {

                if (args.length < 2) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Incorrect usage: /suffix set <suffix>");
                    return true;
                }
                if (!player.hasPermission("donator.suffix") && !player.hasPermission("essentials.kits.legend")) {
                    sender.sendMessage(pluginPrefix + "You need to purchase a suffix token before you can set a custom suffix.");
                    sender.sendMessage(pluginPrefix + "Tokens are available at " + ChatColor.BLUE + "https://www.genesis-mc.net/shop");
                    return true;
                }

                String suffixInput = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
                String firstCheckSuffix = GenesisPrefix.getUtils().initialCheck(player, suffixInput);
                String preparedSuffix = GenesisPrefix.getUtils().prepareFix(firstCheckSuffix);

                if (GenesisPrefix.getUtils().getLength(preparedSuffix) > config.getInt("max-suffix-length")) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Suffix too long - maximum allowed length is " + GenesisPrefix.getPlugin().getConfig().getInt("max-suffix-length"));
                    return true;
                }
                if (GenesisPrefix.getUtils().getLength(preparedSuffix) < config.getInt("min-suffix-length")) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Suffix too short - minimum length is " + GenesisPrefix.getPlugin().getConfig().getInt("min-suffix-length"));
                    return true;
                }

                sender.sendMessage(pluginPrefix + "Your suffix will display as:");
                sender.sendMessage(pluginPrefix + preparedSuffix);
                sender.sendMessage(pluginPrefix + ChatColor.translateAlternateColorCodes('&',"Type &a&l/suffix confirm&e to set it."));
                GenesisPrefix.getPlugin().waitingSuffix.put(player, preparedSuffix);

                return true;
            }

            if (args[0].equals("confirm")) {
                if (!GenesisPrefix.getPlugin().waitingSuffix.containsKey(player)) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "You do not have a suffix to confirm. Use the SET command first.");
                    return true;
                }
                if (GenesisPrefix.getPlugin().waitingSuffix.get(player).equals("_remove")) {
                    Predicate<Node> removeSuffix = NodeType.SUFFIX.predicate(n -> n.getPriority() == 1);
                    updatePlayer.data().clear(removeSuffix);
                    GenesisPrefix.getPlugin().waitingSuffix.remove(player);
                    sender.sendMessage(pluginPrefix + "Your suffix has been removed.");
                }
                else {
                    Predicate<Node> removeSuffix = NodeType.SUFFIX.predicate(n -> n.getPriority() == 1);
                    updatePlayer.data().clear(removeSuffix);
                    DataMutateResult result = updatePlayer.data().remove(Node.builder("donator.suffix").build());
                    DataMutateResult result2 = updatePlayer.data().add(SuffixNode.builder(GenesisPrefix.getPlugin().waitingSuffix.get(player), 1).build());
                    GenesisPrefix.getPlugin().waitingSuffix.remove(player);
                    sender.sendMessage(pluginPrefix + "Your suffix has been set!");
                }
                GenesisPrefix.getPlugin().api.getUserManager().saveUser(updatePlayer);
                return true;
            }
            if (args[0].equals("remove")) {
                sender.sendMessage(pluginPrefix + ChatColor.translateAlternateColorCodes('&',"Your suffix will be &c&lREMOVED&e. Confirm with &c/suffix confirm&a if you wish to remove it permanently."));
                GenesisPrefix.getPlugin().waitingSuffix.put(player, "_remove");
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
