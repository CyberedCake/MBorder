package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Messages;
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
import java.util.Arrays;
import java.util.List;

public class Update extends SubCommand {

    public static String version = "1.0.0";
    public static int protocol = 1;

    public Update() {
        super("update", "mborder.command.update", Messages.getMessagesCommands("update.description"), "/mborder update", new String[]{"version", "ver"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        if(args.length > 1) {
            if(args[1].equalsIgnoreCase("checkagain")) {
                DataUtils.setCustomYml("data", "server.latest.lastUpdateCheck", 0);
                sender.sendMessage(Utils.chat(Messages.getMessagesCommands("update.checkAgain")));
                if(sender instanceof Player) {
                    Player p = (Player) sender;
                    p.performCommand("movingborder version");
                }
                return;
            }
        }

        if((Utils.getUnix() - DataUtils.getCustomYmlLong("data", "server.latest.lastUpdateCheck")) > 600) {
            sender.sendMessage(Utils.chat(Messages.getMessagesCommands("update.checkingVersion")));
        }

        int latestProtocol = getLatestUpdate();
        String latestVersion = DataUtils.getCustomYmlString("data", "server.latest.version");

        if(getProtocolDiff(latestProtocol + "", protocol + "") == -1) {
            sender.sendMessage(Utils.chat(Messages.getMessagesCommands("update.customVersion")));
            return;
        }

        TextComponent running = new TextComponent(Utils.chat("&fThis server is running MBorder version " + version + ". Newer versions can be found "));
        TextComponent newUpdateLink = new TextComponent(Utils.chat("&f&o&nhere!"));
        TextComponent newestUpdate = new TextComponent(Utils.chat(" &fThe latest version is " + latestVersion + "."));
        newUpdateLink.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.chat("&fClick here to visit the &6GitHub &freleases page!\n&7&o(it's where all the new updates are)")).create()));
        newUpdateLink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/CyberedCake/MBorder/releases"));
        running.addExtra(newUpdateLink);
        running.addExtra(newestUpdate);
        sender.spigot().sendMessage(running);

        if(latestProtocol > protocol) {
            sender.sendMessage(Utils.chat(Messages.getMessagesCommands("update.outdatedVersion", getProtocolDiff(latestProtocol + "", protocol + ""))));
        }else if(latestProtocol == protocol) {
            sender.sendMessage(Utils.chat(Messages.getMessagesCommands("update.runningLatest")));
        }else{
            sender.sendMessage(Utils.chat(Messages.getMessagesCommands("update.errorOccurred")));
        }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        if(args.length <= 2) {
            return CommandManager.createReturnList(Arrays.asList("checkagain"), args[1]);
        }
        return CommandManager.emptyList;
    }

    public int getProtocolDiff(String newestVersion, String currentVersion) {
        if((!CoordsCheck.isInt(newestVersion)) || (!CoordsCheck.isInt(currentVersion))) {
            return -1;
        }
        return Integer.parseInt(newestVersion) - Integer.parseInt(currentVersion);
    }

    public int getLatestUpdate() {
        if((Utils.getUnix() - DataUtils.getCustomYmlLong("data", "server.latest.lastUpdateCheck")) <= 600) {
            return DataUtils.getCustomYmlInt("data", "server.latest.protocol");
        }

        String latest = null;
        String protocol = null;
        try {
            URL url = new URL("https://raw.githubusercontent.com/CyberedCake/MBorder/master/src/main/resources/latest.txt");

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            int line = 0;
            String lineStr = "";
            while ((lineStr = in.readLine()) != null) {
                if(line == 0) {
                    latest = lineStr.replace("latestVersion=", "");
                } else if (line == 1) {
                    protocol = lineStr.replace("versionProtocol=", "");
                }
                line++;
            }
            in.close();
        } catch (MalformedURLException e) {
            Bukkit.getLogger().severe("An error occurred whilst checking for updates! (MalformedURLException)");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe("Stack trace below:");
            Utils.printBetterStackTrace(e);
            return -1;
        } catch (IOException e) {
            Bukkit.getLogger().severe("An error occurred whilst checking for updates! (IOException)");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe("Stack trace below:");
            Utils.printBetterStackTrace(e);
            return -1;
        } catch (Exception e) {
            Bukkit.getLogger().severe("An error occurred whilst checking for updates! (Exception)");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe("Stack trace below:");
            Utils.printBetterStackTrace(e);
            return -1;
        }

        DataUtils.setCustomYml("data", "server.latest.lastUpdateCheck", Utils.getUnix());
        DataUtils.setCustomYml("data", "server.latest.version", latest);
        DataUtils.setCustomYml("data", "server.latest.protocol", Integer.parseInt(protocol));

        return Integer.parseInt(protocol);
    }
}
