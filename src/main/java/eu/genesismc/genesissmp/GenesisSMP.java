package eu.genesismc.genesissmp;

import eu.genesismc.genesissmp.commands.*;
import eu.genesismc.genesissmp.events.*;
import eu.genesismc.genesissmp.managers.ConfigManager;
import eu.genesismc.genesissmp.managers.WorldGuardManager;
import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
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

    public static GenesisSMP getInstance() {
        return (GenesisSMP) Bukkit.getPluginManager().getPlugin("GenesisSMP");
    }

    @Override
    public void onLoad() {
        WorldGuardManager.getInstance().registerFlags();
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

        // event registration
        Bukkit.getLogger().info(ChatColor.AQUA + "GenesisSMP > Registering event handlers..");
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(new PrefixCommand(), this);
        pm.registerEvents(new SuffixCommand(), this);
        pm.registerEvents(new AdminCommand(), this);
        pm.registerEvents(new PlotCommand(), this);
        pm.registerEvents(new InventoryCommands(), this);
        pm.registerEvents(new EntityPickupItem(), this);
        pm.registerEvents(new CreatureSpawn(), this);
        pm.registerEvents(new PlayerPortal(), this);
        pm.registerEvents(new ClearCrate(), this);
        pm.registerEvents(new BanAnnounce(), this);
        pm.registerEvents(new EntityTarget(), this);
        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new BlockPlace(), this);
        pm.registerEvents(new PlayerDeath(), this);
        pm.registerEvents(new HangingBreak(), this);
        // pm.registerEvents(new ChatFilter(), this);
        // pm.registerEvents(new PreventBlockXray(), this);

        // command handlers
        Bukkit.getLogger().info(ChatColor.AQUA + "GenesisSMP > Registering command handlers..");
        this.getCommand("prefix").setExecutor(new PrefixCommand());
        this.getCommand("prefix").setTabCompleter(new PrefixCommand());
        this.getCommand("suffix").setExecutor(new SuffixCommand());
        this.getCommand("suffix").setTabCompleter(new SuffixCommand());
        this.getCommand("clearcrate").setExecutor(new ClearCrate());
        this.getCommand("clearcrate").setTabCompleter(new ClearCrate());
        this.getCommand("gsmp").setExecutor(new AdminCommand());
        this.getCommand("gsmp").setTabCompleter(new AdminCommand());
        this.getCommand("invman").setExecutor(new InventoryCommands());
        this.getCommand("invman").setTabCompleter(new InventoryCommands());
        this.getCommand("plot").setExecutor(new PlotCommand());
        this.getCommand("plot").setTabCompleter(new PlotCommand());

        // WorldGuard check
        Bukkit.getLogger().info(ChatColor.AQUA + "GenesisSMP > Hooking into WorldGuard..");
        if (WorldGuardManager.getInstance().registered) {
            Bukkit.getLogger().info(ChatColor.AQUA + "GenesisSMP > WorldGuard hook successful.");
        }
        else {
            Bukkit.getLogger().info(ChatColor.RED + "GenesisSMP > WorldGuard hook failed!");
        }

        // luckperms API
        try {
            Bukkit.getLogger().info(ChatColor.AQUA + "GenesisSMP > Hooking into LuckPerms API..");
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            api = provider.getProvider();
            Bukkit.getLogger().info(ChatColor.AQUA + "GenesisSMP > LuckPerms hook successful.");
        } catch (NullPointerException npe) {
            Bukkit.getLogger().info(ChatColor.RED + "GenesisSMP > Could not connect to LuckPerms API.");
        }

        // register BlockChunkLimit blocks and generate permission nodes
        try {
            for (String key : config.getConfigurationSection("BlockChunkLimit.blocks").getKeys(false)) {
                Bukkit.getPluginManager().addPermission(new Permission("genesissmp.blocklimit.bypass." + key));
            }
        } catch (NullPointerException e) {
            Bukkit.getLogger().info("GenesisSMP: Failed to get list from BlockChunkLimit in config.");
        }
    }

}
