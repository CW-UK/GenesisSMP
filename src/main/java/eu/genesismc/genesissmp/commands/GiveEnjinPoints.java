package eu.genesismc.genesissmp.commands;

import com.enjin.core.EnjinServices;
import com.enjin.rpc.mappings.mappings.general.RPCData;
import com.enjin.rpc.mappings.services.PointService;
import eu.genesismc.genesissmp.GenesisSMP;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class GiveEnjinPoints {

    PointService pointService = EnjinServices.getService(PointService.class);

    public boolean isRegistered(String player) {
        try {
            double points = EnjinServices.getService(PointService.class).get(player).getResult();
            //Bukkit.getLogger().info(player + " has " + points + " on Enjin..");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void giveEnjinPoints(String player, double points, @Nullable String reason) {

        Bukkit.getScheduler().runTaskAsynchronously(GenesisSMP.getPlugin(), () -> {
            try {
                String msg = reason == null ? "." : " for " + reason;
                RPCData<Integer> response = pointService.add(player, (int) points);
                boolean registered = isRegistered(player);

                try {
                    Player onlinePlayer = Bukkit.getPlayerExact(player);
                    if (onlinePlayer.isOnline()) {
                        if (response == null || response.getError() != null) {
                            if (!registered) {
                                onlinePlayer.sendMessage(ChatColor.YELLOW + "You missed out on " + (int) points + " points as your account isn't linked.");
                                return;
                            }
                        }
                        onlinePlayer.sendMessage(ChatColor.YELLOW + "You have been awarded " + (int) points + " points" + msg);
                    }
                } catch (NullPointerException e) {
                    // ignore
                }

                if (registered) {
                    Bukkit.getLogger().info("Awarded " + player + " " + (int) points + " points" + msg);
                } else {
                    Bukkit.getLogger().info("Could not award " + player + " " + (int) points + " points" + msg);
                }

            } catch (Exception ex) {
                // ignore
                Bukkit.getLogger().info("[GenesisSMP] Error with giving points to " + player);
                Bukkit.getLogger().info(ex.getStackTrace().toString());
            }

        });

    }

}
