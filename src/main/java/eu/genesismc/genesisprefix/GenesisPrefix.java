package eu.genesismc.genesisprefix;

import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.*;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GenesisPrefix extends JavaPlugin implements Listener {

    private static GenesisPrefix plugin;
    public FileConfiguration config = this.getConfig();
    public LuckPerms api;
    private Utils utils;
    public HashMap<Player, String> waitingPrefix = new HashMap<Player, String>();
    public HashMap<Player, String> waitingSuffix = new HashMap<Player, String>();
    public String pluginPrefix = ChatColor.translateAlternateColorCodes('&', "&6&lGenesisMc > &e");
    public static GenesisPrefix getPlugin() {
        return plugin;
    }
    public static Utils getUtils() {
        return getPlugin().utils;
    }

    @Override
    public void onEnable() {

        // plugin variables
        plugin = this;
        utils = new Utils();

        // config initialisation
        ConfigManager configManager = new ConfigManager();
        configManager.setupConfig();

        // event registers
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(new PrefixCommand(), this);
        pm.registerEvents(new SuffixCommand(), this);
        pm.registerEvents(new AdminCommand(), this);

        // command handlers
        this.getCommand("prefix").setExecutor(new PrefixCommand());
        this.getCommand("prefix").setTabCompleter(new PrefixCommand());
        this.getCommand("suffix").setExecutor(new SuffixCommand());
        this.getCommand("suffix").setTabCompleter(new SuffixCommand());

        // luckperms API
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        api = provider.getProvider();
    }







}
