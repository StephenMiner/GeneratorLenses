package me.stephenminer.generatorlenses.inventoryfunctions;

import com.sun.org.apache.bcel.internal.generic.I2F;
import me.stephenminer.generatorlenses.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Recipes implements Listener {
    private Main plugin;
    public Recipes(Main plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void invClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (!player.hasPermission(Main.genEdit + ".recipes"))
            return;
        if (event.getClickedInventory() == null)
            return;

        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER))
            return;
        String title = event.getView().getTitle();
        if (title.contains("Recipe for "))
            for (String key : plugin.Recipes.getConfig().getConfigurationSection("recipes").getKeys(false)){
                if (title.contains("Recipe for " + key + " lens")){
                    RecipeMethods rm = new RecipeMethods(plugin, key);
                    event.setCancelled(true);
                    if (rm.craftingSlot(event.getSlot()))
                        event.setCancelled(false);
                    ItemStack item = event.getCurrentItem();
                    if (item == null)
                        return;
                    if (item.getType().equals(Material.BARRIER)){
                        LensMethods lm = new LensMethods(plugin, key);
                        player.openInventory(lm.mainMenu());
                        return;
                    }
                }
            }
    }

    @EventHandler
    public void invClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        if (!player.hasPermission(Main.genEdit + ".recipes"))
            return;
        String title = event.getView().getTitle();
        if (title.contains("Recipe for ")) {
            for (String key : plugin.LenseTypes.getConfig().getConfigurationSection("lenses").getKeys(false)) {
                if (title.contains("Recipe for " + key + " lens")) {
                    RecipeMethods rm = new RecipeMethods(plugin, key);
                    rm.saveRecipe(event.getInventory());
                }
            }
        }
    }

}
