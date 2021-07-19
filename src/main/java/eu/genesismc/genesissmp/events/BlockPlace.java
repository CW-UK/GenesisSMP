package eu.genesismc.genesissmp.events;

import eu.genesismc.genesissmp.GenesisSMP;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;

public class BlockPlace implements Listener {

    //GenesisSMP plugin;
    //public BlockPlace(GenesisSMP plugin) {
    //    this.plugin = plugin;
    //}

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent event) {

        FileConfiguration config = GenesisSMP.getInstance().config;
        if (!config.getBoolean("BlockChunkLimit.enabled")) { return; }

        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Material type = block.getType();

        if (player.getWorld().getName().equals("smphub")) { return; }
        if (player.hasPermission("genesissmp.blocklimit.bypass.all") || player.isOp()) { return; }
        if (!config.contains("BlockChunkLimit.blocks." + type) || player.hasPermission("genesissmp.blocklimit.bypass." + type)) { return; }

        final Chunk chunk = block.getChunk();
        final int currentLimit = config.getInt("BlockChunkLimit.blocks." + type);
        final long chunkAmount = Arrays.stream(chunk.getTileEntities()).filter(te -> te.getType() == type).count();

        if (chunkAmount > currentLimit) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "A limit of " + ChatColor.WHITE + currentLimit + ChatColor.RED + " per chunk is in place for " + ChatColor.WHITE + type.toString().toLowerCase() + "s");
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onDropper(BlockDispenseEvent e) {

        FileConfiguration config = GenesisSMP.getInstance().config;
        if (!config.getBoolean("BlockChunkLimit.dispensing-per-chunk-enabled")) { return; }

        final Chunk chunk = e.getBlock().getChunk();
        final int currentLimit = config.getInt("BlockChunkLimit.dispensing-per-chunk");
        final long chunkAmount = Arrays.stream(chunk.getTileEntities()).filter(te -> te.getType() == Material.DROPPER || te.getType() == Material.DISPENSER).count();

        if (chunkAmount >= currentLimit) {
            e.setCancelled(true);
            //Bukkit.getLogger().info("Cancelling " + chunkAmount + "(" + currentLimit + ") dispensing blocks in chunk " + chunk);
        }

    }


}
