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
import java.io.IOException;
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
            Properties pr = new Properties();
            File f = new File("server.properties");
            try {
                FileInputStream in = new FileInputStream(f);
                pr.load(in);
                String mainWorld = pr.getProperty("level-name");
                if(p.getWorld() != Bukkit.getWorld(mainWorld)) { p.sendMessage(Utils.chat("&cYou must be in the main Minecraft world specified in the server.properties to start the game!")); }
                else if(!DataUtils.getCustomYmlBoolean("data", "server.active")) {
                    DataUtils.setCustomYml("data", "server.active", true);
                    DataUtils.setCustomYml("data", "server.centerLocation", p.getLocation());
                    Entity entity = p.getWorld().spawnEntity(p.getLocation(), EntityType.valueOf(Main.getMainConfig().getString("worldBorderAnimal")));
                    entity.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "worldBorderAnimal"), PersistentDataType.INTEGER, 1);
                    entity.setCustomName(Utils.chat(Main.getMainConfig().getString("worldBorderAnimalName")));
                    entity.setCustomNameVisible(true);
                    entity.setInvulnerable(true);
                    entity.setPersistent(true);
                    if(entity.getType().equals(EntityType.PIG)) {
                        Pig pig = (Pig) entity;
                        pig.setSaddle(true);
                    }else if(entity.getType().equals(EntityType.HORSE)) {
                        AbstractHorse horse = (AbstractHorse) entity;
                        horse.setTamed(true);
                        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                    }
                    if(Main.getMainConfig().getBoolean("worldBorderAnimalSilent")) { entity.setSilent(true); }
                    if(Main.getMainConfig().getBoolean("worldBorderAnimalSilent")) { entity.setGlowing(true); }
                    DataUtils.setCustomYml("data", "server.mobUUID", entity.getUniqueId().toString());
                    p.sendMessage(Utils.chat("&a&lGAME ENABLED!"));
                    p.sendMessage(Utils.chat("&7&oThe game has started with the center being your location!"));
                }else if(DataUtils.getCustomYmlBoolean("data", "server.active")) {
                    DataUtils.setCustomYml("data", "server.active", false);
                    Entity entity = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server.mobUUID")));
                    entity.remove();
                    p.sendMessage(Utils.chat("&c&lGAME DISABLED!"));
                    p.sendMessage(Utils.chat("&7&oThe game has been ended!"));
                }
            } catch (IOException e) { }
        } else { System.out.println("Only players can execute this sub-command!"); }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return CommandManager.emptyList;
    }
}
