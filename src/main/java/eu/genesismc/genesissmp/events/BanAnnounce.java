package eu.genesismc.genesissmp.events;

import eu.genesismc.genesissmp.GenesisSMP;
import me.TechsCode.UltraPunishments.event.BanEvent;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
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
        Boolean strike = config.getBoolean("BanAnnounce.strike");
        int qty = config.getInt("BanAnnounce.strike-amount");
        OfflinePlayer pOff = e.getPlayer();
        Player pOn = (Player) e.getPlayer();

        if (pOn.isOnline()) {
            pOn.chat(GenesisSMP.getUtils().getRGB(baMsg));
            if (strike) {
                Location loc = pOn.getLocation();
                for (int i = 1; i <= qty; i++) {
                    pOn.getWorld().strikeLightningEffect(loc);
                }
            }
        }

    }

    public Float getFloat(String str) {
        try { return Float.valueOf(str); } catch (Exception e) { return 1.0F; }
    }

}
