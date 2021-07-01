package eu.genesismc.genesissmp.managers;

import eu.genesismc.genesissmp.GenesisSMP;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;

public class PlaceholderManager extends PlaceholderExpansion implements Listener {

    @Override
    public String getAuthor() { return "CW_UK"; }
    @Override
    public String getVersion() { return "1.0"; }
    @Override
    public String getIdentifier() { return "plots"; }
    @Override
    public boolean canRegister() { return true; }
    @Override
    public boolean persist() { return true; }

    PlotManager plotManager = new PlotManager();
    InventoryManager inventoryManager = new InventoryManager();
    String pluginPrefix = GenesisSMP.getPlugin().pluginPrefix;

    @Override
    public String onPlaceholderRequest(Player player, String ph) {

        if (player == null || ph == null) {
            return "";
        }

        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        String noPlaceholder = "Invalid";

        if (ph.startsWith("expiry_")) {

            try {
                int plot = Integer.parseInt(ph.replace("expiry_", ""));
                long millis = config.getLong("Plots.Plot" + plot + ".Expires") - System.currentTimeMillis();

                String msg = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                if (msg.contains("-")) {
                    msg = "Expiring Now";
                    plotManager.expirePlot(plot);
                }

                return msg;

            } catch (Exception e) {
                Bukkit.getLogger().info("Something went really wrong.");
            }

            return "ERROR";

        }

        return noPlaceholder;

    }

}
