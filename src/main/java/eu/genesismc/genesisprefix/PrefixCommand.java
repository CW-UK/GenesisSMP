package eu.genesismc.genesisprefix;

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

    private String pluginPrefix = GenesisPrefix.getPlugin().pluginPrefix;
    FileConfiguration config = GenesisPrefix.getPlugin().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;
        User updatePlayer = GenesisPrefix.getPlugin().api.getPlayerAdapter(Player.class).getUser(player);

        if (cmd.getName().equalsIgnoreCase("prefix")) {

            if (args.length < 1) {
                sender.sendMessage(pluginPrefix + ChatColor.RED + "Incorrect usage: /prefix <confirm|remove|set <prefix>>");
                return true;
            }

            if (args[0].equals("set")) {

                if (args.length < 2) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Incorrect usage: /prefix set <prefix>");
                    return true;
                }
                if (!player.hasPermission("donator.prefix")) {
                    sender.sendMessage(pluginPrefix + "You need to purchase a prefix token before you can set one.");
                    return true;
                }

                String prefixInput = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
                String firstCheckPrefix = GenesisPrefix.getUtils().initialCheck(player, prefixInput);
                String preparedPrefix = GenesisPrefix.getUtils().prepareFix(firstCheckPrefix);

                if (GenesisPrefix.getUtils().getLength(preparedPrefix) > config.getInt("max-prefix-length")) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Prefix too long. Maximum allowed length is " + GenesisPrefix.getPlugin().getConfig().getInt("max-prefix-length"));
                    return true;
                }
                if (GenesisPrefix.getUtils().getLength(preparedPrefix) < config.getInt("min-prefix-length")) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Prefix too short. Minimum length is " + GenesisPrefix.getPlugin().getConfig().getInt("min-prefix-length"));
                    return true;
                }

                sender.sendMessage(pluginPrefix + "Your prefix will display as:");
                sender.sendMessage(pluginPrefix + preparedPrefix);
                sender.sendMessage(pluginPrefix + ChatColor.translateAlternateColorCodes('&',"Type &a&l/prefix confirm&e to set it."));
                GenesisPrefix.getPlugin().waitingPrefix.put(player, preparedPrefix);

                return true;
            }

            if (args[0].equals("confirm")) {
                if (!GenesisPrefix.getPlugin().waitingPrefix.containsKey(player)) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "You do not have a prefix to confirm. Use the SET command first.");
                    return true;
                }
                if (GenesisPrefix.getPlugin().waitingPrefix.get(player).equals("_remove")) {
                    Predicate<Node> removePrefix = NodeType.PREFIX.predicate(n -> n.getPriority() == 61);
                    updatePlayer.data().clear(removePrefix);
                    GenesisPrefix.getPlugin().waitingPrefix.remove(player);
                    sender.sendMessage(pluginPrefix + "Your prefix has been removed.");
                }
                else {
                    Predicate<Node> removePrefix = NodeType.PREFIX.predicate(n -> n.getPriority() == 61);
                    updatePlayer.data().clear(removePrefix);
                    DataMutateResult result = updatePlayer.data().remove(Node.builder("donator.prefix").build());
                    DataMutateResult result2 = updatePlayer.data().add(PrefixNode.builder(GenesisPrefix.getPlugin().waitingPrefix.get(player), 61).build());
                    GenesisPrefix.getPlugin().waitingPrefix.remove(player);
                    sender.sendMessage(pluginPrefix + "Your prefix has been set!");
                }
                GenesisPrefix.getPlugin().api.getUserManager().saveUser(updatePlayer);
                return true;
            }
            if (args[0].equals("remove")) {
                sender.sendMessage(pluginPrefix + ChatColor.translateAlternateColorCodes('&',"Your prefix will be &c&lREMOVED&e. Confirm with &c/prefix confirm&a if you wish to remove it permanently."));
                GenesisPrefix.getPlugin().waitingPrefix.put(player, "_remove");
                return true;
            }

            if (args[0].equals("reload") && sender.isOp()) {
                GenesisPrefix.getPlugin().reloadConfig();
                GenesisPrefix.getPlugin().config = GenesisPrefix.getPlugin().getConfig();
                config = GenesisPrefix.getPlugin().getConfig();
                sender.sendMessage(pluginPrefix + ChatColor.GREEN + " Configuration reloaded:");
                sender.sendMessage(pluginPrefix + ChatColor.YELLOW + "Prefix: Min " + config.getString("min-prefix-length") + " / Max " + config.getString("max-prefix-length"));
                sender.sendMessage(pluginPrefix + ChatColor.YELLOW + "Suffix: Min " + config.getString("min-suffix-length") + " / Max " + config.getString("max-suffix-length"));
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
