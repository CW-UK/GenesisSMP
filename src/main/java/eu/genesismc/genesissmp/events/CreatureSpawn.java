package eu.genesismc.genesissmp.events;

import eu.genesismc.genesissmp.GenesisSMP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Strider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Random;

public class CreatureSpawn implements Listener {

    @EventHandler
    public void striderSpawn(CreatureSpawnEvent e) {
        if (e.getEntity() instanceof Strider && e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {

            FileConfiguration config = GenesisSMP.getInstance().config;

            if (!config.getBoolean("LimitStriders.enabled")) {
                return;
            }
            Random r = new Random();
            if (r.nextInt(100) + 1 > 4) {
                e.setCancelled(true);
            }
        }
    }

}
