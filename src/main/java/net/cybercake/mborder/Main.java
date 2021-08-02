package net.cybercake.mborder;

import net.cybercake.mborder.Commands.CommandListeners;
import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommands.ToggleActive;
import net.cybercake.mborder.Listeners.EntityDeath;
import net.cybercake.mborder.RepeatingTasks.TrackEntity;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class Main extends JavaPlugin {

    // vv "luckperms" may never be used but it's to tell the Utils class that luckperms does not exist
    public static boolean luckperms = false;

    private static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;

        String serverVersion = getServer().getClass().getPackage().getName();
        String version = serverVersion.substring(serverVersion.lastIndexOf('.') + 1);

        if(!version.equals("v1_17_R1")) {
            getLogger().severe(" ");
            getLogger().severe("--- PLUGIN DISABLED ----");
            getLogger().severe(" ");
            getLogger().severe(Utils.chat("The plugin MBorder only works on servers with versions &a1.17.1&c!"));
            getLogger().severe(" ");
            getLogger().severe("Hopefully later, we will support all 1.16 versions. But for now, that is not the case");
            getLogger().severe(" ");
            getLogger().severe("--- PLUGIN DISABLED ----");
            getLogger().severe(" ");
            getServer().getPluginManager().disablePlugin(this);
        }

        if(!getConfig().getBoolean("enabled")) {
            disablePlugin();
            return;
        }

        if(!DataUtils.getCustomYmlFile("data").exists()) {
            saveResource("data.yml", false);
        }

        if(!getConfig().getBoolean("persistent")) {
            TrackEntity.disableGame();

            try {
                Entity entityOverworld = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server.overworld.mobUUID")));
                Entity entityNether = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server.nether.mobUUID")));
                entityOverworld.remove();
                entityNether.remove();
            } catch (Exception e) { }

            DataUtils.setCustomYml("data", "server.overworld.centerLocation", new Location(ToggleActive.getMainWorld(), 0, 0, 0, 0, 0));
            DataUtils.setCustomYml("data", "server.overworld.mobUUID", 0);

            DataUtils.setCustomYml("data", "server.nether.centerLocation", new Location(Bukkit.getWorld(ToggleActive.getMainWorldString() + "_nether"), 0, 0, 0, 0, 0));
            DataUtils.setCustomYml("data", "server.nether.mobUUID", 0);
        }

        saveDefaultConfig();
        reloadConfig();

        registerCommand("mborder", new CommandManager());

        registerTabCompleter("mborder", new CommandManager());

        registerListener(new CommandListeners());
        registerListener(new EntityDeath());

        registerRunnable(new TrackEntity(), Main.getMainConfig().getLong("updateWorldBorderInterval"));

        System.out.println("Enabled MBorder [v" + getPlugin(Main.class).getDescription().getVersion() + "]");

    }

    @Override
    public void onDisable() { System.out.println("Disabled MBorder [v" + getPlugin(Main.class).getDescription().getVersion() + "]"); }

    public static Main getPlugin() { return plugin; }

    public static FileConfiguration getMainConfig() { return plugin.getConfig(); }

    public static void disablePlugin() {
        getPlugin(Main.class).getPluginLoader().disablePlugin(plugin);
        Bukkit.getLogger().severe(" ");
        Bukkit.getLogger().severe("The plugin MBorder has been disabled due to the setting set in the configuration file!");
        Bukkit.getLogger().severe(" ");
        Bukkit.getLogger().severe("Go to the config and change 'enabled' to 'true' to enable this plugin, then restart the server.");
        Bukkit.getLogger().severe(" ");
    }

    public static void registerCommand(String name, CommandExecutor commandExecutor) { plugin.getCommand(name).setExecutor(commandExecutor); }
    public static void registerTabCompleter(String name, TabCompleter tabCompleter) { plugin.getCommand(name).setTabCompleter(tabCompleter); }
    public static void registerListener(Listener listener) { plugin.getServer().getPluginManager().registerEvents(listener, plugin); }
    public static void registerRunnable(Runnable runnable, long period) { Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, period); }
}
