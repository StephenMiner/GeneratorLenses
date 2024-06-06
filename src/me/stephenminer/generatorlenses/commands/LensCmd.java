package me.stephenminer.generatorlenses.commands;

import me.stephenminer.generatorlenses.Main;
import me.stephenminer.generatorlenses.inventoryfunctions.LensMethods;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

public class LensCmd implements CommandExecutor, TabCompleter {
    private final Main plugin;
    public LensCmd(){
        this.plugin = JavaPlugin.getPlugin(Main.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
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
        if (!plugin.LenseTypes.getConfig().contains("lenses." + args[0])){
            player.sendMessage(ChatColor.RED + args[0] + " isn't a lens!");
            return false;
        }
        String lens = args[0];
        LensMethods lm = new LensMethods(plugin, lens);
        player.openInventory(lm.mainMenu());
        return true;
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
