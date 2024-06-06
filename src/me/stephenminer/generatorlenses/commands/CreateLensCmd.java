package me.stephenminer.generatorlenses.commands;

import me.stephenminer.generatorlenses.Lenses.CreateLens;
import me.stephenminer.generatorlenses.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CreateLensCmd implements CommandExecutor {
    private final Main plugin;
    public CreateLensCmd(){
        this.plugin = JavaPlugin.getPlugin(Main.class);

    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
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
                String[] lore = args[3].split(",");
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
            CreateLens createLens = new CreateLens(plugin, lens, Material.matchMaterial(material),name, tempList);
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
}
