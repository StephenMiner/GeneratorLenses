package me.stephenminer.generatorlenses;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import me.stephenminer.generatorlenses.Lenses.CreateLens;
import me.stephenminer.generatorlenses.Lenses.Lense;
import me.stephenminer.generatorlenses.Lenses.Recipe;
import me.stephenminer.generatorlenses.inventoryfunctions.EditLens;
import me.stephenminer.generatorlenses.inventoryfunctions.LensMethods;
import me.stephenminer.generatorlenses.inventoryfunctions.Recipes;
import me.stephenminer.generatorlenses.transformations.LenseActions;
import me.stephenminer.generatorlenses.transformations.PlayerInteraction;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("givelens")){
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Sorry, but only players can use this command!");
                return false;
            }
            if (args.length < 1){
                sender.sendMessage(ChatColor.RED + "You need to specify which lens you want to give!");
                return false;
            }
            Player player = (Player) sender;
            if (!player.hasPermission(Main.genCmd + ".give"))
                return false;
            if (args.length == 1){
                String lens = args[0];
                Lense l = new Lense(this, lens);
                if (l.checkEistence()){
                    player.getInventory().addItem(l.buildItemStack());
                    return true;
                }else{
                    player.sendMessage(ChatColor.RED + "This lens type does not exist!");
                    return false;
                }
            }

        }

        if (label.equalsIgnoreCase("reloadconfig")){
            if (sender instanceof Player){
                Player p = (Player) sender;
                if (!p.hasPermission(Main.genCmd + ".reload"))
                    return false;
            }
            LensStorage.reloadConfig();
            LenseTypes.reloadConfig();
            Recipes.reloadConfig();
            LensStorage.saveConfig();
            LenseTypes.saveConfig();
            Recipes.saveConfig();
            sender.sendMessage("Reloaded config for GeneratorLenses!");
            return true;
        }

        if (label.equalsIgnoreCase("lens")){
            if (!(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED + "Sorry! Only players can run this command!");
                return false;
            }
            Player player = (Player) sender;
            if(!player.hasPermission(Main.genCmd + ".editlens")){
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return false;
            }
            if (args.length < 1){
                player.sendMessage(ChatColor.RED + "You need to specify which lens you want to edit!");
                return false;
            }
            if (!LenseTypes.getConfig().contains("lenses." + args[0])){
                player.sendMessage(ChatColor.RED + args[0] + " isn't a lens!");
                return false;
            }
            String lens = args[0];
            LensMethods lm = new LensMethods(this, lens);
            player.openInventory(lm.mainMenu());
            return true;
        }


        if (label.equalsIgnoreCase("createlens")){
            if (sender instanceof Player){
                Player player = (Player) sender;
                if (!player.hasPermission(Main.genCmd + ".editlens")){
                    player.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
                    return false;
                }
            }
            if (args.length >= 3){
                String lens = args[0];
                String material = args[2];
                String name = args[1];
                List<String> tempList = new ArrayList<>();
                if (args.length >=4){
                    List<String> lore = Lists.newArrayList(Splitter.on(",").split(args[3]));
                    for (String key : lore){
                        tempList.add(ChatColor.translateAlternateColorCodes('&', key).replace('_',' '));
                    }
                }
                if (tempList.size() < 1)
                    tempList.add(" ");
                if (Material.matchMaterial(material) == null){
                    sender.sendMessage(ChatColor.RED + "Incorrect information!");
                    sender.sendMessage(ChatColor.RED + "format is /createlens [type name] [item name] [item material name] [lore] (optional)");
                    sender.sendMessage(ChatColor.RED + "to input lore use , for new lines and _ for spaces");
                    return false;
                }
                CreateLens createLens = new CreateLens(this, lens, Material.matchMaterial(material),name, tempList);
                sender.sendMessage(ChatColor.GREEN + "Created a new lens: " + lens);
                return true;
            }
            else{
                sender.sendMessage(ChatColor.RED + "Not enough info! You need to include a lens name, its item name, " +
                        "and its item material, and if you want, its item lore.");
                sender.sendMessage(ChatColor.RED + "format is /createlens [type name] [item name] [item material name] [lore] (optional)");
                sender.sendMessage(ChatColor.RED + "to input lore use , for new lines and _ for spaces");
                return false;
            }
        }

        if (label.equalsIgnoreCase("deletelens")){
            if (sender instanceof Player){
                Player player = (Player)  sender;
                if (!player.hasPermission(Main.genCmd + ".delete")){
                    player.sendMessage(ChatColor.RED + "Sorry! You don't have permission to use this command");
                    return false;
                }
            }
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "You need to specify which lens you want to delete, i.e.");
                sender.sendMessage(ChatColor.RED + "/deletelens earth");
                return false;
            }
            String name = args[0];
            LensStorage.getConfig().set("lenses." + name, null);
            LensStorage.saveConfig();
            LenseTypes.getConfig().set("lenses." + name, null);
            LenseTypes.saveConfig();
            Recipes.getConfig().set("recipes." + name, null);
            Recipes.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Deleted lens "+ name);
            return true;
        }
        return false;
    }


    private void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new LenseActions(this), this);
        pm.registerEvents(new PlayerInteraction(this),this);
        pm.registerEvents(new Recipes(this), this);
        pm.registerEvents(new EditLens(this), this);

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


    public static String convertBlockKey(long key){
        Location loc =
    }
}
