package me.stephenminer.generatorlenses.transformations;

import me.stephenminer.generatorlenses.Lenses.Lense;
import me.stephenminer.generatorlenses.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class LenseActions implements Listener {
    private Main plugin;
    public LenseActions(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void blockform(BlockFormEvent event){
        new BukkitRunnable(){
            @Override
            public void run(){
                if (event.isCancelled())return;
                Block b = event.getBlock();
                if(!(b.getType().equals(Material.COBBLESTONE) || b.getType().equals(Material.STONE))) {
                    return;
                }
                Block up = b.getRelative(BlockFace.UP);
                Location loc = up.getLocation();
                long l = Main.getBlockKey(loc.getBlockX(),loc.getBlockY(), loc.getBlockZ());
                if (!plugin.LensStorage.getConfig().contains("lenses"))
                    return;
                String type = null;
                for (String key : plugin.LensStorage.getConfig().getConfigurationSection("lenses").getKeys(false)){
                    if (plugin.LensStorage.getConfig().contains("lenses."+ key + "." + loc.getWorld().getName() + "." + l))
                        type = key;
                }
                if (type == null)
                    return;
                Lense lens = new Lense(plugin, type);
                b.setType(lens.runRolls());
            }
        }.runTaskLater(plugin, 2);
    }
}
