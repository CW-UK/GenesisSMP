package eu.genesismc.genesissmp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public class ConfigManager implements Listener {

    public FileConfiguration config = GenesisSMP.getPlugin().getConfig();

    public void setupConfig() {
        config.addDefault("prefixes.min-prefix-length", 3);
        config.addDefault("prefixes.max-prefix-length", 16);
        config.addDefault("suffixes.min-suffix-length", 3);
        config.addDefault("suffixes.max-suffix-length", 16);
        config.addDefault("LimitStriders.enabled", true);
        config.addDefault("PreventPickup.enabled", true);
        config.addDefault("PreventBlockXray.enabled", true);
        config.options().copyDefaults(true);
        GenesisSMP.getPlugin().saveConfig();
    }
}
