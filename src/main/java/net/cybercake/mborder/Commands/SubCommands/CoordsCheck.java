package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CoordsCheck extends SubCommand {

    public CoordsCheck() {
        super("coordscheck", "mborder.command.coordscheck", "Gets the distance between you and a certain location.", "/mborder coordscheck [<x> <y> <z>]", new String[]{"calc", "distancebetween"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(args.length == 1) {
                if(DataUtils.getCustomYmlBoolean("data", "server.active")) {
                    String type = "";
                    if(p.getWorld().equals(ToggleActive.getMainWorld())) {
                        type = "overworld";
                    }else if(p.getWorld().equals(Bukkit.getWorld(ToggleActive.getMainWorldString() + "_nether"))){
                        type = "nether";
                    }else{
                        p.sendMessage(Utils.chat("&cInvalid usage! &7/mborder coordscheck [<x> <y> <z>]"));
                        return;
                    }
                    TextComponent mainBlocksText = new TextComponent(Utils.chat("&fYou are &e" + Utils.formatLong(Math.round(p.getLocation().distance(DataUtils.getCustomYmlLocation("data", "server." + type + ".centerLocation")))) + " &fblocks away from "));
                    TextComponent theCenter = new TextComponent(Utils.chat("&a&nthe center"));
                    theCenter.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.chat("&6World Center:\n&fx" + Math.round(DataUtils.getCustomYmlLocation("data", "server." + type + ".centerLocation").getX()) + " y" + Math.round(DataUtils.getCustomYmlLocation("data", "server." + type + ".centerLocation").getY()) + " z" + Math.round(DataUtils.getCustomYmlLocation("data", "server." + type + ".centerLocation").getZ()))).create()));
                    mainBlocksText.addExtra(theCenter);
                    p.spigot().sendMessage(mainBlocksText);
                }else{
                    p.sendMessage(Utils.chat("&cInvalid usage! &7/mborder coordscheck [<x> <y> <z>]"));
                }
            }
            else if(args.length < 4) { p.sendMessage(Utils.chat("&cInvalid usage! &7/mborder coordscheck [<x> <y> <z>]")); }
            else if((!isInt(args[1])) || (!isInt(args[2])) || (!isInt(args[3]))) { p.sendMessage(Utils.chat("&cInvalid integer! &7/mborder coordscheck [<x> <y> <z>]")); }
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
