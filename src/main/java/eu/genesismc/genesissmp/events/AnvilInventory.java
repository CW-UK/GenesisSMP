package eu.genesismc.genesissmp.events;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class AnvilInventory implements Listener {

    @EventHandler (ignoreCancelled = false)
    public void onAnvil(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) { return; }
        if (e.getInventory().getType() == InventoryType.ANVIL) {
            if (e.getCurrentItem().getType() == Material.SPAWNER) {
                e.setCancelled(true);
                e.getWhoClicked().sendMessage(ChatColor.RED + "You cannot rename spawners.");
            }
        }
    }

}
