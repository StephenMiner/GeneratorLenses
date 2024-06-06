package me.stephenminer.generatorlenses.transformations;

import me.stephenminer.generatorlenses.Lenses.Lense;
import me.stephenminer.generatorlenses.Main;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

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
                String type = typeAtLoc(loc);
                if (type != null && validType(type)){
                    Lense lense = new Lense(plugin,type);
                    b.setType(lense.runRolls());
                }
            }
        }.runTaskLater(plugin, 2);
    }

    /**
     *
     * @param loc where to check for a lens
     * @return lens type found at loc, null if none
     */
    private String typeAtLoc(Location loc){
        String sLoc = plugin.fromBLoc(loc);
        return plugin.LensStorage.getConfig().getString("locs." + sLoc + ".type");
    }

    /**
     *
     * @param type lens type
     * @return true if type is found in LensType yml file, false otherwise
     */
    private boolean validType(String type){
        return plugin.LenseTypes.getConfig().contains("lenses." + type);
    }



    @EventHandler
    public void migrateOldConfig(BlockFormEvent event){
        Block block = event.getBlock().getRelative(BlockFace.UP);
        long key = Main.getBlockKey(block.getX(),block.getY(),block.getZ());
        migrateFromKey(key, block.getLocation(),null);
    }

    @EventHandler
    public void migrateOnBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        long key = Main.getBlockKey(block.getX(),block.getY(),block.getZ());
        migrateFromKey(key, block.getLocation(),null);
    }

    /**
     *
     * @param key block key (from loc)
     * @param loc location to migrate
     * @param owner unused
     * @return true if anything was migrated, false if otherwise
     */
    private boolean migrateFromKey(long key, Location loc, Player owner){
        if (!plugin.LensStorage.getConfig().contains("lenses")) return false;
        Set<String> types = plugin.LensStorage.getConfig().getConfigurationSection("lenses").getKeys(false);
        String type = null;
        for (String entry : types){
            if (plugin.LensStorage.getConfig().contains("lenses." + entry + "." + loc.getWorld().getName() + "." + key)){
                createLenseConfiguration(entry,loc,owner);
                type = entry;
                break;
            }
        }
        if (type != null) {
            Lense lense = new Lense(plugin, type);
            lense.removeLegacyLocation(loc);
            return true;
        }else return false;
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
