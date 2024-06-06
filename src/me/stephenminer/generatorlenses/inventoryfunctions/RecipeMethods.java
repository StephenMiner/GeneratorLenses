package me.stephenminer.generatorlenses.inventoryfunctions;

import me.stephenminer.generatorlenses.Lenses.Recipe;
import me.stephenminer.generatorlenses.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class RecipeMethods {
    private Main plugin;
    private String lens;
    public RecipeMethods(Main plugin, String lens){
        this.plugin = plugin;
        this.lens = lens;
    }

    public Inventory mainScreen(){
        Items items = new Items();
        Inventory inv = Bukkit.createInventory(null, 54, "Recipe for " + lens + " lens");
        for (int i = 0; i <= 53; i++){
            inv.setItem(i,items.filler());
        }
        for (int i = 21; i <= 23; i++){
            inv.setItem(i, null);
        }
        for (int i = 30; i <= 32; i++){
            inv.setItem(i, null);
        }
        for (int i = 39; i <= 41; i++){
            inv.setItem(i, null);
        }
        inv.setItem(49, items.back());
        setRecipeLayOut(inv);
        return inv;
    }

    public void setRecipeLayOut(Inventory inv){
        if (inv.getSize() < 50)
            return;
        Recipe r = new Recipe(plugin, lens);
        if (r.hasIngredient("a"))
            inv.setItem(21, r.getIngrediant("a"));
        if (r.hasIngredient("b"))
            inv.setItem(22, r.getIngrediant("b"));
        if (r.hasIngredient("c"))
            inv.setItem(23, r.getIngrediant("c"));
        if (r.hasIngredient("d"))
            inv.setItem(30, r.getIngrediant("d"));
        if (r.hasIngredient("e"))
            inv.setItem(31, r.getIngrediant("e"));
        if (r.hasIngredient("f"))
            inv.setItem(32, r.getIngrediant("f"));
        if (r.hasIngredient("g"))
            inv.setItem(39, r.getIngrediant("g"));
        if (r.hasIngredient("h"))
            inv.setItem(40, r.getIngrediant("h"));
        if (r.hasIngredient("i"))
            inv.setItem(41, r.getIngrediant("i"));

    }
    public boolean craftingSlot(int i){
        switch (i){
            case 21:
                return true;
            case 22:
                return true;
            case 23:
                return true;
            case 30:
                return true;
            case 31:
                return true;
            case 32:
                return true;
            case 39:
                return true;
            case 40:
                return true;
            case 41:
                return true;
        }
        return false;
    }

    public void saveRecipe(Inventory inv){
        plugin.Recipes.getConfig().set("recipes." + lens + ".a", inv.getItem(21));
        plugin.Recipes.getConfig().set("recipes." + lens + ".b", inv.getItem(22));
        plugin.Recipes.getConfig().set("recipes." + lens + ".c", inv.getItem(23));
        plugin.Recipes.getConfig().set("recipes." + lens + ".d", inv.getItem(30));
        plugin.Recipes.getConfig().set("recipes." + lens + ".e", inv.getItem(31));
        plugin.Recipes.getConfig().set("recipes." + lens + ".f", inv.getItem(32));
        plugin.Recipes.getConfig().set("recipes." + lens + ".g", inv.getItem(39));
        plugin.Recipes.getConfig().set("recipes." + lens + ".h", inv.getItem(40));
        plugin.Recipes.getConfig().set("recipes." + lens + ".i", inv.getItem(41));
        plugin.Recipes.saveConfig();
    }
}
