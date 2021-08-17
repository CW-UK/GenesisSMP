package eu.genesismc.genesissmp.events;

import eu.genesismc.genesissmp.GenesisSMP;
import eu.genesismc.genesissmp.managers.WorldGuardManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (event.getEntity() instanceof Player) {

            Player player = event.getEntity().getPlayer();

            if (event.getEntity().getPlayer().hasPermission("invdrop.bypass")) { return; } // perm prevents drop
            if (!WorldGuardManager.getInstance().regionHasInvDrop(event.getEntity().getPlayer())) {
                return;
            }

            player.sendMessage(ChatColor.GOLD +
                    "You died in an area that doesn't have Keep Inventory enabled. Your inventory was dropped at your death location!");

            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack != null) {
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                    player.getInventory().removeItem(itemStack);
                }
            }

            player.getInventory().clear();
            GenesisSMP.getUtils().addLogEntry(player.getName() + " died in an area where keepInventory is off");

        }
    }

}
