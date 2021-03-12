package eu.genesismc.genesissmp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EndSpawnPoint implements Listener {

    @EventHandler
    public void onPortal(PlayerPortalEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {

            // Player already in the end - abort
            if (e.getFrom().getWorld().getName().equals("world_the_end")) { return; }

            FileConfiguration config = GenesisSMP.getPlugin().getConfig();
            if (!config.getBoolean("EndSpawnPoint.enabled")) { return; }

            // Cancel the portal event and teleport the player instead because
            // of MC's auto regen of the obsidian platform..
            e.setCancelled(true);

            World w = Bukkit.getWorld(config.getString("EndSpawnPoint.world"));
            if (w == null) {
                WorldCreator wc = new WorldCreator("world_the_end");
                Bukkit.createWorld(wc);
                w = Bukkit.getWorld("world_the_end");
            }
            double x = config.getDouble("EndSpawnPoint.x");
            double y = config.getDouble("EndSpawnPoint.y");
            double z = config.getDouble("EndSpawnPoint.z");
            float yaw = (float) config.getDouble("EndSpawnPoint.yaw");
            float pitch = (float) config.getDouble("EndSpawnPoint.pitch");
            Location loc = new Location(w, x, y, z, yaw, pitch);
            e.getPlayer().teleport(loc);

        }
    }

}
