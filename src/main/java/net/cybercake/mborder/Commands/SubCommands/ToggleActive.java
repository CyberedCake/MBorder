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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
            if(!DataUtils.getCustomYmlBoolean("data", "server.active")) {
                Location locationOverworld = Utils.getTopBlock(new Location(getMainWorld(), p.getLocation().getX(), 0, p.getLocation().getZ(), 0, 0), 120);
                Location locationNether = Utils.getTopBlock(new Location(Bukkit.getWorld(getMainWorldString() + "_nether"), p.getLocation().getX(), 0, p.getLocation().getZ(), 0, 0), 120);

                DataUtils.setCustomYml("data", "server.active", true);
                DataUtils.setCustomYml("data", "server.activeStart", Utils.getUnix());
                DataUtils.setCustomYml("data", "server.overworld.centerLocation", locationOverworld);
                DataUtils.setCustomYml("data", "server.nether.centerLocation", locationNether);

                Entity entityoverworld = getMainWorld().spawnEntity(locationOverworld, EntityType.valueOf(Main.getMainConfig().getString("overworld.worldBorderAnimal")));
                spawnEntity("overworld", entityoverworld);
                DataUtils.setCustomYml("data", "server.overworld.mobUUID", entityoverworld.getUniqueId().toString());

                Entity entitynether = Bukkit.getWorld(getMainWorldString() + "_nether").spawnEntity(locationNether, EntityType.valueOf(Main.getMainConfig().getString("nether.worldBorderAnimal")));
                spawnEntity("nether", entitynether);
                DataUtils.setCustomYml("data", "server.nether.mobUUID", entitynether.getUniqueId().toString());

                Bukkit.getOnlinePlayers().forEach(player -> player.teleport(Utils.getTopBlock(new Location(p.getWorld(), p.getLocation().getX(), 0, p.getLocation().getZ(), 0, 0), 120)));
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Utils.chat("&a&lMBORDER GAME ENABLED!\n&7&oThe game has been started by &e" + p.getName())));

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

    public static void spawnEntity(String worldType, Entity entity) {
        entity.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "worldBorderAnimal"), PersistentDataType.STRING, worldType);
        entity.setCustomName(Utils.chat(Main.getMainConfig().getString(worldType + ".worldBorderAnimalName")));
        entity.setCustomNameVisible(true);
        entity.setPersistent(true);
        LivingEntity livingEntity = (LivingEntity) entity;
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 15, false, false));

        if(entity instanceof Steerable) {
            Steerable vehicle = (Steerable) entity;
            vehicle.setSaddle(true);
        }else if(entity instanceof AbstractHorse) {
            AbstractHorse horse = (AbstractHorse) entity;
            horse.setTamed(true);
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        }
        if(Main.getMainConfig().getBoolean(worldType + ".worldBorderAnimalSilent")) { entity.setSilent(true); }
        if(Main.getMainConfig().getBoolean(worldType + ".worldBorderAnimalGlowing")) { entity.setGlowing(true); }
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
