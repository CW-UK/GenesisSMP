package eu.genesismc.genesissmp.events;

import eu.genesismc.genesissmp.GenesisSMP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("essentials.fly")) {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            Player p = event.getPlayer();
            scheduler.scheduleSyncDelayedTask(GenesisSMP.getInstance(), new Runnable() {
                @Override
                public void run() {
                    p.getPlayer().setAllowFlight(true);
                    p.getPlayer().setFlying(true);
                    p.getPlayer().performCommand("fly on");
                }
            }, 20L);
        }
    }

}
