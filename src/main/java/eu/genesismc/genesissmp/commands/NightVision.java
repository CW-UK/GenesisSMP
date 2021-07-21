package eu.genesismc.genesissmp.commands;

import eu.genesismc.genesissmp.GenesisSMP;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVision implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("nv")) {
            if (sender.hasPermission("genesissmp.nightvision") || sender.isOp()) {
                String pluginPrefix = GenesisSMP.getPlugin().pluginPrefix;
                Player player = (Player) sender;
                if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    player.sendMessage(pluginPrefix + "Night vision has been turned " + ChatColor.RED + "OFF.");
                }
                else {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, true, false));
                    player.sendMessage(pluginPrefix + "Night vision has been turned " + ChatColor.GREEN + "ON.");
                }
                return true;
            }
            return false;
        }
        return true;
    }

}
