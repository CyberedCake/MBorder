package net.cybercake.mborder.RepeatingTasks;

import net.cybercake.mborder.Commands.SubCommands.ToggleActive;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.DataUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class TrackEntity implements Runnable {


    // Remember: simply the two borders thing into a method to make it 'cleaner'

    @Override
    public void run() {
        if(!DataUtils.getCustomYmlBoolean("data", "server.active")) { disableGame(); return; }


        Entity overworldentity = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server.overworld.mobUUID")));
        if (overworldentity != null){
            World overworld = ToggleActive.getMainWorld();
            WorldBorder border = overworld.getWorldBorder();
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
            border.setDamageBuffer(Main.getMainConfig().getDouble("overworld.worldBorderBuffer"));
            border.setDamageAmount(Main.getMainConfig().getDouble("overworld.worldBorderDamage"));
            border.setDamageAmount(Main.getMainConfig().getLong("overworld.worldBorderDamage"));
            border.setWarningDistance(5);

            DataUtils.setCustomYml("data", "server.overworld.centerLocation", overworldentity.getLocation());
            overworld.setSpawnLocation(DataUtils.getCustomYmlLocation("data", "server.overworld.centerLocation"));
            overworld.setGameRule(GameRule.SPAWN_RADIUS, 40);

            overworldentity.setPortalCooldown(5);
        }

        Entity netherentity = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server.nether.mobUUID")));
        if (netherentity != null){
            World nether = Bukkit.getWorld(ToggleActive.getMainWorldString() + "_nether");
            WorldBorder border = nether.getWorldBorder();
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
            border.setDamageBuffer(Main.getMainConfig().getDouble("nether.worldBorderBuffer"));
            border.setDamageAmount(Main.getMainConfig().getDouble("nether.worldBorderDamage"));
            border.setDamageAmount(Main.getMainConfig().getLong("nether.worldBorderDamage"));
            border.setWarningDistance(5);

            DataUtils.setCustomYml("data", "server.nether.centerLocation", netherentity.getLocation());
            nether.setSpawnLocation(DataUtils.getCustomYmlLocation("data", "server.nether.centerLocation"));
            nether.setGameRule(GameRule.SPAWN_RADIUS, 40);

            netherentity.setPortalCooldown(5);
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
