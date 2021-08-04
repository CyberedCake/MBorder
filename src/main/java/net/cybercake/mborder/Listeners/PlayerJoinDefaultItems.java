package net.cybercake.mborder.Listeners;

import net.cybercake.mborder.Commands.SubCommands.DefaultItemsCMD;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinDefaultItems implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        if(!DataUtils.getCustomYmlBoolean("data", "server.active")) return;

        Player p = e.getPlayer();

        if(DataUtils.getCustomYmlLong("/playerdata/" + p.getUniqueId(), "player.firstJoined") == 0) {
            DataUtils.setCustomYml("/playerdata/" + p.getUniqueId(), "player.firstJoined", Utils.getUnix());
            for(ItemStack item : DefaultItemsCMD.getCustomItems()) {
                p.getInventory().addItem(item);
            }
        }
    }

}
