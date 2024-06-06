package me.stephenminer.generatorlenses;

import me.stephenminer.generatorlenses.Lenses.Recipe;
import me.stephenminer.generatorlenses.commands.*;
import me.stephenminer.generatorlenses.inventoryfunctions.EditLens;
import me.stephenminer.generatorlenses.inventoryfunctions.Recipes;
import me.stephenminer.generatorlenses.transformations.LenseActions;
import me.stephenminer.generatorlenses.transformations.PlayerInteraction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Main extends JavaPlugin {
    public ConfigFiles LenseTypes;
    public ConfigFiles Recipes;
    public ConfigFiles LensStorage;
    public static String genCmd = "generatorlenses.commands";
    public static String genEdit = "generatorlenses.edit";



    @Override
    public void onEnable(){
        this.LenseTypes = new ConfigFiles(this, "lensetypes");
        this.Recipes = new ConfigFiles(this,"recipes");
        this.LensStorage = new ConfigFiles(this, "lensstorage");
        registerEvents();
        registerRecipes();
        addCommands();
    }
    @Override
    public void onDisable(){
        LenseTypes.saveConfig();
        Recipes.saveConfig();
        LensStorage.saveConfig();

    }

    private void registerRecipes(){
        for (String key : Recipes.getConfig().getConfigurationSection("recipes").getKeys(false)){
            Recipe r = new Recipe(this, key);
            r.createRecipe(key);
        }
    }



    private void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new LenseActions(this), this);
        pm.registerEvents(new PlayerInteraction(this),this);
        pm.registerEvents(new Recipes(this), this);
        pm.registerEvents(new EditLens(this), this);

    }
    private void addCommands(){
        GiveCmd giveCmd = new GiveCmd();
        getCommand("givelens").setExecutor(giveCmd);
        getCommand("givelens").setTabCompleter(giveCmd);

        ReloadCmd reloadCmd = new ReloadCmd();
        getCommand("reloadlens").setExecutor(reloadCmd);

        LensCmd lensCmd = new LensCmd();
        getCommand("lens").setExecutor(lensCmd);
        getCommand("lens").setTabCompleter(lensCmd);

        CreateLensCmd createLensCmd = new CreateLensCmd();
        getCommand("createlens").setExecutor(createLensCmd);

        DeleteCmd deleteCmd = new DeleteCmd();
        getCommand("deletelens").setExecutor(deleteCmd);
        getCommand("deletelens").setTabCompleter(deleteCmd);


    }

    /**
     *
     * @param loc
     * @return String fromatted as world,x,y,z,yaw,pitch
     */
    public String fromLoc(Location loc){
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
    }

    /**
     *
     * @param loc block location preferably
     * @return String formatted as world,x,y,z
     */
    public String fromBLoc(Location loc){
        return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    /**
     *
     * @param str String format as either world,x,y,z or world,x,y,z,yaw,pitch
     * @return Location based on str parameter
     */
    public Location fromString(String str){
        String[] split = str.split(",");
        World world = Bukkit.getWorld(split[0]);
        if (world == null){
            Bukkit.getLogger().warning("Couldnt get world " + split[0] + " because world is unloaded or doesn't exist");
            return null;
        }
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        if (split.length >= 6){
            float yaw = Float.parseFloat(split[4]);
            float pitch = Float.parseFloat(split[5]);
            return new Location( world,x,y,z,yaw,pitch);
        }else return new Location(world,x,y,z);
    }
    public static long getBlockKey(int x, int y, int z) {
        return (long)x & 134217727L | ((long)z & 134217727L) << 27 | (long)y << 54;
    }


    public List<String> filter(Collection<String> base, String match){
        match = match.toLowerCase();
        List<String> filtered = new ArrayList<>();
        for (String entry : base){
            String temp = ChatColor.stripColor(entry).toLowerCase();
            if (temp.contains(match)) filtered.add(entry);
        }
        return filtered;
    }


}
