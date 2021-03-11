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

        config.addDefault("EndSpawnPoint.enabled", true);
        config.addDefault("EndSpawnPoint.world", "world_the_end");
        config.addDefault("EndSpawnPoint.x", 98.50);
        config.addDefault("EndSpawnPoint.y", 49.0);
        config.addDefault("EndSpawnPoint.z", 0.5);
        config.addDefault("EndSpawnPoint.pitch", 90.0);
        config.addDefault("EndSpawnPoint.yaw", 0.0);

        config.addDefault("EndLootCrate.enabled", true);
        config.addDefault("EndLootCrate.x", 0);
        config.addDefault("EndLootCrate.y", 67);
        config.addDefault("EndLootCrate.z", 0);
        config.addDefault("EndLootCrate.delay", 6000L);

        config.options().copyDefaults(true);
        GenesisSMP.getPlugin().saveConfig();

    }
}
