package eu.genesismc.genesissmp;

import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;


public class PreventPickup implements Listener {

    @EventHandler
    public void pickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Skeleton) {
            e.setCancelled(true);
            e.getEntity().setCanPickupItems(false);
        }
    }

    @EventHandler
    public void drop(EntityDeathEvent e) {
        if (e.getEntity() instanceof Skeleton) {
            Random r = new Random();
            if (r.nextInt(100)+1 > 25) {
                e.getDrops().remove(new ItemStack(Material.BOW, 1));
            }
        }
    }

}
