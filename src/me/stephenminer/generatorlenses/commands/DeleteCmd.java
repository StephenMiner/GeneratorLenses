package me.stephenminer.generatorlenses.commands;

import me.stephenminer.generatorlenses.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeleteCmd implements CommandExecutor, TabCompleter {
    private final Main plugin;
    public DeleteCmd(){
        this.plugin = JavaPlugin.getPlugin(Main.class);

    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
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
       // plugin.LensStorage.getConfig().set("lenses." + name, null);
       // plugin.LensStorage.saveConfig();
        removeCurrentLenses(name);
        plugin.LenseTypes.getConfig().set("lenses." + name, null);
        plugin.LenseTypes.saveConfig();
        plugin.Recipes.getConfig().set("recipes." + name, null);
        plugin.Recipes.saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Deleted lens "+ name);
        return true;
    }

    private void removeCurrentLenses(String type){
        List<String> toRemove = new ArrayList<>();
        Set<String> locs = plugin.LensStorage.getConfig().getConfigurationSection("locs").getKeys(false);
        for (String loc : locs){
            String entry = plugin.LensStorage.getConfig().getString("locs." + loc + ".type");
            if (entry != null && entry.equalsIgnoreCase(type)) toRemove.add(loc);
        }

        for (String loc : toRemove){
            plugin.LensStorage.getConfig().set("locs." + loc, null);
        }
        plugin.LensStorage.saveConfig();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
        int size = args.length;
        if (size == 1) return types(args[0]);
        return null;
    }


    private List<String> types(String match){
        if (!plugin.LenseTypes.getConfig().contains("lenses")) return null;
        Set<String> types = plugin.LenseTypes.getConfig().getConfigurationSection("lenses").getKeys(false);
        return plugin.filter(types, match);
    }
}
