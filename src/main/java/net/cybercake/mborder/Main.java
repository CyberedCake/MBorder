package net.cybercake.mborder;

import net.cybercake.mborder.Commands.CommandListeners;
import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Utils.DataUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    // vv "luckperms" may never be used but it's to tell the Utils class that luckperms does not exist
    public static boolean luckperms = false;

    private static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;

        saveResource("data.yml", false);

        if(!getConfig().getBoolean("enabled")) {
            disablePlugin();
            return;
        }
        if(!getConfig().getBoolean("persistent")) {
            DataUtils.setCustomYml("data", "server.active", false);
        }

        saveDefaultConfig();
        reloadConfig();

        registerCommand("mborder", new CommandManager());

        registerTabCompleter("mborder", new CommandManager());

        registerListener(new CommandListeners());

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
