package me.stephenminer.generatorlenses.commands;

import me.stephenminer.generatorlenses.Lenses.Lense;
import me.stephenminer.generatorlenses.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

public class GiveCmd implements CommandExecutor, TabCompleter {
    private final Main plugin;
    public GiveCmd(){
        this.plugin = JavaPlugin.getPlugin(Main.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
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
            Lense l = new Lense(plugin, lens);
            if (l.checkEistence()){
                player.getInventory().addItem(l.buildItemStack());
                return true;
            }else{
                player.sendMessage(ChatColor.RED + "This lens type does not exist!");
                return false;
            }
        }
        return false;
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
