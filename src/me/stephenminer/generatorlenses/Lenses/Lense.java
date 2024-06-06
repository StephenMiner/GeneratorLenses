package me.stephenminer.generatorlenses.Lenses;

import me.stephenminer.generatorlenses.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Lense {
    private String lense;
    private Main plugin;

    public Lense(Main plugin, String type){
        this.lense = type;
        this.plugin = plugin;
    }


    public boolean checkEistence(){
        if (plugin.LenseTypes.getConfig().contains("lenses." + lense))
            return true;
        return false;
    }

    private int getBounds(String material){
       return plugin.LenseTypes.getConfig().getInt("lenses." + lense + ".outputs." + material + ".chance");
    }

    public ItemStack buildItemStack(){
        String s = plugin.LenseTypes.getConfig().getString("lenses." + lense + ".material");
        Material baseMaterial = Material.matchMaterial(s);
        if (baseMaterial == null) {
            baseMaterial = Material.DIRT;
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error building ItemStack material for " + lense + " lense. The material " + s + "" +
                    " is inputted incorrectly or doesn't exist!");
        }
        ItemStack item = new ItemStack(baseMaterial);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + lense + " lense");
        List<String> lore = new ArrayList<>();
        List<String> pList = plugin.LenseTypes.getConfig().getStringList("lenses." + lense + ".lore");
        for (String key : pList){
            lore.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        lore.add(ChatColor.ITALIC + "GeneratorLens");
        lore.add(ChatColor.ITALIC + "Lens type is " + lense);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public Material runRolls(){
        Random r = new Random();
        int i = r.nextInt(100);
        List<String> options = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();
        for (String s : plugin.LenseTypes.getConfig().getConfigurationSection("lenses." + lense + ".outputs").getKeys(false)){
            Material mat = Material.matchMaterial(s);
            if (mat == null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error rolling material for " + lense + " lense. The material "
                        + s + " either isn't inputted properly or doesn't exist! Re-rolling output!");
                return runRolls();
            }
            if (i <= getBounds(s)) {
                options.add(s);
                numbers.add(getBounds(s));
            }
        }
        if (options.size() < 1)
            return Material.COBBLESTONE;
        Collections.sort(numbers);
        List<String> rList = new ArrayList<>();
        for (String key : options){
            int n = getBounds(key);
            if(numbers.get(0) == n){
                rList.add(key);
            }
        }
        if (rList.size() < 1)
            return Material.COBBLESTONE;
        Material m = Material.matchMaterial(rList.get(r.nextInt(rList.size())));
        return m;
    }
    public void saveLocation(Location location){
        long l = Main.getBlockKey(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        plugin.LensStorage.getConfig().set("lenses." + lense + "." + location.getWorld().getName() + "."+l, l);
        plugin.LensStorage.saveConfig();
    }
    public void removeLocation(Location location){
        long l = Main.getBlockKey(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        plugin.LensStorage.getConfig().set("lenses." + lense + "." + location.getWorld().getName() + "." + l, null);
        plugin.LensStorage.saveConfig();
    }
    public void removeLegacyLocation(Location location){
        long l = Main.getBlockKey(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        plugin.LensStorage.getConfig().set("lenses." + lense + "." + location.getWorld().getName() + "." + l, null);
        plugin.LensStorage.saveConfig();
    }
}
