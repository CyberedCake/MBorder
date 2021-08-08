package net.cybercake.mborder;

import net.cybercake.mborder.Commands.CommandListeners;
import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommands.ToggleActive;
import net.cybercake.mborder.Listeners.*;
import net.cybercake.mborder.RepeatingTasks.RespawnMob;
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

    public static boolean justStarted;

    private static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;

        String serverVersion = getServer().getClass().getPackage().getName();
        String version = serverVersion.substring(serverVersion.lastIndexOf('.') + 1);
        if(!version.equals("v1_17_R1")) {
            getLogger().severe("--- PLUGIN DISABLED ----");
            getLogger().severe(" ");
            getLogger().severe(Utils.chat("The plugin MBorder only works on servers with versions &a1.17 or 1.17.1&c!"));
            getLogger().severe(" ");
            getLogger().severe("Hopefully later, we will support all 1.16 versions. But for now, that is not the case");
            getLogger().severe(" ");
            getLogger().severe("--- PLUGIN DISABLED ----");
            getServer().getPluginManager().disablePlugin(this);
        }

        if(!getConfig().getBoolean("enabled")) { disablePlugin(); return; }

        if(!DataUtils.getCustomYmlFile("data").exists()) { saveResource("data.yml", false); }

        if(!getConfig().getBoolean("persistent")) {
            TrackEntity.disableGame();

            DataUtils.setCustomYml("data", "server.overworld.centerLocation", new Location(ToggleActive.getMainWorld(), 0, 0, 0, 0, 0));
            DataUtils.setCustomYml("data", "server.overworld.mobUUID", 0);

            DataUtils.setCustomYml("data", "server.nether.centerLocation", new Location(Bukkit.getWorld(ToggleActive.getMainWorldString() + "_nether"), 0, 0, 0, 0, 0));
            DataUtils.setCustomYml("data", "server.nether.mobUUID", 0);
        }else if(getConfig().getBoolean("persistent")) {
            justStarted = true;
            if(Bukkit.getOnlinePlayers().size() > 0) {
                justStarted = false;
                PlayerJoinStart.justStarted();
            }
        }

        saveDefaultConfig();
        reloadConfig();
        DefaultItems.saveDefaultItems();
        DefaultItems.reloadItems();
        Messages.saveDefaultMessages();
        Messages.reloadMessages();

        registerCommand("mborder", new CommandManager());

        registerTabCompleter("mborder", new CommandManager());

        registerListener(new CommandListeners());
        registerListener(new EntityDeath());
        registerListener(new PlayerJoinStart());
        registerListener(new EntityDamageByEntity());
        registerListener(new PlayerJoinDefaultItems());
        registerListener(new PlayerJoinTeleport());

        registerRunnable(new TrackEntity(), Main.getMainConfig().getLong("updateWorldBorderInterval"));
        registerRunnable(new RespawnMob(), 40);

        logInfo("Enabled MBorder [v" + getPlugin(Main.class).getDescription().getVersion() + "]");

    }

    @Override
    public void onDisable() {
        DataUtils.setCustomYml("data", "server.lastUpdateCheck", 0);

        DataUtils.setCustomYml("data", "server.activeOnRestart", DataUtils.getCustomYmlBoolean("data", "server.active"));
        if(DataUtils.getCustomYmlBoolean("data", "server.active")) {
            try {
                Entity entityOverworld = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server.overworld.mobUUID")));
                Entity entityNether = Bukkit.getEntity(UUID.fromString(DataUtils.getCustomYmlString("data", "server.nether.mobUUID")));

                entityOverworld.remove();
                entityNether.remove();

                Bukkit.getLogger().info("MBorder successfully removed the entity in the overworld & nether");
                Bukkit.getLogger().info("[^^^ Note: This happens regardless of 'persistence' in the config! ^^^]");
            } catch (Exception e) {
                Bukkit.getLogger().severe("MBorder: An error occurred whilst attempting to remove mobs from worlds!");
                Bukkit.getLogger().severe(" ");
                Bukkit.getLogger().severe("Stack trace is below:");
                Utils.printBetterStackTrace(e);
            }
            TrackEntity.disableGame();
        }
        logInfo("Disabled MBorder [v" + getPlugin(Main.class).getDescription().getVersion() + "]");
    }

    public static void disablePlugin() {
        getPlugin(Main.class).getPluginLoader().disablePlugin(plugin);
        Bukkit.getLogger().severe(" ");
        Bukkit.getLogger().severe("The plugin MBorder has been disabled due to the setting set in the configuration file!");
        Bukkit.getLogger().severe(" ");
        Bukkit.getLogger().severe("Go to the config and change 'enabled' to 'true' to enable this plugin, then restart the server.");
        Bukkit.getLogger().severe(" ");
    }

    public static Main getPlugin() { return plugin; }
    public static FileConfiguration getMainConfig() { return plugin.getConfig(); }
    public static String getPluginPrefix() { return getPlugin(Main.class).getDescription().getPrefix(); }
    public static void logInfo(String msg) { Bukkit.getLogger().info("[" + getPluginPrefix() + "] " + msg); }
    public static void logWarn(String msg) { Bukkit.getLogger().warning("[" + getPluginPrefix() + "] " + msg); }
    public static void logError(String msg) { Bukkit.getLogger().severe(msg); }

    public static void registerCommand(String name, CommandExecutor commandExecutor) { plugin.getCommand(name).setExecutor(commandExecutor); }
    public static void registerTabCompleter(String name, TabCompleter tabCompleter) { plugin.getCommand(name).setTabCompleter(tabCompleter); }
    public static void registerListener(Listener listener) { plugin.getServer().getPluginManager().registerEvents(listener, plugin); }
    public static void registerRunnable(Runnable runnable, long period) { Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, period); }
}
