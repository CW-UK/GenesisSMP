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
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;

public class BlockPlace implements Listener {

    FileConfiguration config = GenesisSMP.getPlugin().getConfig();

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent event) {

        // TODO: Add bypass per block, e.g. blocklimit.bypass.all
        //       or blocklimit.bypass.SPAWNER etc.
        if (!config.getBoolean("BlockChunkLimit.enabled")) { return; }
        final Player player = event.getPlayer();
        if (player.hasPermission("genesissmp.blocklimit.bypass") || player.isOp()) { return; }
        final Block block = event.getBlock();
        if (!config.contains("BlockChunkLimit.blocks." + block.getType())) { return; }

        final Material type = block.getType();
        final Chunk chunk = block.getChunk();
        final int currentLimit = config.getInt("BlockChunkLimit.blocks." + event.getBlock().getType());
        //final long chunkAmount = check(chunk, type);
        final long chunkAmount = Arrays.stream(chunk.getTileEntities()).filter(te -> te.getType() == type).count();

        if (chunkAmount + 1 > currentLimit) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "A per-chunk limit of " + ChatColor.WHITE + currentLimit + ChatColor.RED + " is in place for the block " + ChatColor.WHITE + type);
        }

    }

    /*public static long check(final Chunk chunk, final Material material) {
        return Arrays.stream(chunk.getTileEntities())
                .filter(te -> te.getType() == material)
                .count();
    }*/
}
