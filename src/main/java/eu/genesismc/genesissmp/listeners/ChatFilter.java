package eu.genesismc.genesissmp.listeners;

import org.bukkit.event.Listener;

public class ChatFilter implements Listener {
/*
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();

        //if (p.hasPermission("genesisprefix.exempt")) { return; }

        String message = ChatColor.stripColor(e.getMessage());

        if(e.getMessage().contains("nigg") ||
           e.getMessage().contains("n1gg") ||
           e.getMessage().contains("negro")) {
            e.setCancelled(true);
            p.sendMessage(org.bukkit.ChatColor.RED + "Your message could not be sent.");
        }


        // getUpper(message)
        if (message.length() > 4) {
            if (getUpper(message) > 50) {
                e.setCancelled(true);
                p.sendMessage(org.bukkit.ChatColor.RED + "Your message had too much SHOUTING to send.");
            }
        }

    }

    public int getUpper(String input) {
        int upperCase = 0;
        String noSpaces = StringUtils.remove(input, " ");
        int msgLength = noSpaces.length();
        for (int k = 0; k < msgLength; k++) {
            if (Character.isUpperCase(noSpaces.charAt(k))) { upperCase++; }
        }
        return (upperCase * 100 / msgLength);
    }
*/
}
