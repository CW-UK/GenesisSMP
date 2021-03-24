package eu.genesismc.genesissmp.events;

import eu.genesismc.genesissmp.GenesisSMP;
import me.TechsCode.UltraPunishments.event.BanEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BanAnnounce implements Listener {

    @EventHandler (priority = EventPriority.HIGH)
    public void onBan(BanEvent e) {

        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        if (!config.getBoolean("BanAnnounce.enabled")) { return; }

        String baMsg = config.getString("BanAnnounce.message");
        Sound baSound = Sound.valueOf(config.getString("BanAnnounce.sound"));
        Float baVolume = getFloat(config.getString("BanAnnounce.volume"));
        Float baPitch = getFloat(config.getString("BanAnnounce.pitch"));

        OfflinePlayer pOff = e.getPlayer();
        Player pOn = (Player) e.getPlayer();

        if (pOn.isOnline()) {
            Bukkit.getOnlinePlayers().forEach(
                    playerOn -> playerOn.getWorld().playSound(playerOn.getLocation(), baSound, baVolume, baPitch)
            );
            pOn.chat(baMsg);
        }

        // TODO: Test this as using offline player will also announce banning players that have already left, for example.
        // TODO: Is there a way to get last online time and compare with current timestamp maybe?
        else {
         /*   Bukkit.getOnlinePlayers().forEach(
                    playerOn -> {
                        playerOn.getWorld().playSound(playerOn.getLocation(), baSound, baVolume, baPitch);
                        playerOn.sendMessage(pOff + " " + ChatColor.GRAY + ">>" + ChatColor.RESET + baMsg);
                    }
            );*/
            Bukkit.getLogger().info("GenesisSMP > DEBUG: Player " + pOff + " was banned but detected as being offline.");
        }
    }

    public Float getFloat(String str) {
        try { return Float.valueOf(str); } catch (Exception e) { return 1.0F; }
    }

}
