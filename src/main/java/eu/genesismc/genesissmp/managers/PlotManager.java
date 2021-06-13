package eu.genesismc.genesissmp.managers;

import eu.genesismc.genesissmp.GenesisSMP;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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

}
