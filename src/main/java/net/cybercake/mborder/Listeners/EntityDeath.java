package net.cybercake.mborder.Listeners;

import net.cybercake.mborder.Utils.DataUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class EntityDeath implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(DataUtils.getCustomYmlBoolean("data", "server.active")) {
            if(e.getEntity().getUniqueId().equals(UUID.fromString(DataUtils.getCustomYmlString("data", "server.overworld.mobUUID")))) {
                e.getDrops().clear();
            }else if(e.getEntity().getUniqueId().equals(UUID.fromString(DataUtils.getCustomYmlString("data", "server.nether.mobUUID")))) {
                e.getDrops().clear();
            }
        }
    }

}
