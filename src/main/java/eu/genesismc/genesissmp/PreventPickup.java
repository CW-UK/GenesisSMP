package eu.genesismc.genesissmp;

import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PreventPickup implements Listener {

    @EventHandler
    public void pickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Skeleton) {
            e.setCancelled(true);
            e.getEntity().setCanPickupItems(false);
        }
    }

}
