package eu.genesismc.genesissmp.managers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardManager {

    public static final StateFlag CREATIVE_PLOT = new StateFlag("creative-plot", false);
    public static final StateFlag INVENTORY_DROP = new StateFlag("inventory-drop", false);
    public boolean registered = false;
    private static WorldGuardManager instance;

    public static WorldGuardManager getInstance() {
        if (instance == null) { instance = new WorldGuardManager(); }
        return instance;
    }

    public void registerFlags() {
        try {
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
            try {
                registry.register(CREATIVE_PLOT);
                registry.register(INVENTORY_DROP);
                Bukkit.getLogger().info(ChatColor.AQUA + "GenesisSMP > WorldGuard flags registered successfully.");
                registered = true;
            } catch (FlagConflictException e) {
                e.printStackTrace();
                Bukkit.getLogger().info(ChatColor.RED + "GenesisSMP > Failed to register WorldGuard flags!");
            }
        } catch (NoClassDefFoundError e) {
            Bukkit.getLogger().info(ChatColor.RED + "GenesisSMP > Could not register WorldGuard flags - WorldGuard not found!");
        }
    }

    public boolean regionIsCreativePlot(Player player) {
        if (player == null) { return false; }
        BukkitPlayer localPlayer = BukkitAdapter.adapt(player);
        com.sk89q.worldedit.util.Location loc = localPlayer.getLocation();
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        return query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(player), CREATIVE_PLOT); // not for boolean flag
    }

    public boolean blockIsCreativePlot(Location loc) {
        if (loc == null) { return false; }
        return StateFlag.test(WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().queryState(BukkitAdapter.adapt(loc), (RegionAssociable) null, CREATIVE_PLOT));
    }

    public boolean regionHasInvDrop(Player player) {
        if (player == null) { return false; }
        BukkitPlayer localPlayer = BukkitAdapter.adapt(player);
        com.sk89q.worldedit.util.Location loc = localPlayer.getLocation();
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        return query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(player), INVENTORY_DROP); // not for boolean flag
    }

    public boolean regionIsCreativeArena(Player player) {
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player).getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        for (ProtectedRegion region : set) {
            if (region.getId().equals("creative_arena")) {
                return true;
            }
        }
        return false;
    }


}
