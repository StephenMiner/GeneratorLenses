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
        int roll = r.nextInt(1000);
        HashMap<Integer, List<Material>> rollOptions = loadRollOptions();
        if (rollOptions.size() < 1) return Material.COBBLESTONE;
        List<Integer> numbers = new ArrayList<>(rollOptions.keySet());
        Collections.sort(numbers);
        for (int chance : numbers){
            if (roll <= chance){
                List<Material> potential = rollOptions.get(chance);
                if (potential.isEmpty()) return Material.COBBLESTONE;
                else if (potential.size() < 2) return potential.get(0);
                else return potential.get(r.nextInt(potential.size()));
            }
        }
        return Material.COBBLESTONE;
    }

    private HashMap<Integer, List<Material>> loadRollOptions(){
        HashMap<Integer, List<Material>> rollOptions = new HashMap<>();
        Set<String> mats = plugin.LenseTypes.getConfig().getConfigurationSection("lenses." + lense +".outputs").getKeys(false);
        for (String sMat : mats){
            Material mat = Material.matchMaterial(sMat);
            if (mat == null){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error rolling material for " + lense + " lense. The material "
                        + sMat + " either isn't inputted properly or doesn't exist! Re-rolling output!");
                continue;
            }
            int chance = getBounds(sMat);
            List<Material> options = rollOptions.getOrDefault(chance, new ArrayList<>());
            options.add(mat);
            rollOptions.put(chance, options);
        }
        return rollOptions;
    }
    public void saveLocation(Location location){
        String sLoc = plugin.fromBLoc(location);
        plugin.LensStorage.getConfig().set("locs." + sLoc + ".type", lense);
        plugin.LensStorage.saveConfig();
    }
    public void removeLocation(Location location){
        String sLoc = plugin.fromBLoc(location);
        plugin.LensStorage.getConfig().set("locs." + sLoc, null);
        plugin.LensStorage.saveConfig();
    }
    public void removeLegacyLocation(Location location){
        long l = Main.getBlockKey(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        plugin.LensStorage.getConfig().set("lenses." + lense + "." + location.getWorld().getName() + "." + l, null);
        plugin.LensStorage.saveConfig();
    }
}
