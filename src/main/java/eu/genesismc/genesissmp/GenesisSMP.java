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

    //public APIEndpoint upun = UltraPunishments.getAPI();
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

    public static GenesisSMP getInstance() {
        return (GenesisSMP) Bukkit.getPluginManager().getPlugin("GenesisSMP");
    }

    @Override
    public void onEnable() {

        Bukkit.getLogger().info(ChatColor.AQUA + "  _____                      _      _____ __  __ _____");
        Bukkit.getLogger().info(ChatColor.AQUA + " / ____|                    (_)    / ____|  \\/  |  __ \\");
        Bukkit.getLogger().info(ChatColor.AQUA + "| |  __  ___ _ __   ___  ___ _ ___| (___ | \\  / | |__) |");
        Bukkit.getLogger().info(ChatColor.AQUA + "| | |_ |/ _ \\ '_ \\ / _ \\/ __| / __|\\___ \\| |\\/| |  ___/");
        Bukkit.getLogger().info(ChatColor.AQUA + "| |__| |  __/ | | |  __/\\__ \\ \\__ \\____) | |  | | |");
        Bukkit.getLogger().info(ChatColor.AQUA + " \\_____|\\___|_| |_|\\___||___/_|___/_____/|_|  |_|_|");
        Bukkit.getLogger().info(ChatColor.AQUA + "--------------------------------------------------------");

        // plugin variables
        plugin = this;
        utils = new Utils();

        // config initialisation
        Bukkit.getLogger().info(ChatColor.AQUA + "GenesisSMP > Loading configuration..");
        ConfigManager configManager = new ConfigManager();
        configManager.setupConfig();

        // event registers
        Bukkit.getLogger().info(ChatColor.AQUA + "GenesisSMP > Registering event handlers..");
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(new PrefixCommand(), this);
        pm.registerEvents(new SuffixCommand(), this);
        pm.registerEvents(new AdminCommand(), this);
        pm.registerEvents(new ChatFilter(), this);
        pm.registerEvents(new PreventBlockXray(), this);
        pm.registerEvents(new PreventPickup(), this);
        pm.registerEvents(new LimitStriders(), this);

        // command handlers
        Bukkit.getLogger().info(ChatColor.AQUA + "GenesisSMP > Registering command handlers..");
        this.getCommand("prefix").setExecutor(new PrefixCommand());
        this.getCommand("prefix").setTabCompleter(new PrefixCommand());
        this.getCommand("suffix").setExecutor(new SuffixCommand());
        this.getCommand("suffix").setTabCompleter(new SuffixCommand());

        // luckperms API
        Bukkit.getLogger().info(ChatColor.AQUA + "GenesisSMP > Hooking into LuckPerms API..");
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        api = provider.getProvider();

    }

}
