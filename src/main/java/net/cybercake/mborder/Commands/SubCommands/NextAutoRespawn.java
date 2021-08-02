package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class NextAutoRespawn extends SubCommand {

    public NextAutoRespawn() {
        super("nextautorespawn", "mborder.command.nextautorespawn", "Allows you to see when the next auto-respawn will occur.", "/mborder nextautorespawn", new String[]{"nar", "respawn"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        if(DataUtils.getCustomYmlBoolean("data", "server.active")) {
            sender.sendMessage(Utils.getSeperator(ChatColor.BLUE));
            sender.sendMessage(Utils.chat("&fThe next auto-respawn will occur in &e" + (Utils.getFormattedSeconds(Main.getMainConfig().getLong("mobRespawnInterval")-(Utils.getUnix() - DataUtils.getCustomYmlLong("data", "server.activeStart")), Utils.ReturnType.WITH_LETTERS_SPACED, false)) + "&f!"));
            long percentage = Main.getMainConfig().getLong("mobRespawnInterval")-(Utils.getUnix() - DataUtils.getCustomYmlLong("data", "server.activeStart")) / Main.getMainConfig().getLong("mobRespawnInterval");
            sender.sendMessage(Utils.chat(""));
            sender.sendMessage(Utils.chat("&fRespawns will always happen every &a" + Utils.getFormattedSeconds(Main.getMainConfig().getLong("mobRespawnInterval"), Utils.ReturnType.WITH_LETTERS_SPACED, false) + "&f!"));
            sender.sendMessage(Utils.getSeperator(ChatColor.BLUE));
        } else {
            sender.sendMessage(Utils.chat("&cThe game must be active for you to view when the next respawn is!"));
        }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return CommandManager.emptyList;
    }
}
