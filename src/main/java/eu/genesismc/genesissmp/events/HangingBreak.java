package eu.genesismc.genesissmp.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;

public class HangingBreak implements Listener {

    @EventHandler (ignoreCancelled = true)
    public void hangingBreak(HangingBreakEvent e) {
        if (!e.getEntity().getWorld().toString().toLowerCase().contains("smphub")) {
            return;
        }
        if (e.getCause() == HangingBreakEvent.RemoveCause.PHYSICS) {
            e.setCancelled(true);
        }
    }

}
