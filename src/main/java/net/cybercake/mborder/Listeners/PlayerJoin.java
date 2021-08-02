package net.cybercake.mborder.Listeners;

import net.cybercake.mborder.Commands.SubCommands.ToggleActive;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        if(Main.justStarted) {
            Main.justStarted = false;
            try {
                DataUtils.setCustomYml("data", "server.active", true);
                DataUtils.setCustomYml("data", "server.activeStart", Utils.getUnix());

                Entity entityoverworld = ToggleActive.getMainWorld().spawnEntity(DataUtils.getCustomYmlLocation("data", "server.overworld.centerLocation"), EntityType.valueOf(Main.getMainConfig().getString("overworld.worldBorderAnimal")));
                ToggleActive.spawnEntity(ToggleActive.MEntityType.OVERWORLD, entityoverworld);
                DataUtils.setCustomYml("data", "server.overworld.mobUUID", entityoverworld.getUniqueId().toString());

                Entity entitynether = Bukkit.getWorld(ToggleActive.getMainWorldString() + "_nether").spawnEntity(DataUtils.getCustomYmlLocation("data", "server.nether.centerLocation"), EntityType.valueOf(Main.getMainConfig().getString("nether.worldBorderAnimal")));
                ToggleActive.spawnEntity(ToggleActive.MEntityType.NETHER, entitynether);
                DataUtils.setCustomYml("data", "server.nether.mobUUID", entitynether.getUniqueId().toString());
                System.out.println("Successfully started the game again because persistence in the config is set to 'true'!");
            } catch (Exception ex) {
                DataUtils.setCustomYml("data", "server.active", false);
                Bukkit.getLogger().severe("MBorder: An error occurred whilst attempting to respawn mobs from the last server instance, disabling game...");
                Bukkit.getLogger().severe(" ");
                Bukkit.getLogger().severe("Stack trace is below:");
                Utils.printBetterStackTrace(ex);
            }
        }
    }

}
