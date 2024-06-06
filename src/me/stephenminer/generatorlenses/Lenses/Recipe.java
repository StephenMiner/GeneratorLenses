package me.stephenminer.generatorlenses.Lenses;

import me.stephenminer.generatorlenses.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;


public class Recipe {
    private Main plugin;
    private String lens;
    public Recipe(Main plugin, String lens){
        this.plugin = plugin;
        this.lens = lens;
    }


    public ItemStack getResult(){
        Lense l = new Lense(plugin, lens);
        return l.buildItemStack();
    }

    public boolean hasIngredient(String s){
        return plugin.Recipes.getConfig().contains("recipes." + lens + "." + s);
    }


    public ItemStack getIngrediant(String s){
        if (!plugin.Recipes.getConfig().contains("recipes." + lens))
            return null;
        switch (s){
            case "a":
                ItemStack a = plugin.Recipes.getConfig().getItemStack("recipes." + lens + ".a");
                if (a == null) {
                    String key = plugin.Recipes.getConfig().getString("recipes." + lens + ".a");
                    a = new ItemStack(Material.matchMaterial(key));
                    if (a == null)
                        a = new ItemStack(Material.AIR);
                    return a;
                }
                return a;
            case "b":
                ItemStack b = plugin.Recipes.getConfig().getItemStack("recipes." + lens + ".b");
                if (b == null) {
                    String key = plugin.Recipes.getConfig().getString("recipes." + lens + ".b");
                    b = new ItemStack(Material.matchMaterial(key));
                    if (b == null)
                        b = new ItemStack(Material.AIR);
                    return b;
                }
                return b;
            case "c":
                ItemStack c = plugin.Recipes.getConfig().getItemStack("recipes." + lens + ".c");
                if (c == null) {
                    String key = plugin.Recipes.getConfig().getString("recipes." + lens + ".c");
                    c = new ItemStack(Material.matchMaterial(key));
                    if (c == null)
                        c = new ItemStack(Material.AIR);
                    return c;
                }
                return c;
            case "d":
                ItemStack d = plugin.Recipes.getConfig().getItemStack("recipes." + lens + ".d");
                if (d == null) {
                    String key = plugin.Recipes.getConfig().getString("recipes." + lens + ".d");
                    d = new ItemStack(Material.matchMaterial(key));
                    if (d == null)
                        d = new ItemStack(Material.AIR);
                    return d;
                }
                return d;
            case "e":
                ItemStack e = plugin.Recipes.getConfig().getItemStack("recipes." + lens + ".e");
                if (e == null) {
                    String key = plugin.Recipes.getConfig().getString("recipes." + lens + ".e");
                    e = new ItemStack(Material.matchMaterial(key));
                    if (e == null)
                        e = new ItemStack(Material.AIR);
                    return e;
                }
                return e;
            case "f":
                ItemStack f = plugin.Recipes.getConfig().getItemStack("recipes." + lens + ".f");
                if (f == null) {
                    String key = plugin.Recipes.getConfig().getString("recipes." + lens + ".f");
                    f = new ItemStack(Material.matchMaterial(key));
                    if (f == null)
                        f = new ItemStack(Material.AIR);
                    return f;
                }
                return f;
            case "g":
                ItemStack g = plugin.Recipes.getConfig().getItemStack("recipes." + lens + ".g");
                if (g == null) {
                    String key = plugin.Recipes.getConfig().getString("recipes." + lens + ".g");
                    g = new ItemStack(Material.matchMaterial(key));
                    if (g == null)
                        g = new ItemStack(Material.AIR);
                    return g;
                }
                return g;
            case "h":
                ItemStack h = plugin.Recipes.getConfig().getItemStack("recipes." + lens + ".h");
                if (h == null) {
                    String key = plugin.Recipes.getConfig().getString("recipes." + lens + ".h");
                    h = new ItemStack(Material.matchMaterial(key));
                    if (h == null)
                        h = new ItemStack(Material.AIR);
                    return h;
                }
                return h;
            case "i":
                ItemStack i = plugin.Recipes.getConfig().getItemStack("recipes." + lens + ".i");
                if (i == null) {
                    String key = plugin.Recipes.getConfig().getString("recipes." + lens + ".i");
                    i = new ItemStack(Material.matchMaterial(key));
                    if (i == null)
                        i = new ItemStack(Material.AIR);
                    return i;
                }
                return i;
        }
        return null;
    }

    public String setRow(String s1, String s2, String s3){
        if (hasIngredient(s1) && hasIngredient(s2) && hasIngredient(s3))
            return s1+s2+s3;
        if (hasIngredient(s1) && hasIngredient(s2) && !hasIngredient(s3))
            return s1 + s2 + " ";
        if (!hasIngredient(s1) && hasIngredient(s2) && hasIngredient(s3))
            return " " + s2+s3;
        if (hasIngredient(s1) && !hasIngredient(s2) && hasIngredient(s3))
            return s1+" " + s3;
        if (hasIngredient(s1) && !hasIngredient(s2) && !hasIngredient(s3))
            return s1 + "  ";
        if (!hasIngredient(s1) && hasIngredient(s2) && !hasIngredient(s3))
            return " " + s2 + " ";
        if (!hasIngredient(s1) && !hasIngredient(s2) && hasIngredient(s3))
            return "  " + s3;
        return "   ";
    }
    private boolean hasRecipe(){
        int i = 0;
        if (hasIngredient("a"))
            i++;
        if (hasIngredient("b"))
            i++;
        if (hasIngredient("c"))
            i++;
        if (hasIngredient("d"))
            i++;
        if (hasIngredient("e"))
            i++;
        if (hasIngredient("f"))
            i++;
        if (hasIngredient("g"))
            i++;
        if (hasIngredient("h"))
            i++;
        if (hasIngredient("i"))
            i++;
        return i >= 1;
    }

    public void createRecipe(String lens){
        if (!hasRecipe())
            return;
        NamespacedKey key = new NamespacedKey(plugin, lens);
        ShapedRecipe recipe = new ShapedRecipe(key, getResult());
        recipe.shape(setRow("a","b","c"), setRow("d","e","f"), setRow("g", "h", "i"));
        if (hasIngredient("a"))
            recipe.setIngredient('a', new RecipeChoice.ExactChoice(getIngrediant("a")));
        if (hasIngredient("b"))
            recipe.setIngredient('b', new RecipeChoice.ExactChoice(getIngrediant("b")));
        if (hasIngredient("c"))
            recipe.setIngredient('c', new RecipeChoice.ExactChoice(getIngrediant("c")));
        if (hasIngredient("d"))
            recipe.setIngredient('d', new RecipeChoice.ExactChoice(getIngrediant("d")));
        if (hasIngredient("e"))
            recipe.setIngredient('e', new RecipeChoice.ExactChoice(getIngrediant("e")));
        if (hasIngredient("f"))
            recipe.setIngredient('f', new RecipeChoice.ExactChoice(getIngrediant("f")));
        if (hasIngredient("g"))
            recipe.setIngredient('g', new RecipeChoice.ExactChoice(getIngrediant("g")));
        if (hasIngredient("h"))
            recipe.setIngredient('h', new RecipeChoice.ExactChoice(getIngrediant("h")));
        if (hasIngredient("i"))
            recipe.setIngredient('i', new RecipeChoice.ExactChoice(getIngrediant("i")));
        Bukkit.addRecipe(recipe);
    }



}
