package eu.genesismc.genesissmp;

import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class GenesisSMP extends JavaPlugin implements Listener {

    private static GenesisSMP plugin;
    public FileConfiguration config = this.getConfig();
    public LuckPerms api;
    private Utils utils;
    public HashMap<Player, String> waitingPrefix = new HashMap<Player, String>();
    public HashMap<Player, String> waitingSuffix = new HashMap<Player, String>();
    public String pluginPrefix = ChatColor.translateAlternateColorCodes('&', "&6&lGenesisMc > &e");
    public static GenesisSMP getPlugin() {
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
        pm.registerEvents(new ChatFilter(), this);

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
