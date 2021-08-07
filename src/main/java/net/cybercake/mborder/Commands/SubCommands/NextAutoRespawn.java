package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Messages;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class NextAutoRespawn extends SubCommand {

    public NextAutoRespawn() {
        super("nextautorespawn", "mborder.command.nextautorespawn", Messages.getMessagesCommands("nextAutoRespawn.description"), "/mborder nextautorespawn", new String[]{"nar", "respawn"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        if(!DataUtils.getCustomYmlBoolean("data", "server.active")) {
            sender.sendMessage(Utils.chat(Messages.getMessagesCommands("nextAutoRespawn.gameMustBeActive")));
            return;
        }

        sender.sendMessage(Utils.getSeperator(ChatColor.BLUE));
        if(sender instanceof Player) {
            Player p = (Player) sender;

            //TextComponent nextAutoRespawn = new TextComponent(Utils.chat("&fThe next auto-respawn will occur in "));
            //// So many damn parentheses it's honestly horrifying
            //TextComponent nextAutoRespawnSeconds = new TextComponent(Utils.chat("&e" + Utils.getFormattedSeconds((Main.getMainConfig().getLong("mobRespawnInterval")-(Utils.getUnix() - DataUtils.getCustomYmlLong("data", "server.activeStart"))), Utils.ReturnType.WITH_LETTERS_SPACED, false)));
            //nextAutoRespawnSeconds.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.chat("&6In Seconds:\n&f" + Utils.formatLong(Main.getMainConfig().getLong("mobRespawnInterval")-(Utils.getUnix() - DataUtils.getCustomYmlLong("data", "server.activeStart"))) + " seconds")).create()));
            //TextComponent nextAutoRespawnEnding = new TextComponent(Utils.chat("&f!"));
            //nextAutoRespawn.addExtra(nextAutoRespawnSeconds);
            //nextAutoRespawn.addExtra(nextAutoRespawnEnding);
            //p.spigot().sendMessage(nextAutoRespawn);
            p.sendMessage(Utils.chat(Messages.getMessagesCommands("nextAutoRespawn.nextAutoRespawn", Utils.getFormattedSeconds((Main.getMainConfig().getLong("mobRespawnInterval")-(Utils.getUnix() - DataUtils.getCustomYmlLong("data", "server.activeStart"))), Utils.ReturnType.WITH_LETTERS_SPACED, false))));

            double getTimeLeft = Utils.getUnix() - DataUtils.getCustomYmlLong("data", "server.activeStart");
            double getTimeMax = Main.getMainConfig().getLong("mobRespawnInterval");
            double percentage = getTimeLeft / getTimeMax;
            p.sendMessage(Utils.chat(Utils.getProgressBar(ChatColor.AQUA, ChatColor.WHITE, percentage, "|", 115)));

            p.sendMessage(" ");

            //TextComponent mobRespawnInterval = new TextComponent(Utils.chat("&fRespawns will always happen every "));
            //TextComponent mobRespawnIntervalSeconds = new TextComponent(ChatColor.GREEN + Utils.getFormattedSeconds(Main.getMainConfig().getLong("mobRespawnInterval"), Utils.ReturnType.WITH_LETTERS_SPACED, false));
            //mobRespawnIntervalSeconds.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.chat("&6In Seconds:\n&f" + Utils.formatLong(Main.getMainConfig().getLong("mobRespawnInterval")) + " seconds")).create()));
            //TextComponent mobRespawnIntervalEnding = new TextComponent(Utils.chat("&f!"));
            //mobRespawnInterval.addExtra(mobRespawnIntervalSeconds);
            //mobRespawnInterval.addExtra(mobRespawnIntervalEnding);
            //p.spigot().sendMessage(mobRespawnInterval);
            p.sendMessage(Utils.chat(Messages.getMessagesCommands("nextAutoRespawn.respawnsEvery", Utils.getFormattedSeconds(Main.getMainConfig().getLong("mobRespawnInterval"), Utils.ReturnType.WITH_LETTERS_SPACED, false))));
        } else {
            sender.sendMessage(Utils.chat(Messages.getMessagesCommands("nextAutoRespawn.nextAutoRespawn", Utils.getFormattedSeconds(Main.getMainConfig().getLong("mobRespawnInterval")-(Utils.getUnix() - DataUtils.getCustomYmlLong("data", "server.activeStart")), Utils.ReturnType.WITH_LETTERS_SPACED, false))));
            sender.sendMessage(Utils.chat(Messages.getMessagesCommands("nextAutoRespawn.respawnsEvery", Utils.getFormattedSeconds(Main.getMainConfig().getLong("mobRespawnInterval"), Utils.ReturnType.WITH_LETTERS_SPACED, false))));
        }
        sender.sendMessage(Utils.getSeperator(ChatColor.BLUE));
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return CommandManager.emptyList;
    }
}
