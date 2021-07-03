package eu.genesismc.genesissmp.managers;

import eu.genesismc.genesissmp.GenesisSMP;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class PlaceholderManager extends PlaceholderExpansion implements Listener {

    @Override
    public @NotNull String getAuthor() { return "CW_UK"; }
    @Override
    public @NotNull String getVersion() { return "1.0"; }
    @Override
    public @NotNull String getIdentifier() { return "plots"; }
    @Override
    public boolean canRegister() { return true; }
    @Override
    public boolean persist() { return true; }

    @Override
    public String onPlaceholderRequest(Player player, String ph) {

        if (player == null || ph == null) { return ""; }

        if (ph.startsWith("expiry_")) {

            try {
                FileConfiguration config = GenesisSMP.getPlugin().config;
                int plot = Integer.parseInt(ph.replace("expiry_", ""));
                long millis = config.getLong("Plots.Plot" + plot + ".Expires") - System.currentTimeMillis();

                if (millis < 1) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "holo setline plot"+plot + " 6 {animation: expiring.txt}");
                    return "";
                }

                String msg = String.format(
                        "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                );

                if (plot == 5) { Bukkit.getLogger().info("Placeholder will be " + msg); }

                return msg;

            } catch (Exception e) {
                Bukkit.getLogger().info("Something went really wrong.");
                return "ERROR";
            }
        }

        return "INVALID";
    }

}
