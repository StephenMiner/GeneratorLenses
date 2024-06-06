package me.stephenminer.generatorlenses.commands;

import me.stephenminer.generatorlenses.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCmd implements CommandExecutor {
    private final Main plugin;
    public ReloadCmd() {
        this.plugin = JavaPlugin.getPlugin(Main.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (!p.hasPermission(Main.genCmd + ".reload"))
                return false;
        }
        plugin.LensStorage.reloadConfig();
        plugin.LenseTypes.reloadConfig();
        plugin.Recipes.reloadConfig();
        plugin.LensStorage.saveConfig();
        plugin.LenseTypes.saveConfig();
        plugin.Recipes.saveConfig();
        sender.sendMessage("Reloaded config for GeneratorLenses!");
        return true;
    }
}
