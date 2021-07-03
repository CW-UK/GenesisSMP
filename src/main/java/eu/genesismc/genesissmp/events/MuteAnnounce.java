package eu.genesismc.genesissmp.events;

import org.bukkit.event.Listener;

public class MuteAnnounce implements Listener {
/*
    @EventHandler
    public void onMute(MuteEvent e) {

        Bukkit.getLogger().info(ChatColor.AQUA + "BAN DETECTED!!");

        FileConfiguration config = GenesisSMP.getPlugin().config;
        if (!config.getBoolean("BanAnnounce.enabled")) { return; }

        String baMsg = config.getString("BanAnnounce.message");
        //Sound baSound = Sound.valueOf(config.getString("BanAnnounce.sound"));
        //Float baVolume = getFloat(config.getString("BanAnnounce.volume"));
        //Float baPitch = getFloat(config.getString("BanAnnounce.pitch"));
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

            //pOn.getWorld().strikeLightning(loc);
            //Bukkit.getOnlinePlayers().forEach(
            //        playerOn -> playerOn.getWorld().playSound(playerOn.getLocation(), baSound, baVolume, baPitch)
            //);
        }

    }

    public Float getFloat(String str) {
        try { return Float.valueOf(str); } catch (Exception e) { return 1.0F; }
    }
*/
}
