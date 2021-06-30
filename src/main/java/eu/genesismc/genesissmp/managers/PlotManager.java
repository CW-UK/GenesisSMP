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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

public class PlotManager {

    /**
     * Check if plot is in use.
     * @param i Plot Number
     * @return boolean
     */
    public boolean plotInUse(int i) {
        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        return config.getString("Plots.Plot" + i + ".Owner") == null;
    }

    /**
     * Get owner of given plot
     * @param i Plot Number
     * @return String player
     */
    public String plotOwner(int i) {
        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        return config.getString("Plots.Plot" + i + ".Owner");
    }

    /**
     * Get timestamp of last leave time for plot
     * @param i Plot Number
     * @return int timestamp
     */
    public int plotLeaveTime(int i) {
        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        return config.getInt("Plots.Plot" + i + ".TimeLeave");
    }

    /**
     * Check if a plot is locked.
     * Returns true if individual plot is locked.
     * @param i Plot Number
     * @return boolean
     */
    public boolean plotIsLocked(int i) {
        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
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
        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        return config.getBoolean("Plots.Locked");
    }

    /**
     * Locks a plot
     * Returns true if locking was successful.
     * @param i Plot number
     * @return boolean
     */
    public boolean lockPlot(int i) {
        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        config.set("Plots.Plot"+i+".Locked", true);
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
        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
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
        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        config.set("Plots.Plot"+i+".Owner", p.getUniqueId().toString());
        config.set("Plots.Plot"+i+".Locked", true);
        config.set("Plots.InPlot." + p.getUniqueId().toString(), i);
        GenesisSMP.getPlugin().saveConfig();
    }

    /**
     * Check if player already has a plot assigned
     * Returns true/false
     * @param u UUID
     * @return boolean
     */
    public boolean hasAssignedPlot(UUID u) {
        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        if (config.contains("Plots.InPlot."+u.toString())) {
            return true;
        }
        return false;
    }

    /**
     * Get player's assigned plot number
     * Returns true/false
     * @param u UUID
     * @return int
     */
    public int getAssignedPlot(UUID u) {
        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        return config.getInt("Plots.InPlot."+u.toString());
    }

    /**
     * Get Teleport IN location of plot
     * Returns Location
     * @param i Plot number
     * @return Location
     */
    public Location getTeleportIn(int i) {
        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        return config.getLocation("Plots.Plot"+i+".TeleportIn");
    }

    /**
     * Get Teleport OUT location of plot
     * Returns Location
     * @param i Plot number
     * @return Location
     */
    public Location getTeleportOut(int i) {
        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        return config.getLocation("Plots.Plot"+i+".TeleportOut");
    }

    public void clearPlot(int plotNumber) throws IOException {

        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(Bukkit.getWorld("smphub"));
        File file = new File("plugins/GenesisSMP/schematics/plot-air.schem");
        ClipboardFormat format = ClipboardFormats.findByFile(file);

        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();
            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(85, 13, 124))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
        }

    }

    public void plotFloor(String choice) throws IOException {

        Bukkit.getLogger().info("ATTEMPT2: Trying to set floor to " + choice.toLowerCase());
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(Bukkit.getWorld("smphub"));
        File file = new File("plugins/GenesisSMP/schematics/floor-" + choice.toLowerCase() + ".schem");
        ClipboardFormat format = ClipboardFormats.findByFile(file);

        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();
            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(85, 14, 124))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
        }

    }

}
