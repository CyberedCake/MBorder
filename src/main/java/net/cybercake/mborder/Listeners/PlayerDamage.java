package net.cybercake.mborder.Listeners;

import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.DataUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamage implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if(!Main.getMainConfig().getBoolean("mobsCannotKillPlayers")) return;
        if(!DataUtils.getCustomYmlBoolean("data", "server.active")) return;
        if(!(e.getEntity() instanceof Player)) return;
        if(!(e.getDamager() instanceof LivingEntity)) return;
        Player victim = (Player) e.getEntity();
        if(e.getFinalDamage() <= victim.getHealth()) return;

        e.setCancelled(true);
    }

}
