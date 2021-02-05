package eu.genesismc.genesisprefix;

import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.*;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenesisPrefix extends JavaPlugin implements Listener {

    public static HashMap<Player, String> waitingPrefix = new HashMap<Player, String>();
    public static HashMap<Player, String> waitingSuffix = new HashMap<Player, String>();
    LuckPerms api;
    String pluginPrefix = ChatColor.translateAlternateColorCodes('&', "&6&lGenesisMc > &e");

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }
        final FileConfiguration config = this.getConfig();
        config.addDefault("max-length", 16);
        config.addDefault("allow-k", false); // magic
        config.addDefault("allow-l", true);  // bold
        config.addDefault("allow-o", true);  // italic
        config.addDefault("allow-m", true);  // strike
        config.addDefault("allow-n", true);  // underline
        config.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;
        User updatePlayer = api.getPlayerAdapter(Player.class).getUser(player);

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
                String firstCheckPrefix = initialCheck(player, prefixInput);
                String preparedPrefix = prepareFix(firstCheckPrefix);

                if (getLength(preparedPrefix) > this.getConfig().getInt("max-length")) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Prefix too long. Maximum allowed length is " + this.getConfig().getInt("max-length"));
                    return true;
                }
                sender.sendMessage(pluginPrefix + "Your prefix will display as:");
                sender.sendMessage(pluginPrefix + preparedPrefix);
                sender.sendMessage(pluginPrefix + ChatColor.translateAlternateColorCodes('&',"Type &a&l/prefix confirm&e to set it, or use &a&l/prefix set&e again."));
                waitingPrefix.put(player, preparedPrefix);
                return true;
            }
            if (args[0].equals("confirm")) {
                if (!waitingPrefix.containsKey(player)) {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "You do not have a prefix to confirm. Use the SET command first.");
                    return true;
                }
                if (waitingPrefix.get(player).equals("_remove")) {
                    Predicate<Node> removePrefix = NodeType.PREFIX.predicate(n -> n.getPriority() == 61);
                    updatePlayer.data().clear(removePrefix);
                    waitingPrefix.remove(player);
                    sender.sendMessage(pluginPrefix + "Your prefix has been removed.");
                }
                else {
                    Predicate<Node> removePrefix = NodeType.PREFIX.predicate(n -> n.getPriority() == 61);
                    updatePlayer.data().clear(removePrefix);
                    DataMutateResult result = updatePlayer.data().remove(Node.builder("donator.prefix").build());
                    DataMutateResult result2 = updatePlayer.data().add(PrefixNode.builder(waitingPrefix.get(player), 61).build());
                    waitingPrefix.remove(player);
                    sender.sendMessage(pluginPrefix + "Your prefix has been set!");
                }
                api.getUserManager().saveUser(updatePlayer);
                return true;
            }
            if (args[0].equals("remove")) {
                sender.sendMessage(pluginPrefix + ChatColor.translateAlternateColorCodes('&',"Your prefix will be &c&lREMOVED&e. Confirm with &c/prefix confirm&a if you wish to remove it permanently."));
                waitingPrefix.put(player, "_remove");
                return true;
            }
            return false;
        }
        return false;
    }

    public String prepareFix (String input) {
        return ChatColor.GRAY + "[" + getRGB(input) + ChatColor.GRAY + "]";
    }

    public String getRGB (String input) {
        final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
        char COLOR_CHAR = ChatColor.COLOR_CHAR;
        Matcher matcher = HEX_PATTERN.matcher(ChatColor.translateAlternateColorCodes('&', input));
        StringBuffer buffer = new StringBuffer(input.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

    public int getLength (String input) {
        return ChatColor.stripColor(input).length() - 2;
    }

    public boolean canUse (Player p, String chr) {
        return p.hasPermission("genesisprefix.code." + chr);
    }

    public String initialCheck (Player p, String input) {
        String output = input;
        if (!canUse(p, "k")) { output = output.replace("&k", ""); }
        if (!canUse(p, "l")) { output = output.replace("&l", ""); }
        if (!canUse(p, "o")) { output = output.replace("&o", ""); }
        if (!canUse(p, "m")) { output = output.replace("&m", ""); }
        if (!canUse(p, "n")) { output = output.replace("&n", ""); }
        return output;
    }

}
