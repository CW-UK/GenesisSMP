package eu.genesismc.genesissmp.managers;

import eu.genesismc.genesissmp.GenesisSMP;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class InventoryManager {

    static Plugin plugin = GenesisSMP.getPlugin();

    public void saveInventory(Player p) throws IOException {
        File f = new File("plugins/GenesisSMP/inventories/", p.getName() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set("inventory.armor", p.getInventory().getArmorContents());
        c.set("inventory.content", p.getInventory().getContents());
        c.save(f);
    }

    @SuppressWarnings("unchecked")
    public void restoreInventory(Player p) throws IOException {
        Bukkit.getLogger().info("[GenesisSMP] Inventory restore request received for " + p.getName());
        File f = new File("plugins/GenesisSMP/inventories/", p.getName() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        try {
            ItemStack[] content = ((List<ItemStack>) c.get("inventory.armor")).toArray(new ItemStack[0]);
            p.getInventory().setArmorContents(content);
            content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
            p.getInventory().setContents(content);
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    f.delete();
                }
            }, 5L);
        } catch (NullPointerException e) {
            Bukkit.getLogger().info("[GenesisSMP] Inventory was empty when trying to restore " + p.getName());
        } catch (Exception e) {
            Bukkit.getLogger().info("[GenesisSMP] Something else went wrong..");
        }
    }

}
