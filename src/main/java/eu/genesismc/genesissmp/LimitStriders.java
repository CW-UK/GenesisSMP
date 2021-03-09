package eu.genesismc.genesissmp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Strider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Random;

public class LimitStriders implements Listener {

    FileConfiguration config = GenesisSMP.getPlugin().getConfig();

    @EventHandler
    public void striderSpawn(CreatureSpawnEvent e) {
        Entity spawnedEntity = e.getEntity();
        if (spawnedEntity instanceof Strider && e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            if (!config.getBoolean("LimitStriders.enabled")) { return; }
            Random r = new Random();
            int result = r.nextInt(100)+1;
            if (result > 6) {
                e.setCancelled(true);
            }
        }
    }

}
