package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class DistanceBetween extends SubCommand {

    public DistanceBetween() {
        super("distancebetween", "mborder.command.distancebetween", "Gets the distance between you and a certain location.", "/mborder distancebetween <x> <y> <z>", new String[]{"calc"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(args.length < 4) { p.sendMessage(Utils.chat("&cInvalid usage! &7/mborder distancebetween <x> <y> <z>")); }
            else if((!isInt(args[1])) || (!isInt(args[2])) || (!isInt(args[3]))) { p.sendMessage(Utils.chat("&cInvalid integer! &7/mborder distancebetween <x> <y> <z>")); }
            else {
                Location playerLocation = p.getLocation();
                Location specificLocation = new Location(p.getWorld(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));

                p.sendMessage(Utils.chat("&fYou are &e" + Utils.formatLong(Math.round(playerLocation.distance(specificLocation))) + " &fblocks away from &ax" + args[1] + " y" + args[2] + " z" + args[3] + "&f!"));
            }
        }else{
            System.out.println("Only players can execute this sub-command!");
        }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(args.length == 2) {
                return CommandManager.createReturnList(Arrays.asList(Math.round(p.getLocation().getX()) + " " + Math.round(p.getLocation().getY()) + " " + Math.round(p.getLocation().getZ())), args[1]);
            }else if(args.length == 3) {
                return CommandManager.createReturnList(Arrays.asList(Math.round(p.getLocation().getY()) + " " + Math.round(p.getLocation().getZ())), args[2]);
            }else if(args.length == 4) {
                return CommandManager.createReturnList(Arrays.asList(String.valueOf(Math.round(p.getLocation().getZ()))), args[3]);
            }
        }
        return CommandManager.emptyList;
    }

    public static boolean isInt(String integerString) {
        try {
            Integer.parseInt(integerString);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
