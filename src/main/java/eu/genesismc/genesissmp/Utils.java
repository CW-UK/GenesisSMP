package eu.genesismc.genesissmp;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public String initialCheck (Player p, String input) {
        String output = input;
        if (!canUse(p, "k")) { output = output.replace("&k", ""); }
        if (!canUse(p, "l")) { output = output.replace("&l", ""); }
        if (!canUse(p, "o")) { output = output.replace("&o", ""); }
        if (!canUse(p, "m")) { output = output.replace("&m", ""); }
        if (!canUse(p, "n")) { output = output.replace("&n", ""); }
        return output;
    }

    public String getRGB (String input) {
        final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
        char COLOR_CHAR = ChatColor.COLOR_CHAR;
        Matcher matcher = HEX_PATTERN.matcher(ChatColor.translateAlternateColorCodes('&', input));
        StringBuffer buffer = new StringBuffer(input.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

    public int getLength (String input) {
        String toCheck = ChatColor.stripColor(input);
        return (toCheck.length() - 2);
    }

    public boolean canUse (Player p, String chr) {
        return p.hasPermission("genesisprefix.code." + chr);
    }

    public String prepareFix (String input) {
        return "&7[" + input + "&7]";
    }

    public String prettyFix (String input) {
        return ChatColor.GRAY + "[" + getRGB(input) + ChatColor.GRAY + "]";
    }

    public Location getSafePlace(Location loc) {
        Block b;
        b = loc.getWorld().getHighestBlockAt(loc);
        for (double X = loc.getX(); X <= X+20; X++) {
            for (double Z = loc.getZ(); Z <= Z+20; Z++) {
                b = loc.getWorld().getHighestBlockAt((int) X, (int) Z);
                while (!b.getType().name().endsWith("AIR") && b.getType() != Material.COMPOSTER && b.getType() != Material.CAULDRON) {
                    b = b.getLocation().clone().add(0,1,0).getBlock();
                }
                if (isSafe(b.getLocation())) {
                    return b.getLocation().add(0.5,0,0.5);
                }
            }
        }
        Bukkit.getLogger().info("Couldn't find a safe block!");
        return null;
    }

    public static boolean isSafe(Location loc) {
        if (loc == null) return false;
        if (loc.getY() < 1) return false;
        if (!loc.getBlock().getType().name().endsWith("AIR")) return false;
        if (loc.clone().add(0, -1, 0).getBlock().getType().name().endsWith("AIR")) { return false; }
        return !loc.clone().add(0, -1, 0).getBlock().isLiquid();
    }

    public Player getPlayer(String player) {
        if (Bukkit.getPlayer(player) == null) {
            Bukkit.getLogger().info("GenesisPlot > " + player + " is not an online player!");
            return null;
        }
        return Bukkit.getPlayer(player);
    }

    public void addLogEntry(String str) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(GenesisSMP.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "command-log.txt", true), "utf-8"))) {
            Bukkit.getLogger().info("Attempting to write to " + GenesisSMP.getPlugin().getDataFolder().getAbsolutePath() + "plot-log.txt");
            SimpleDateFormat time = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            writer.write(System.lineSeparator() + "[" + time.format(new Date()) + "] " + str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
