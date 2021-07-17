package eu.genesismc.genesissmp.events;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
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
            if (e.getCurrentItem().getType() == Material.SPAWNER && !e.getWhoClicked().isOp()) {
                e.setCancelled(true);
                e.getWhoClicked().sendMessage(ChatColor.RED + "Sorry, spawners cannot be renamed by players.");
                //try { e.getWhoClicked().closeInventory(); } catch (Exception ex) { /*ignore*/ }
                String msg = ChatColor.translateAlternateColorCodes('&',
                        "&3[&eGenesisSMP&3] Prevented &e" + e.getWhoClicked().getName() + " &3from &erenaming a spawner&3 on an anvil."
                );
                Bukkit.broadcast(msg, "illegalstack.notify");

            }
        }
    }

}
