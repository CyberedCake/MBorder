package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class TPMob extends SubCommand {

    public TPMob() {
        super("tpmob", "mborder.command.tpmob", "Teleports you to one of the two mobs.", "/mborder tpmob <overworld/nether>", new String[]{"mobtp", "mobteleport", "teleporttomob"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(DataUtils.getCustomYmlBoolean("data", "server.active")) {
                if(args.length < 2) {
                    p.sendMessage(Utils.chat("&cInvalid usage! &7/mborder tpmob <overworld/nether>"));
                }else{
                    if(args[1].equals("overworld") || args[1].equals("nether")) {
                        Location location = DataUtils.getCustomYmlLocation("data", "server." + args[1] + ".centerLocation");
                        try {
                            p.teleport(location);
                            String mobType = "";
                            if(args[1].equals("overworld")) { mobType = "&aoverworld"; }
                            if(args[1].equals("nether")) { mobType = "&cnether"; }
                            p.sendMessage(Utils.chat("&fYou teleported to the " + mobType + " &fentity!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                        } catch (Exception e) {
                            p.sendMessage(Utils.chat("&cAn error occurred whilst trying to warp you to the " + args[1] + " entity: &8" + e.toString()));
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.1F);
                        }
                    }else{
                        p.sendMessage(Utils.chat("&cUnknown argument: &8" + args[1]));
                    }
                }
            }else{
                p.sendMessage(Utils.chat("&cThe game must be active for you to teleport to the entity!"));
            }
        }else{
            Main.logError("Only players can execute this sub-command!");
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
