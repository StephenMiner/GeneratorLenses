package me.stephenminer.generatorlenses.inventoryfunctions;

import me.stephenminer.generatorlenses.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class LensMethods {

    private final Main plugin;
    private final String lens;

    public LensMethods(Main plugin, String lens){
        this.plugin = plugin;
        this.lens = lens;
    }

    public Inventory mainMenu(){
        Items items = new Items();
        Inventory inv = Bukkit.createInventory(null, 9, lens + " menu");
        for (int i = 0; i <= 8; i++){
            inv.setItem(i, items.filler());
        }
        inv.setItem(3, items.recipe());
        inv.setItem(4, items.drops());
        return inv;
    }
    public Inventory matMenu(Material mat){
        Items items = new Items();
        Inventory inv = Bukkit.createInventory(null, 9, lens + " editing " + mat.toString());
        for (int i = 0; i <= 8; i++){
            inv.setItem(i, items.filler());
        }
        inv.setItem(4, items.chance(mat));
        return inv;
    }
    public void saveDrops(Inventory inv){
        List<Material> drops = new ArrayList<Material>();
        if (inv.getSize() < 44)
            return;
        if (inv.getContents().length < 1)
            if (plugin.LenseTypes.getConfig().contains("lenses." + lens + ".outputs"))
                return;
        for (int i = 0; i <= 44;i++){
            ItemStack item = inv.getItem(i);
            if (item == null)
                continue;
            if (!item.getType().isBlock())
                continue;
            drops.add(item.getType());
            Material mat = item.getType();
            if (plugin.LenseTypes.getConfig().contains("lenses." + lens + ".outputs." + mat.name()))
                continue;
            plugin.LenseTypes.getConfig().set("lenses." + lens + ".outputs." + mat.name() + ".chance", 50);
            plugin.LenseTypes.saveConfig();
        }
        for (String key : plugin.LenseTypes.getConfig().getConfigurationSection("lenses." + lens + ".outputs").getKeys(false)){
            if (!drops.contains(Material.matchMaterial(key))) {
                plugin.LenseTypes.getConfig().set("lenses." + lens + ".outputs." + key, null);
                plugin.LenseTypes.saveConfig();
            }
        }

    }
    public ItemStack[] getDrops(){
        if (!plugin.LenseTypes.getConfig().contains("lenses." + lens))
            return null;
        List<ItemStack> items = new ArrayList<>();
        if (!plugin.LenseTypes.getConfig().contains("lenses." + lens + ".outputs"))
            return items.toArray(new ItemStack[0]);
        for (String key : plugin.LenseTypes.getConfig().getConfigurationSection("lenses." + lens + ".outputs").getKeys(false)){
            Material mat = Material.matchMaterial(key);
            if (mat == null){
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error in LensTypes.yml, attempted to get" +
                        " drops for " + lens + " but found a non-material, " + key);
                mat = Material.RED_STAINED_GLASS;
            }
            items.add(new ItemStack(mat));
        }
        return items.toArray(new ItemStack[0]);
    }
    public Inventory dropsMenu(){
        Items items = new Items();
        Inventory inv = Bukkit.createInventory(null, 54, lens + " drops");
        for (int i = 45; i <= 53; i++){
            inv.setItem(i, items.filler());
        }
        inv.setItem(49, items.back());
        inv.addItem(getDrops());
        return inv;
    }
    public Inventory sideMenu(Material mat){
        Inventory inv = Bukkit.createInventory(null, 9, lens + " drop " + mat.name());
        Items items = new Items();
        for (int i = 0; i <= 8; i++){
            inv.setItem(i, items.filler());
        }
        inv.setItem(8, items.back());
        inv.setItem(4, items.chance(mat));
        return inv;
    }
    public Inventory chanceMenu(Material mat){
        Items items = new Items();
        Inventory inv = Bukkit.createInventory(null, 9, lens + " material chances");
        for (int i = 0; i <= 8; i++){
            inv.setItem(i, items.filler());
        }
        inv.setItem(8, items.back());
        inv.setItem(1, items.changeChance(-1));
        inv.setItem(2, items.changeChance(-5));
        inv.setItem(3, items.changeChance(-10));
        inv.setItem(4, items.currentChance(plugin, lens, mat));
        inv.setItem(5, items.changeChance(1));
        inv.setItem(6, items.changeChance(5));
        inv.setItem(7, items.changeChance(10));
        return inv;
    }

    public void saveChance(Player player, Material mat, Inventory inv, int increment){
        Items items = new Items();
        int i = plugin.LenseTypes.getConfig().getInt("lenses." + lens + ".outputs." + mat.name() + ".chance");
        int sum = increment + i;
        if (sum < 0)
            sum = 0;
        plugin.LenseTypes.getConfig().set("lenses." + lens + ".outputs." + mat.name() + ".chance", sum);
        inv.setItem(4, items.currentChance(plugin, lens, mat));
        player.sendMessage(ChatColor.GREEN + "Changed chance to " + sum + "!");
        plugin.LenseTypes.saveConfig();
        plugin.LenseTypes.reloadConfig();
    }
}
