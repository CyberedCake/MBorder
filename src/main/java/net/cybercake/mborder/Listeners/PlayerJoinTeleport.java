package net.cybercake.mborder.Listeners;

import net.cybercake.mborder.Commands.SubCommands.TPRandom;
import net.cybercake.mborder.Utils.DataUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.NumberConversions;

public class PlayerJoinTeleport implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if(!DataUtils.getCustomYmlBoolean("data", "server.active")) return;
        if(TPRandom.getWorldType(p) == null) return;
        if(getOnlyXZDistance(p.getLocation(), DataUtils.getCustomYmlLocation("data", "server." + TPRandom.getWorldType(p) + ".centerLocation")) < 76) return;

        p.teleport(TPRandom.teleportRandom(p));
    }

    public static double getOnlyXZDistance(Location loc1, Location loc2) {
        return Math.sqrt(NumberConversions.square(loc1.getX() - loc2.getX()) + NumberConversions.square(loc1.getZ() - loc2.getZ()));
    }

}
