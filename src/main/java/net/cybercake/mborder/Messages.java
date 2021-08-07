package net.cybercake.mborder;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.regex.Matcher;

public class Messages {

    private static File configFile = new File(JavaPlugin.getPlugin(Main.class).getDataFolder() + "/messages.yml");
    private static FileConfiguration newConfig = YamlConfiguration.loadConfiguration(configFile);

    public static String getMessagesGeneral(String path) {
        return getMessages().getString("messages.general." + path);
    }

    public static String getMessagesMobs(String path) {
        return getMessages().getString("messages.mobs." + path);
    }

    public static String getMessagesCommands(String path, Object... replacements) {
        String newMsg = getMessages().getString("messages.commands." + path);
        if(newMsg == null) {
            return "messages.commands." + path;
        }
        int replacenum = 0;
        for(Object obj : replacements) {
            newMsg = newMsg.replace("{" + replacenum + "}", obj.toString());
            replacenum++;
        }
        return newMsg;
    }

    @NotNull
    public static FileConfiguration getMessages() {
        if (newConfig == null) {
            reloadMessages();
        }
        return newConfig;
    }

    public static void reloadMessages() {
        newConfig = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = JavaPlugin.getPlugin(Main.class).getResource("messages.yml");

        newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    public static void saveMessages() {
        try {
            getMessages().save(configFile);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save messages to " + configFile, ex);
        }
    }

    public static void saveDefaultMessages() {
        if (!configFile.exists()) {
            JavaPlugin.getPlugin(Main.class).saveResource("messages.yml", false);
        }
    }

}
