package eu.genesismc.genesissmp.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTarget implements Listener {

    @EventHandler
    public void vexTarget(EntityTargetEvent e) {
        Entity entity = e.getEntity();
        Entity target = e.getTarget();
        if (entity instanceof Vex && target instanceof Player) {
            if (e.getReason() != EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY) {
                e.setCancelled(true);
            }
        }
    }

}
