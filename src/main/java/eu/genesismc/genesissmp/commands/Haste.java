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

public class Haste implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("haste")) {
            if (sender.hasPermission("genesissmp.haste") || sender.isOp()) {
                String pluginPrefix = GenesisSMP.getPlugin().pluginPrefix;
                Player player = (Player) sender;
                if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                    player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                    player.sendMessage(pluginPrefix + "Haste has been turned " + ChatColor.RED + "OFF.");
                    GenesisSMP.getUtils().addLogEntry(sender.getName() + " turned off Haste");
                }
                else {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, true, false));
                    player.sendMessage(pluginPrefix + "Haste has been turned " + ChatColor.GREEN + "ON.");
                    GenesisSMP.getUtils().addLogEntry(sender.getName() + " turned on Haste");
                }
                return true;
            }
            return false;
        }
        return true;
    }

}
