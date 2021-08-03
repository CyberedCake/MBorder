package net.cybercake.mborder.RepeatingTasks;

import net.cybercake.mborder.Commands.SubCommands.ToggleActive;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class RespawnMob implements Runnable{

    @Override
    public void run() {
        if(!DataUtils.getCustomYmlBoolean("data", "server.active")) { return; }
        long diff = Utils.getUnix() - DataUtils.getCustomYmlLong("data", "server.activeStart");
        if(diff < Main.getMainConfig().getLong("mobRespawnInterval")) return;

        respawnEntity("overworld");
        respawnEntity("nether");
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Utils.chat("\n&fThe &aoverworld &fmob has been respawned in &e" + ToggleActive.getMainWorldString() + "&f!\n&fThe &cnether &fmob has been respawned in &e" + ToggleActive.getMainWorldString() + "_nether&f!\n ")));
    }

    public static void respawnEntity(String worldType) {
        Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server." + worldType + ".mobUUID"))).remove();
        World world = null;
        if(worldType.equals("overworld")) {
            world = ToggleActive.getMainWorld();
        }else if(worldType.equals("nether")) {
            world = Bukkit.getWorld(ToggleActive.getMainWorldString() + "_nether");
        }
        Location location = Utils.getTopBlock(DataUtils.getCustomYmlLocation("data", "server." + worldType + ".centerLocation"), 120);
        Entity entity = world.spawnEntity(location, EntityType.valueOf(Main.getMainConfig().getString(worldType + ".worldBorderAnimal")));
        ToggleActive.spawnEntity(worldType, entity);
        DataUtils.setCustomYml("data", "server." + worldType + ".mobUUID", entity.getUniqueId().toString());
        DataUtils.setCustomYml("data", "server.activeStart", Utils.getUnix());
        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(entity.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1F, 1F));
        world.spawnParticle(Particle.CLOUD, entity.getLocation(), 1000, 0, 0, 0, 0.4);
    }
}
