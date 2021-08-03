package net.cybercake.mborder.Listeners;

import net.cybercake.mborder.RepeatingTasks.RespawnMob;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class EntityDeath implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(!DataUtils.getCustomYmlBoolean("data", "server.active")) return;

        if(e.getEntity().getUniqueId().equals(UUID.fromString(DataUtils.getCustomYmlString("data", "server.overworld.mobUUID")))) {
            e.getDrops().clear();
            Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Utils.chat("&fThe &aOVERWORLD &fmob has died, respawning it..."))));
            RespawnMob.respawnEntity("overworld");
        }else if(e.getEntity().getUniqueId().equals(UUID.fromString(DataUtils.getCustomYmlString("data", "server.nether.mobUUID")))) {
            e.getDrops().clear();
            Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Utils.chat("&fThe &cNETHER &fmob has died, respawning it..."))));
            RespawnMob.respawnEntity("nether");
        }
    }

}
