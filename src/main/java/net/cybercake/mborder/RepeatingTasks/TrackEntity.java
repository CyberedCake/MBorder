package net.cybercake.mborder.RepeatingTasks;

import net.cybercake.mborder.Commands.SubCommands.ToggleActive;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TrackEntity implements Runnable {

    @Override
    public void run() {
        if(DataUtils.getCustomYmlBoolean("data", "server.active")) {
            Entity entity = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server.overworld.mobUUID")));
            if(entity == null) {
                disableGame();
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(Utils.chat("&c&lAn error occurred!\n&7The plugin &aMBorder &7could not find the specific entity for the WorldBorder game. Was it killed?"));
                    p.playSound(p.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 0.4F, 1F);
                }
            }else{
                WorldBorder border = ToggleActive.getMainWorld().getWorldBorder();
                border.setCenter(entity.getLocation().getX(), entity.getLocation().getZ());
                border.setSize(Main.getMainConfig().getDouble("overworld.worldBorderSize"));
                border.setWarningDistance(0);
                DataUtils.setCustomYml("data", "server.overworld.centerLocation", entity.getLocation());
            }
        }else if(!DataUtils.getCustomYmlBoolean("data", "server.active")) {
            disableGame();
        }
    }

    public static void disableGame() {
        WorldBorder border = ToggleActive.getMainWorld().getWorldBorder();
        border.setCenter(0, 0);
        border.setSize(59999968);
        border.setWarningDistance(0);
        DataUtils.setCustomYml("data", "server.active", false);
    }
}
