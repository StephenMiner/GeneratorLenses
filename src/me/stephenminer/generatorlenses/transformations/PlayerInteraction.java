package me.stephenminer.generatorlenses.transformations;

import me.stephenminer.generatorlenses.Lenses.Lense;
import me.stephenminer.generatorlenses.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerInteraction implements Listener {
    private Main plugin;
    public PlayerInteraction(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void place(BlockPlaceEvent event){
        ItemStack item = event.getItemInHand();
        if (!item.hasItemMeta())
            return;
        if (!item.getItemMeta().hasLore())
            return;
        if (event.isCancelled()) return;
        List<String> lore = item.getItemMeta().getLore();
        String type = null;
        for (String types : plugin.LenseTypes.getConfig().getConfigurationSection("lenses").getKeys(false)) {
            for (String key : lore){
                if (key.contains("Lens type is " + types))
                    type = types;
            }
        }
        if (type == null) return;
        Location loc = event.getBlockPlaced().getLocation();
        Lense lense = new Lense(plugin,type);
        lense.saveLocation(loc);
        Player p = event.getPlayer();
        p.sendMessage(ChatColor.GREEN + "Placed down " + type + " lens!");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if (event.isCancelled())return;
        Block b = event.getBlock();
        Location bLoc = b.getLocation();
        if (!plugin.LensStorage.getConfig().contains("locs"))
            return;
        String sLoc = plugin.fromBLoc(bLoc);
        if (plugin.LensStorage.getConfig().contains("locs." + sLoc)){
            Player player = event.getPlayer();
            String type = plugin.LensStorage.getConfig().getString("locs." + sLoc + ".type");
            Lense lense = new Lense(plugin, type);
            ItemStack item = lense.buildItemStack();
            event.setDropItems(false);
            player.getWorld().dropItemNaturally(bLoc, item);
            player.sendMessage(ChatColor.GREEN + "Removed lens " + type + "!");
            lense.removeLocation(bLoc);
            lense.removeLegacyLocation(bLoc);
            event.setDropItems(false);
            event.setExpToDrop(0);
        }
    }

    /**
     * Prevents lenses from being blown up
     * @param event
     */
    @EventHandler
    public void onExplode(BlockExplodeEvent event){
        if (!plugin.LensStorage.getConfig().contains("locs") || event.isCancelled())
            return;
        for (Block b : event.blockList()){
            Location loc = b.getLocation();
            String sLoc = plugin.fromBLoc(loc);
            if (plugin.LensStorage.getConfig().contains("locs." + sLoc)){
                event.blockList().remove(b);
            }
        }
    }
    /**
     * Prevents lenses from being blown up
     * @param event
     */
    @EventHandler
    public void onEExplode(EntityExplodeEvent event){
        if (!plugin.LensStorage.getConfig().contains("locs") || event.isCancelled())
            return;
        for (Block b : event.blockList()){
            Location loc = b.getLocation();
            String sLoc = plugin.fromBLoc(loc);
            if (plugin.LensStorage.getConfig().contains("locs." + sLoc)){
                event.blockList().remove(b);
            }
        }
    }

    /**
     * Creates a new lens storage entry
     * @param type type of lense
     * @param loc location of lens
     * @param owner unused
     *
     */
    private void createLenseConfiguration(String type, Location loc, Player owner){
        String sLoc = plugin.fromBLoc(loc);
        plugin.LensStorage.getConfig().set("locs." + sLoc + ".type", type);
        if (owner != null)
            plugin.LensStorage.getConfig().set("locs." + sLoc + ".placer", owner.getUniqueId());
        plugin.LensStorage.saveConfig();
    }

}
