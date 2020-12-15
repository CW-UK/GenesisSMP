package eu.genesismc.genesisprefix;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GenesisPrefix implements Listener {

    @EventHandler (ignoreCancelled = true)
    public void onPlayerTalk(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
    }

}
