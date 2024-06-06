package me.stephenminer.generatorlenses.Lenses;

import me.stephenminer.generatorlenses.Main;
import org.bukkit.Material;
import java.util.List;

public class CreateLens {
    private Main plugin;
    private String type;
    private Material mat;
    private String name;
    private List<String> lore;
    public CreateLens(Main plugin, String type, Material mat, String name, List<String> lore){
        this.plugin = plugin;
        this.type = type;
        this.mat = mat;
        this.name = name;
        this.lore = lore;
        saveItem();
    }

    public void saveItem(){
        plugin.LenseTypes.getConfig().set("lenses." + type + ".material", mat.name());
        plugin.LenseTypes.getConfig().set("lenses." + type + ".item-name", name);
        plugin.LenseTypes.getConfig().set("lenses." + type + ".lore", lore);
        plugin.Recipes.getConfig().getConfigurationSection("recipes").createSection(type);
        plugin.LenseTypes.saveConfig();

    }
}
