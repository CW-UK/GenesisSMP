package eu.genesismc.genesissmp.events;

import eu.genesismc.genesissmp.GenesisSMP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.PigZombie;
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
            if (r.nextInt(100) + 1 > 2) {
                e.setCancelled(true);
            }
        }

        if (e.getEntity() instanceof PigZombie && e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NETHER_PORTAL) {
            if (e.getLocation().getWorld().getName().contains("smphub")) {
                e.setCancelled(true);
            }
        }

    }

}
