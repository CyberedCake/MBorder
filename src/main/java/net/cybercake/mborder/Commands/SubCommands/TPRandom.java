package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.DefaultItems;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Messages;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TPRandom extends SubCommand {

    public TPRandom() {
        super("tprandom", "mborder.command.tprandom", Messages.getMessagesCommands("tpRandom.description"), "/mborder tprandom [player]", new String[]{"teleportrandom", "tpr"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(!DataUtils.getCustomYmlBoolean("data", "server.active")) {
                p.sendMessage(Utils.chat(Messages.getMessagesCommands("tpRandom.gameMustBeActive")));
                return;
            }

            if(args.length < 2) {
                p.teleport(teleportRandom(p));
                p.sendMessage(Utils.chat(Messages.getMessagesCommands("tpRandom.teleportedSelf")));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
            }else if(args.length > 1) {
                Player target = Bukkit.getPlayerExact(args[1]);
                if(target == null) { p.sendMessage(Utils.chat(Messages.getMessagesCommands("invalidOnlinePlayer", this.getUsage()))); return; }

                p.sendMessage(Utils.chat(Messages.getMessagesCommands("tpRandom.teleportedPlayer", target.getName())));
                target.teleport(teleportRandom(target));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
            }
        }else{
            Main.logError(Messages.getMessagesCommands("onlyPlayers"));
        }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        if(args.length <= 2) {
            ArrayList<String> names = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> names.add(player.getName()));
            return names;
        }
        return CommandManager.emptyList;
    }

    public static String getWorldType(Player player) {
        if(player.getWorld().getName().equals(ToggleActive.getMainWorldString())) {
            return "overworld";
        }else if(player.getWorld().getName().equals(ToggleActive.getMainWorldString() + "_nether")) {
            return "nether";
        }
        return null;
    }

    public static World getWorldFromType(String worldType) {
        if(worldType.equals("overworld")) {
            return ToggleActive.getMainWorld();
        }else if(worldType.equals("nether")) {
            return Bukkit.getWorld(ToggleActive.getMainWorldString() + "_nether");
        }
        return null;
    }

    public static Location teleportRandom(Player player) {
        if(getWorldType(player) == null) {
            return null;
        }
        double xValue = DataUtils.getCustomYmlLocation("data", "server." + getWorldType(player) + ".centerLocation").getX();
        double zValue = DataUtils.getCustomYmlLocation("data", "server." + getWorldType(player) + ".centerLocation").getZ();

        return Utils.getTopBlock(new Location(getWorldFromType(getWorldType(player)), Utils.getRandomDouble(xValue-50, xValue+50), 0, Utils.getRandomDouble(zValue-50, zValue+50), 0, 0), 124);
    }
}
