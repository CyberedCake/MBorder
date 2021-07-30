package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class ToggleActive extends SubCommand {

    public ToggleActive() {
        super("toggleactive", "mborder.command.toggleactive", "Start or stop the game.", "/mborder toggleactive", new String[]{"start", "stop"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(p.getWorld() != getMainWorld()) { p.sendMessage(Utils.chat("&cYou must be in the main Minecraft world specified in the server.properties to start the game!")); }
            else if(!DataUtils.getCustomYmlBoolean("data", "server.active")) {
                DataUtils.setCustomYml("data", "server.active", true);
                DataUtils.setCustomYml("data", "server.overworld.centerLocation", p.getLocation());

                Entity entityoverworld = getMainWorld().spawnEntity(p.getLocation(), EntityType.valueOf(Main.getMainConfig().getString("overworld.worldBorderAnimal")));
                spawnEntity(MEntityType.OVERWORLD, entityoverworld);
                DataUtils.setCustomYml("data", "server.overworld.mobUUID", entityoverworld.getUniqueId().toString());

                Entity entitynether = Bukkit.getWorld(getMainWorldString() + "_nether").spawnEntity(new Location(Bukkit.getWorld(getMainWorldString() + "_nether"), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch()), EntityType.valueOf(Main.getMainConfig().getString("nether.worldBorderAnimal")));
                spawnEntity(MEntityType.NETHER, entitynether);
                DataUtils.setCustomYml("data", "server.nether.mobUUID", entitynether.getUniqueId().toString());

                p.sendMessage(Utils.chat("&a&lGAME ENABLED!"));
                p.sendMessage(Utils.chat("&7&oThe game has started with the center being your location!"));
            }else if(DataUtils.getCustomYmlBoolean("data", "server.active")) {
                DataUtils.setCustomYml("data", "server.active", false);
                Entity entityoverworld = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server.overworld.mobUUID")));
                entityoverworld.remove();
                Entity entitynether = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server.nether.mobUUID")));
                entitynether.remove();
                p.sendMessage(Utils.chat("&c&lGAME DISABLED!"));
                p.sendMessage(Utils.chat("&7&oThe game has been ended!"));
            }
        } else { System.out.println("Only players can execute this sub-command!"); }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return CommandManager.emptyList;
    }

    public enum MEntityType {
        OVERWORLD, NETHER
    }

    public static void spawnEntity(MEntityType entityType, Entity entity) {
        switch(entityType) {
            case OVERWORLD:
                entity.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "overworld.worldBorderAnimal"), PersistentDataType.INTEGER, 1);
                entity.setCustomName(Utils.chat(Main.getMainConfig().getString("overworld.worldBorderAnimalName")));
                entity.setCustomNameVisible(true);
                entity.setInvulnerable(true);
                entity.setPersistent(true);

                if(entity instanceof Pig) {
                    Pig pig = (Pig) entity;
                    pig.setSaddle(true);
                }else if(entity instanceof AbstractHorse) {
                    AbstractHorse horse = (AbstractHorse) entity;
                    horse.setTamed(true);
                    horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                }else if(entity instanceof Strider) {
                    Strider strider = (Strider) entity;
                    strider.setSaddle(true);
                }
                if(Main.getMainConfig().getBoolean("overworld.worldBorderAnimalSilent")) { entity.setSilent(true); }
                if(Main.getMainConfig().getBoolean("overworld.worldBorderAnimalGlowing")) { entity.setGlowing(true); }
                break;
            case NETHER:
                entity.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "nether.worldBorderAnimal"), PersistentDataType.INTEGER, 1);
                entity.setCustomName(Utils.chat(Main.getMainConfig().getString("nether.worldBorderAnimalName")));
                entity.setCustomNameVisible(true);
                entity.setInvulnerable(true);
                entity.setPersistent(true);

                if(entity instanceof Pig) {
                    Pig pig = (Pig) entity;
                    pig.setSaddle(true);
                }else if(entity instanceof AbstractHorse) {
                    AbstractHorse horse = (AbstractHorse) entity;
                    horse.setTamed(true);
                    horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                }else if(entity instanceof Strider) {
                    Strider strider = (Strider) entity;
                    strider.setSaddle(true);
                }
                if(Main.getMainConfig().getBoolean("nether.worldBorderAnimalSilent")) { entity.setSilent(true); }
                if(Main.getMainConfig().getBoolean("nether.worldBorderAnimalGlowing")) { entity.setGlowing(true); }
                break;
        }
    }

    public static World getMainWorld() {
        Properties pr = new Properties();
        File f = new File("server.properties");
        try {
            FileInputStream in = new FileInputStream(f);
            pr.load(in);
            return Bukkit.getWorld(pr.getProperty("level-name"));
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public static String getMainWorldString() {
        Properties pr = new Properties();
        File f = new File("server.properties");
        try {
            FileInputStream in = new FileInputStream(f);
            pr.load(in);
            return pr.getProperty("level-name");
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
