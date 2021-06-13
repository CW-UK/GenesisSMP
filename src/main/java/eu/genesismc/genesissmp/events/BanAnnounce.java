package eu.genesismc.genesissmp.events;

import eu.genesismc.genesissmp.GenesisSMP;
import me.TechsCode.UltraPunishments.event.BanEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BanAnnounce implements Listener {

    @EventHandler
    public void onBan(BanEvent e) {

        FileConfiguration config = GenesisSMP.getPlugin().getConfig();
        if (!config.getBoolean("BanAnnounce.enabled")) { return; }

        String baMsg = config.getString("BanAnnounce.message");
        boolean strike = config.getBoolean("BanAnnounce.strike");
        int qty = config.getInt("BanAnnounce.strike-amount");
        Player pOn = (Player) e.getPlayer();

        if (pOn.isOnline()) {
            //pOn.chat(GenesisSMP.getUtils().getRGB(baMsg));
            pOn.chat(ChatColor.RED + "So long, and thanks for all the fish!");
            if (strike) {
                Location loc = pOn.getLocation();
                for (int i = 1; i <= qty; i++) {
                    pOn.getWorld().strikeLightningEffect(loc);
                }
            }
        }

    }

}
