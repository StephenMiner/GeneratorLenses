package me.stephenminer.generatorlenses.inventoryfunctions;

import me.stephenminer.generatorlenses.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Items {
    public ItemStack filler(){
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack back(){
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Back");
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack drops(){
        ItemStack item = new ItemStack(Material.DROPPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Lens Drops");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.ITALIC + "");
        lore.add(ChatColor.ITALIC + "Define what blocks will");
        lore.add(ChatColor.ITALIC + "be generated from the lens");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack chance(Material mat){
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Roll Chance");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.ITALIC + "");
        lore.add(ChatColor.ITALIC + "Defines odds of material generating");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack recipe(){
        ItemStack item = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Crafting Recipe");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.ITALIC + "");
        lore.add(ChatColor.ITALIC + "Defines lens crafting");
        lore.add(ChatColor.ITALIC + "recipe");
        lore.add(ChatColor.RED + "Note, any changes / creation of");
        lore.add(ChatColor.RED + "requires a server restart to implement");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack changeChance(int i){
        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta meta = item.getItemMeta();
        switch (i){
            case -10:
                meta.setDisplayName("-10");
                break;
            case -5:
                meta.setDisplayName("-5");
                break;
            case -1:
                meta.setDisplayName("-1");
                break;
            case 10:
                meta.setDisplayName("+10");
                break;
            case 5:
                meta.setDisplayName("+5");
                break;
            case 1:
                meta.setDisplayName("+1");
                break;
        }
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack currentChance(Main plugin, String lens, Material mat){
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();
        int i = plugin.LenseTypes.getConfig().getInt("lenses." + lens + ".outputs." + mat.name() + ".chance");
        meta.setDisplayName(ChatColor.BLUE + "" + i + "/100 chance");
        Bukkit.broadcastMessage("" + i);
        item.setItemMeta(meta);
        return item;

    }
}
