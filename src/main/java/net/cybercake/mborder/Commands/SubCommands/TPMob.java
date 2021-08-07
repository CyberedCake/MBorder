package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Messages;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class TPMob extends SubCommand {

    public TPMob() {
        super("tpmob", "mborder.command.tpmob", Messages.getMessagesCommands("tpMob.description"), "/mborder tpmob <overworld/nether>", new String[]{"mobtp", "mobteleport", "teleporttomob"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(!DataUtils.getCustomYmlBoolean("data", "server.active")) {
                p.sendMessage(Utils.chat(Messages.getMessagesCommands("tpMob.gameMustBeActive")));
                return;
            }
            if(args.length < 2) {
                p.sendMessage(Utils.chat(Messages.getMessagesCommands("invalidUsage", this.getUsage())));
                return;
            }
            if(!args[1].equalsIgnoreCase("overworld") && !args[1].equalsIgnoreCase("nether")) {
                p.sendMessage(Utils.chat(Messages.getMessagesCommands("unknown.main", Messages.getMessagesCommands("unknown.argument"), args[1])));
                return;
            }
            Location location = DataUtils.getCustomYmlLocation("data", "server." + args[1] + ".centerLocation");
            try {
                p.teleport(location);
                String mobType = "";
                if(args[1].equals("overworld")) { mobType = Messages.getMessagesGeneral("overworld"); }
                if(args[1].equals("nether")) { mobType = Messages.getMessagesGeneral("nether"); }
                p.sendMessage(Utils.chat(Messages.getMessagesCommands("tpMob.success", mobType)));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
            } catch (Exception e) {
                p.sendMessage(Utils.chat(Messages.getMessagesCommands("tpMob.failed", args[1], e.toString())));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.1F);
            }
        }else{
            Main.logError(Messages.getMessagesCommands("onlyPlayers"));
        }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            if(DataUtils.getCustomYmlBoolean("data", "server.active")) {
                if(args.length <= 2) {
                    return CommandManager.createReturnList(Arrays.asList("overworld", "nether"), args[1]);
                }
            }
        }
        return CommandManager.emptyList;
    }
}
