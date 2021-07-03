package eu.genesismc.genesissmp.managers;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import eu.genesismc.genesissmp.GenesisSMP;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PlotManager {

    InventoryManager inventoryManager = new InventoryManager();

    /**
     * Check if plot is in use.
     * @param i Plot Number
     * @return boolean
     */
    public boolean plotInUse(int i) {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        return config.getString("Plots.Plot" + i + ".Owner") != null;
    }

    /**
     * Get owner of given plot
     * @param i Plot Number
     * @return String player
     */
    public String plotOwner(int i) {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        return config.getString("Plots.Plot" + i + ".Owner");
    }

    /**
     * Check if a plot is locked.
     * Returns true if individual plot is locked.
     * @param i Plot Number
     * @return boolean
     */
    public boolean plotIsLocked(int i) {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        if (config.getBoolean("Plots.Plot" + i + ".Locked")) {
            return true;
        }
        return false;
    }

    /**
     * Check if all plots are locked.
     * Returns true if ALL plots are locked.
     * @return boolean
     */
    public boolean allPlotsLocked() {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        return config.getBoolean("Plots.Locked");
    }

    /**
     * Locks a plot
     * Returns true if locking was successful.
     * @param i Plot number
     * @return boolean
     */
    public boolean lockPlot(int i) {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        config.set("Plots.Plot"+i+".Locked", true);
        GenesisSMP.getPlugin().saveConfig();
        return true;
    }

    /**
     * Locks all plots
     * Returns true if locking was successful.
     * @return boolean
     */
    public boolean lockAllPlots() {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        config.set("Plots.Locked", true);
        GenesisSMP.getPlugin().saveConfig();
        return true;
    }

    /**
     * Unlocks all plots
     * Returns true if unlocking was successful.
     * @return boolean
     */
    public boolean unlockAllPlots() {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        config.set("Plots.Locked", false);
        GenesisSMP.getPlugin().saveConfig();
        return true;
    }

    /**
     * Unlocks a plot
     * Returns true if unlocking was successful.
     * @param i Plot number
     * @return boolean
     */
    public boolean unlockPlot(int i) {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        config.set("Plots.Plot"+i+".Locked", false);
        GenesisSMP.getPlugin().saveConfig();
        return true;
    }

    /**
     * Assign plot to player
     * @param i Plot number
     * @param p Player object
     */
    public void assignPlotToPlayer(int i, Player p) {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        long timeToExpire = config.getLong("Plots.ExpiryTime");
        config.set("Plots.Plot"+i+".Owner", p.getName());
        config.set("Plots.Plot"+i+".Locked", true);
        config.set("Plots.Plot"+i+".Expires", System.currentTimeMillis() + timeToExpire);
        config.set("Plots.InPlot." + p.getName(), i);
        GenesisSMP.getPlugin().saveConfig();
    }

    /**
     * Unassign plot from player
     * @param i Plot number
     * @param name Player name as string
     */
    public void unassignPlotFromPlayer(int i, String name) {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        config.set("Plots.Plot"+i+".Owner", null);
        config.set("Plots.Plot"+i+".Locked", false);
        config.set("Plots.Plot"+i+".Expires", null);
        config.set("Plots.InPlot." + name, null);
        GenesisSMP.getPlugin().saveConfig();
    }

    /**
     * Check if player already has a plot assigned
     * Returns true/false
     * @param p Player
     * @return boolean
     */
    public boolean hasAssignedPlot(Player p) {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        if (config.contains("Plots.InPlot."+p.getName())) {
            return true;
        }
        return false;
    }

    /**
     * Get player's assigned plot number
     * Returns true/false
     * @param p Player
     * @return int
     */
    public int getAssignedPlot(Player p) {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        return config.getInt("Plots.InPlot."+p.getName());
    }

    /**
     * Get specified coordinate of plot center point
     * Returns int
     * @param coord X,Y,Z
     * @param plot int
     * @param isFloor boolean
     * @return Location
     */
    public int plotCenter(String coord, int plot, boolean isFloor) {
        FileConfiguration config = GenesisSMP.getPlugin().config;
        if (isFloor && coord.equals("Y")) {
            return config.getInt("Plots.Plot" + plot + ".Center" + coord) + 1;
        } else {
            return config.getInt("Plots.Plot"+plot+".Center"+coord);
        }
    }

    /**
     * Expires a plot
     * Returns void
     * @param plot int
     */
    public void expirePlot(int plot) {

        String plotOwner = plotOwner(plot);
        Bukkit.getLogger().info("Player is " + plotOwner);
        String pluginPrefix = GenesisSMP.getPlugin().pluginPrefix;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg removemember -w smphub plot" + plot + " " + plotOwner);
        unassignPlotFromPlayer(plot, plotOwner);
        plotSignExpired(plot);

        if (Bukkit.getPlayerExact(plotOwner) != null) {
            Player whosePlot = Bukkit.getPlayerExact(plotOwner);
            assert whosePlot != null;
            whosePlot.sendMessage(pluginPrefix + "Your creative plot has expired.");
            /*try {
                Bukkit.getLogger().info("Restoring inventory");
                inventoryManager.restoreInventory(whosePlot);
            } catch (Exception e) {
                //ignore
            }*/
        }
        /*else {
            Bukkit.getLogger().info("Player was offline, unable to restore.");
        }*/

    }

    /**
     * Clears the contents of a plot
     * Returns void
     * @param plotNumber int
     */
    public void clearPlot(int plotNumber) throws IOException {
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(Bukkit.getWorld("smphub"));
        File file = new File("plugins/GenesisSMP/schematics/plot-air.schem");
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();
            int clearX = plotCenter("X", plotNumber, false);
            int clearY = plotCenter("Y", plotNumber, false);
            int clearZ = plotCenter("Z", plotNumber, false);
            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(clearX, clearY, clearZ))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Changes the floor of a plot
     * Returns void
     * @param plotNumber int
     * @param choice String
     */
    public void plotFloor(int plotNumber, String choice) throws IOException {
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(Bukkit.getWorld("smphub"));
        File file = new File("plugins/GenesisSMP/schematics/floor-" + choice.toLowerCase() + ".schem");
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        int floorX = plotCenter("X", plotNumber, true);
        int floorY = plotCenter("Y", plotNumber, true);
        int floorZ = plotCenter("Z", plotNumber, true);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();
            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(floorX, floorY, floorZ))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Changes the hologram of a plot to 'Claimed'
     * Returns void
     * @param player Player
     * @param plot int
     */
    public void plotSignClaimed(Player player, int plot) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "holo setline plot"+plot + " 3 &c&l✖ Claimed ✖");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "holo setline plot"+plot + " 4 " + player.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "holo setline plot"+plot + " 5 &f");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "holo setline plot"+plot + " 6 {medium}&e%plots_expiry_"+plot+"%");
    }

    /**
     * Changes the hologram of a plot to 'Available'
     * Returns void
     * @param plot int
     */
    public void plotSignExpired(int plot) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "holo setline plot"+plot + " 3 &a&lAvailable");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "holo setline plot"+plot + " 4 &f");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "holo setline plot"+plot + " 5 &f&oClaim this plot with");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "holo setline plot"+plot + " 6 &f&o/plot enter " + plot);
    }

    /**
     * Checks each plot and expires if necessary
     * Returns void
     */

    public void runExpiryCheck() {
        Bukkit.getLogger().info("Checking creative plots for expired claims..");
        FileConfiguration config = GenesisSMP.getPlugin().config;
        long currentTime = System.currentTimeMillis();
        for (int i = 1; i < 8; i++) {
            long expires = config.getLong("Plots.Plot"+i+".Expires", 999);
            if (expires != 999) {
                Bukkit.getLogger().info("Plot " + i + " will be checked.");
                if (currentTime > expires) {
                    Bukkit.getLogger().info(ChatColor.RED + "Plot "+i+" needs to expire..");
                    expirePlot(i);
                }
            }
            /*else {
                Bukkit.getLogger().info("Plot " + i + " does not need to be checked.");
            }*/
        }
    }

}
