package net.cybercake.mborder.RepeatingTasks;

import net.cybercake.mborder.Commands.SubCommands.ToggleActive;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AttemptRespawn implements Runnable {

    public static Map<String, Boolean> attemptingRespawn;
    public static Map<String, Integer> attempts;
    public static Map<String, Integer> attemptsMax;

    public AttemptRespawn() {
        attemptingRespawn.put("overworld", false);
        attemptingRespawn.put("nether", false);

        attempts.put("overworld", 0);
        attempts.put("nether", 0);

        attemptsMax.put("overworld", 5);
        attemptsMax.put("nether", 5);
    }

    @Override
    public void run() {
        if(DataUtils.getCustomYmlBoolean("data", "server.active")) {
            respawnAttempt("overworld");
            respawnAttempt("nether");
        }
    }

    public static void respawnAttempt(String worldType) {

        Entity entity = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server." + worldType + ".mobUUID")));
        if(entity == null) {
            if(!attemptingRespawn.get(worldType)) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(Utils.chat("&c&lUh-oh! &fIt looks as if the " + worldType + " mob has died! Attempting to respawn &a(5 attempts max)"));
                }
            }
            attemptingRespawn.put(worldType, true);
            if(attempts.get(worldType) >= attemptsMax.get(worldType)) {
                for(Player p : Bukkit.getOnlinePlayers()) { p.sendMessage(Utils.chat("&cFailed to respawn creature in the " + worldType + "! Try again later: &8(attempts=5, attemptingRespawn=true, world=" + worldType + ")")); }
                attemptingRespawn.put(worldType, false);
                attempts.put(worldType, 0);
                TrackEntity.disableGame();
            }else {
                try {
                    Entity attemptedRespawnEntity = null;
                    if(worldType.equals("overworld")) {
                        attemptedRespawnEntity = ToggleActive.getMainWorld().spawnEntity(DataUtils.getCustomYmlLocation("data", "server.overworld.centerLocation"), EntityType.valueOf(Main.getMainConfig().getString("overworld.worldBorderAnimal")));
                    }else if(worldType.equals("nether")) {
                        attemptedRespawnEntity = Bukkit.getWorld(ToggleActive.getMainWorldString() + "_nether").spawnEntity(DataUtils.getCustomYmlLocation("data", "server.nether.centerLocation"), EntityType.valueOf(Main.getMainConfig().getString("nether.worldBorderAnimal")));
                    }
                    ToggleActive.spawnEntity(ToggleActive.MEntityType.valueOf(worldType.toUpperCase(Locale.ROOT)), attemptedRespawnEntity);
                    if(attemptedRespawnEntity == null) {
                        for(Player p : Bukkit.getOnlinePlayers()) {
                            p.sendMessage(Utils.chat("&7Respawn Attempt #" + (attempts.get(worldType)+1) + ": &cFAILED"));
                            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 0.4F, 1F);
                        }
                        attempts.put(worldType, attempts.get(worldType)+1);
                        attemptingRespawn.put(worldType, true);
                    }else if(attemptedRespawnEntity != null) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendMessage(Utils.chat("&7Respawn Attempt #" + (attempts.get(worldType)+1) + ": &aSUCCESS"));
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.4F, 2F);
                        }
                        DataUtils.setCustomYml("data", "server." + worldType + ".mobUUID", attemptedRespawnEntity.getUniqueId().toString());
                        attemptingRespawn.put(worldType, false);
                        attempts.put(worldType, 0);
                    }
                } catch (Exception e) {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(Utils.chat("&7Respawn Attempt #" + (attempts.get(worldType)+1) + ": &cFAILED"));
                        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 0.4F, 1F);
                    }
                    attempts.put(worldType, attempts.get(worldType)+1);
                    attemptingRespawn.put(worldType, true);
                }
            }
        }else{
            attemptingRespawn.put(worldType, false);
            attempts.put(worldType, 0);
        }
    }
}
