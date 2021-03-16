package eu.genesismc.genesissmp.events;

import eu.genesismc.genesissmp.GenesisSMP;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class EntityPickupItem implements Listener {

    FileConfiguration config = GenesisSMP.getPlugin().getConfig();

    @EventHandler
    public void pickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Skeleton) {
            if (!config.getBoolean("PreventPickup.enabled")) { return; }
            e.setCancelled(true);
            e.getEntity().setCanPickupItems(false);
        }
    }

    @EventHandler
    public void drop(EntityDeathEvent e) {
        if (e.getEntity() instanceof Skeleton) {
            if (!config.getBoolean("PreventPickup.enabled")) { return; }
            Random r = new Random();
            if (r.nextInt(100)+1 > 25) {
                e.getDrops().remove(new ItemStack(Material.BOW, 1));
            }
        }
    }

}
