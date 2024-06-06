package me.stephenminer.generatorlenses.inventoryfunctions;

import me.stephenminer.generatorlenses.Lenses.Lense;
import me.stephenminer.generatorlenses.Lenses.Recipe;
import me.stephenminer.generatorlenses.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class EditLens implements Listener {
    private Main plugin;
    public EditLens(Main plugin){
        this.plugin = plugin;
    }

    HashMap<UUID, Material> matMap = new HashMap<UUID, Material>();

    @EventHandler
    public void invClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory()==null)
            return;
        Inventory inv = event.getClickedInventory();
        if (event.getCurrentItem() == null)
            return;
        if (!player.hasPermission(Main.genEdit + ".lens"))
            return;
        String title= event.getView().getTitle();
        for (String key : plugin.LenseTypes.getConfig().getConfigurationSection("lenses").getKeys(false)){
            if (title.contains(key + " menu")){
                event.setCancelled(true);
                LensMethods lm = new LensMethods(plugin, key);
                ItemStack item = event.getCurrentItem();
                if (item == null)
                    return;
                if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
                    return;
                String name = item.getItemMeta().getDisplayName();
                if (name.contains("Lens Drops"))
                    player.openInventory(lm.dropsMenu());
                if (name.contains("Crafting Recipe")){
                    RecipeMethods rm = new RecipeMethods(plugin, key);
                    player.openInventory(rm.mainScreen());
                }

                return;
            }


            if (matMap.containsKey(player.getUniqueId())){
                if (title.contains(key + " drop " + matMap.get(player.getUniqueId()).name())){
                    event.setCancelled(true);
                    ItemStack item = event.getCurrentItem();
                    if (item.getType().equals(Material.BARRIER)){
                        player.closeInventory();
                        return;
                    }
                    if (item.getItemMeta() == null || !item.getItemMeta().hasDisplayName())
                        return;
                    String name = item.getItemMeta().getDisplayName();
                    if (name.contains("Roll Chance")){
                        LensMethods lm = new LensMethods(plugin, key);
                        player.openInventory(lm.chanceMenu(matMap.get(player.getUniqueId())));
                        return;
                    }
                }
                if (title.contains(key + " material chances")){
                    event.setCancelled(true);
                    ItemStack item = event.getCurrentItem();
                    LensMethods lm = new LensMethods(plugin, key);
                    if (item.getType().equals(Material.BARRIER)){
                        player.openInventory(lm.dropsMenu());
                        return;
                    }
                    if (item.getItemMeta() == null || !item.getItemMeta().hasDisplayName())
                        return;
                    String name = item.getItemMeta().getDisplayName();
                    switch (name){
                        case "-100":
                            lm.saveChance(player, matMap.get(player.getUniqueId()), inv, -100);
                            return;
                        case "-10":
                            lm.saveChance(player, matMap.get(player.getUniqueId()), inv, -10);
                            return;
                        case "-1":
                            lm.saveChance(player, matMap.get(player.getUniqueId()), inv, -1);
                            return;
                        case "+100":
                            lm.saveChance(player, matMap.get(player.getUniqueId()), inv, 100);
                            return;
                        case "+10":
                            lm.saveChance(player, matMap.get(player.getUniqueId()), inv, 10);
                            return;
                        case "+1":
                            lm.saveChance(player, matMap.get(player.getUniqueId()), inv, 1);
                            return;
                    }
                }
            }


            if (title.contains(key + " drops")){
                if (!event.getClickedInventory().getType().equals(InventoryType.PLAYER))
                    if (event.getSlot() > 44 ){
                        event.setCancelled(true);
                        return;
                    }

                LensMethods lm = new LensMethods(plugin, key);
                ItemStack item = event.getCurrentItem();
                if (item.getType().equals(Material.BARRIER)) {
                    event.setCancelled(true);
                    player.openInventory(lm.mainMenu());
                    return;
                }
                if (event.getClick().isRightClick()){
                    if (!event.getClickedInventory().equals(event.getView().getTopInventory()))return;
                    Material mat = item.getType();
                    player.openInventory(lm.sideMenu(mat));
                    matMap.put(player.getUniqueId(), mat);
                }
            }
        }
    }



    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        if (!player.hasPermission(Main.genEdit + ".lens"))
            return;
        Inventory inv = event.getInventory();
        String title = event.getView().getTitle();
        for (String lens : plugin.LenseTypes.getConfig().getConfigurationSection("lenses").getKeys(false)){
            if (title.contains(lens + " drops")) {
                LensMethods lm = new LensMethods(plugin, lens);
                lm.saveDrops(inv);
                return;
            }
        }
    }
}
