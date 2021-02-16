package eu.genesismc.genesisprefix;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public class ConfigManager implements Listener {

    public FileConfiguration config = GenesisPrefix.getPlugin().getConfig();

    public void setupConfig() {
        config.addDefault("min-prefix-length", 3);
        config.addDefault("max-prefix-length", 16);
        config.addDefault("min-suffix-length", 3);
        config.addDefault("max-suffix-length", 16);
        config.options().copyDefaults(true);
        GenesisPrefix.getPlugin().saveConfig();
    }
}
