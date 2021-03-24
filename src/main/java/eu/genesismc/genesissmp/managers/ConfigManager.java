package eu.genesismc.genesissmp.managers;

import eu.genesismc.genesissmp.GenesisSMP;
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

        config.addDefault("BanAnnounce.enabled", true);
        config.addDefault("BanAnnounce.strike", true);
        config.addDefault("BanAnnounce.strike-amount", 2);
        config.addDefault("BanAnnounce.message", "So long, and thanks for all the fish.");
        config.addDefault("BanAnnounce.sound", "ENTITY_LIGHTNING_BOLT_THUNDER");
        config.addDefault("BanAnnounce.volume", 1.0F);
        config.addDefault("BanAnnounce.pitch", 1.0F);

        config.options().copyDefaults(true);
        GenesisSMP.getPlugin().saveConfig();

    }
}
