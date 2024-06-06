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
        String type = "earth";
        for (String types : plugin.LenseTypes.getConfig().getConfigurationSection("lenses").getKeys(false)) {
            for (String key : lore){
                if (key.contains("Type is " + types))
                    type = types;
            }
        }
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
        long l = Main.getBlockKey(bLoc.getBlockX(), bLoc.getBlockY(), bLoc.getBlockZ());
        if (!plugin.LensStorage.getConfig().contains("lenses"))
            return;
        for (String key : plugin.LensStorage.getConfig().getConfigurationSection("lenses").getKeys(false)){
            if (plugin.LensStorage.getConfig().contains("lenses." + key + "." + bLoc.getWorld().getName() + "." + l)){
                Player player = event.getPlayer();
                Lense lense = new Lense(plugin, key);
                ItemStack item = lense.buildItemStack();
                event.setDropItems(false);
                player.getWorld().dropItemNaturally(bLoc, item);
                player.sendMessage(ChatColor.GREEN + "Removed lens " + key + "!");
                lense.removeLocation(bLoc);
                event.setDropItems(false);
                event.setExpToDrop(0);
                return;
            }
        }


    }
    @EventHandler
    public void onExplode(BlockExplodeEvent event){
        for (Block b : event.blockList()){
            Location loc = b.getLocation();
            long l = Main.getBlockKey(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
            if (!plugin.LensStorage.getConfig().contains("lenses"))
                return;
            for (String key : plugin.LensStorage.getConfig().getConfigurationSection("lenses").getKeys(false)){
                if (plugin.LensStorage.getConfig().contains("lenses." + key +"."+ b.getWorld().getName() + "." + l)){
                    if (event.isCancelled())
                        return;
                    event.blockList().remove(b);
                }
            }

        }
    }
    @EventHandler
    public void onEExplode(EntityExplodeEvent event){
        for (Block b : event.blockList()){
            Location loc = b.getLocation();
            long l = Main.getBlockKey(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
            if (!plugin.LensStorage.getConfig().contains("lenses"))
                return;
            for (String key : plugin.LensStorage.getConfig().getConfigurationSection("lenses").getKeys(false)){
                if (plugin.LensStorage.getConfig().contains("lenses." + key +"."+ b.getWorld().getName() + "." + l)){
                    if (event.isCancelled())
                        return;
                    event.blockList().remove(b);
                }
            }
        }
    }

}
