package eu.genesismc.genesissmp.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangeWorld implements Listener {

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().hasPermission("essentials.fly")) {
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(true);
        }
    }

}
