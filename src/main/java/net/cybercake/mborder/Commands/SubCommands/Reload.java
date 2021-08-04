package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.DefaultItems;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Reload extends SubCommand {

    public Reload() {
        super("reload", "mborder.command.reload", "Reloads the configuration file.", "/mborder reload", new String[]{"rl"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        long mss = System.currentTimeMillis();
        Exception ex = null;
        try { Main.getPlugin().reloadConfig(); } catch (Exception e) { ex = e; }
        try { DefaultItems.reloadItems(); } catch (Exception e) { ex = e; }
        long msAfter = System.currentTimeMillis() - mss;
        if(ex != null) {
            sender.sendMessage(Utils.chat("&cFailed to reload the configuration file! Try again later: &8" + ex.toString()));
            for(StackTraceElement element : ex.getStackTrace()) {
                Bukkit.getLogger().severe(element.toString());
            }
        }else{
            if(!Main.getMainConfig().getBoolean("enabled")) { sender.sendMessage(Utils.chat("&6It seems as though you have set &a'enabled' &6to &cfalse&6. &d&l&nYOU MUST RESTART THE SERVER FOR THIS OPTION TO TAKE EFFECT!"));}
            sender.sendMessage(Utils.chat("&aSuccessfully reloaded the configuration files in &f" + msAfter + "&fms&a!"));
        }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return CommandManager.emptyList;
    }

}
