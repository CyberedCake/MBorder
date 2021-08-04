package net.cybercake.mborder;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.logging.Level;

public class DefaultItems {

    private static File configFile = new File(JavaPlugin.getPlugin(Main.class).getDataFolder() + "/default-items.yml");
    private static FileConfiguration newConfig = YamlConfiguration.loadConfiguration(configFile);

    @NotNull
    public static FileConfiguration getItems() {
        if (newConfig == null) {
            reloadItems();
        }
        return newConfig;
    }

    public static void reloadItems() {
        newConfig = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = JavaPlugin.getPlugin(Main.class).getResource("default-items.yml");
        if (defConfigStream == null) {
            return;
        }

        newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    public static void saveItems() {
        try {
            getItems().save(configFile);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    public static void saveDefaultItems() {
        if (!configFile.exists()) {
            JavaPlugin.getPlugin(Main.class).saveResource("default-items.yml", false);
        }
    }

}
