package net.cybercake.mborder.RepeatingTasks;

import net.cybercake.mborder.Commands.SubCommands.ToggleActive;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.DataUtils;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class TrackEntity implements Runnable {

    @Override
    public void run() {
        if(DataUtils.getCustomYmlBoolean("data", "server.active")) {
            Entity overworldentity = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server.overworld.mobUUID")));
            if (overworldentity != null){
                WorldBorder border = ToggleActive.getMainWorld().getWorldBorder();
                border.setCenter(overworldentity.getLocation().getX(), overworldentity.getLocation().getZ());
                border.setSize(Main.getMainConfig().getDouble("overworld.worldBorderSize"));
                if(Main.getMainConfig().getString("overworld.worldBorderColor").equals("RED")) {
                    border.setSize(Main.getMainConfig().getDouble("overworld.worldBorderSize")+0.01);
                    border.setSize(Main.getMainConfig().getDouble("overworld.worldBorderSize"), 9999999);
                }else if(Main.getMainConfig().getString("overworld.worldBorderColor").equals("GREEN")) {
                    border.setSize(Main.getMainConfig().getDouble("overworld.worldBorderSize")-0.01);
                    border.setSize(Main.getMainConfig().getDouble("overworld.worldBorderSize"), 9999999);
                }else{
                    border.setSize(Main.getMainConfig().getDouble("overworld.worldBorderSize"));
                }
                border.setDamageBuffer(0.0);
                border.setDamageAmount(Main.getMainConfig().getLong("overworld.worldBorderDamage"));
                border.setWarningDistance(0);
                DataUtils.setCustomYml("data", "server.overworld.centerLocation", overworldentity.getLocation());
            }
            Entity netherentity = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server.nether.mobUUID")));
            if (netherentity != null){
                WorldBorder border = Bukkit.getWorld(ToggleActive.getMainWorldString() + "_nether").getWorldBorder();
                border.setCenter(netherentity.getLocation().getX(), netherentity.getLocation().getZ());
                border.setSize(Main.getMainConfig().getDouble("nether.worldBorderSize"));
                if(Main.getMainConfig().getString("nether.worldBorderColor").equals("RED")) {
                    border.setSize(Main.getMainConfig().getDouble("nether.worldBorderSize")+0.01);
                    border.setSize(Main.getMainConfig().getDouble("nether.worldBorderSize"), 9999999);
                }else if(Main.getMainConfig().getString("nether.worldBorderColor").equals("GREEN")) {
                    border.setSize(Main.getMainConfig().getDouble("nether.worldBorderSize")-0.01);
                    border.setSize(Main.getMainConfig().getDouble("nether.worldBorderSize"), 9999999);
                }else{
                    border.setSize(Main.getMainConfig().getDouble("nether.worldBorderSize"));
                }
                border.setDamageBuffer(0.0);
                border.setDamageAmount(Main.getMainConfig().getLong("nether.worldBorderDamage"));
                border.setWarningDistance(0);
                DataUtils.setCustomYml("data", "server.nether.centerLocation", netherentity.getLocation());
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

        border = Bukkit.getWorld(ToggleActive.getMainWorldString() + "_nether").getWorldBorder();
        border.setCenter(0, 0);
        border.setSize(59999968);
        border.setWarningDistance(0);
        DataUtils.setCustomYml("data", "server.active", false);
    }
}
