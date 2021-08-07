package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.DefaultItems;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Messages;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Reload extends SubCommand {

    public Reload() {
        super("reload", "mborder.command.reload", Messages.getMessagesCommands("reload.description"), "/mborder reload", new String[]{"rl"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        long mss = System.currentTimeMillis();
        Exception ex = null;
        String exceptionFile = "";
        try { Main.getPlugin().reloadConfig(); } catch (Exception e) { ex = e; exceptionFile = "config.yml"; }
        try { DefaultItems.reloadItems(); } catch (Exception e) { ex = e; exceptionFile = "default-items.yml"; }
        try { Messages.reloadMessages(); } catch (Exception e) { ex = e; exceptionFile = "messages.yml"; }
        long msAfter = System.currentTimeMillis() - mss;
        if(ex != null) {
            sender.sendMessage(Utils.chat(Messages.getMessagesCommands("reload.failed", ex.toString())));
            Bukkit.getLogger().severe("An error occurred whilst trying to reload the " + exceptionFile + " file!");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe("Stack trace is below:");
            Utils.printBetterStackTrace(ex);
        }else{
            if(!Main.getMainConfig().getBoolean("enabled")) { sender.sendMessage(Utils.chat("&6It seems as though you have set &a'enabled' &6to &cfalse&6. &d&l&nYOU MUST RESTART THE SERVER FOR THIS OPTION TO TAKE EFFECT!"));}

            sender.sendMessage(Utils.chat(Messages.getMessagesCommands("reload.success", msAfter + "")));
        }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return CommandManager.emptyList;
    }

}
