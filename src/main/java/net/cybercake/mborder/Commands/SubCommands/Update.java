package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.DataUtils;
import net.cybercake.mborder.Utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Update extends SubCommand {

    public Update() {
        super("update", "mborder.command.update", "Checks if there is an update to the plugin. If there is, it will provide a download link to the latest version.", "/mborder update", new String[]{""});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        if(args.length > 1) {
            if(args[1].equals("reset")) {
                DataUtils.setCustomYml("data", "server.lastUpdateCheck", 0);
                sender.sendMessage(Utils.chat("&cReset the last update check!"));
                return;
            }
        }

        if((Utils.getUnix() - DataUtils.getCustomYmlLong("data", "server.lastUpdateCheck")) > 600) {
            sender.sendMessage(Utils.chat("&f&oChecking version, please wait..."));
        }

        String update = getLatestUpdate();
        TextComponent running = new TextComponent(Utils.chat("&fThis server is running MBorder version " + Main.getPlugin(Main.class).getDescription().getVersion() + ". Newer versions can be found "));
        TextComponent newUpdateLink = new TextComponent(Utils.chat("&f&o&nhere!"));
        TextComponent newestUpdate = new TextComponent(Utils.chat(" &fThe latest version is " + update + "."));
        newUpdateLink.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.chat("&fClick here to visit the &6GitHub &freleases page!\n&7&o(it's where all the new updates are)")).create()));
        newUpdateLink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/CyberedCake/MBorder/releases"));
        running.addExtra(newUpdateLink);
        running.addExtra(newestUpdate);
        sender.spigot().sendMessage(running);
        if (Main.getPlugin(Main.class).getDescription().getVersion().equalsIgnoreCase(update)){
            sender.sendMessage(Utils.chat("&aYou are running the latest version"));
        }else if(!Main.getPlugin(Main.class).getDescription().getVersion().equalsIgnoreCase(update)) {
            sender.sendMessage(Utils.chat("&eYou are running an outdated version"));
            TextComponent updateMe = new TextComponent(Utils.chat("&7&oUpdate me by clicking here!"));
            updateMe.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.chat("&fClick here to visit the &6GitHub &freleases page!\n&7&o(it's where all the new updates are)")).create()));
            updateMe.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/CyberedCake/MBorder/releases"));
            if(sender instanceof Player) {
                sender.spigot().sendMessage(updateMe);
            }
        }else {
            sender.sendMessage(Utils.chat("&cAn error occurred whilst checking for updates for MBorder!"));
        }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return CommandManager.emptyList;
    }

    public static String getLatestUpdate() {
        if((Utils.getUnix() - DataUtils.getCustomYmlLong("data", "server.lastUpdateCheck")) <= 600) {
            return DataUtils.getCustomYmlString("data", "server.latestVersion");
        }


        String line = null;
        try {
            URL url = new URL("https://raw.githubusercontent.com/CyberedCake/MBorder/master/pom.xml");

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            int lineNum = 0;
            while ((line = in.readLine()) != null) {
                if(lineNum == 8) {
                    line = line.substring(13, 18);
                    break;
                }
                lineNum++;
            }
            in.close();
        } catch (MalformedURLException e) {
            Bukkit.getLogger().severe("An error occurred whilst checking for updates! (MalformedURLException)");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe("Stack trace below:");
            Utils.printBetterStackTrace(e);
            return "ERROR:MALFORMEDURLEXCEPTION";
        } catch (IOException e) {
            Bukkit.getLogger().severe("An error occurred whilst checking for updates! (IOException)");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe("Stack trace below:");
            Utils.printBetterStackTrace(e);
            return "ERROR:IOEXCEPTION";
        } catch (Exception e) {
            Bukkit.getLogger().severe("An error occurred whilst checking for updates! (Exception)");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe("Stack trace below:");
            Utils.printBetterStackTrace(e);
            return "ERROR:EXCEPTION";
        }

        DataUtils.setCustomYml("data", "server.lastUpdateCheck", Utils.getUnix());
        DataUtils.setCustomYml("data", "server.latestVersion", line);

        return line;
    }
}
