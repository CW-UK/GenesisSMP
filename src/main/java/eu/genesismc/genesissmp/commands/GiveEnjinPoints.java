package eu.genesismc.genesissmp.commands;

import com.enjin.core.EnjinServices;
import com.enjin.rpc.mappings.mappings.general.RPCData;
import com.enjin.rpc.mappings.services.PointService;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GiveEnjinPoints {

    public boolean isRegistered(Player player) {
        try {
            double points = EnjinServices.getService(PointService.class).get(player.getName()).getResult();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void giveEnjinPoints(Player player, double points, String reason) {
        try {
            // TODO: Make async, run as async task
            String msg = reason == null ? "." : " for " + reason;
            PointService pointService = EnjinServices.getService(PointService.class);
            RPCData<Integer> response = pointService.add(player.getName(), (int) points);
            if (response == null || response.getError() != null) {
                player.sendMessage(ChatColor.YELLOW + "You missed out on " + points + " points as your account isn't linked.");
            }
            player.sendMessage(ChatColor.YELLOW + "You have been awarded " + points + " points" + msg);
        } catch (Exception ex) {
            Bukkit.getLogger().info("[GenesisSMP] Could not give points for " + player.getName());
        }
    }

}
