package net.cybercake.mborder.Commands;

import net.cybercake.mborder.Commands.SubCommands.DistanceBetween;
import net.cybercake.mborder.Commands.SubCommands.Help;
import net.cybercake.mborder.Commands.SubCommands.Reload;
import net.cybercake.mborder.Commands.SubCommands.ToggleActive;
import net.cybercake.mborder.Utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

// I am going to be using the CommandManager from https://gitlab.com/kodysimpson/command-manager-spigot, though slightly modified

public class CommandManager implements CommandExecutor, TabCompleter {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();
    public static ArrayList<String> emptyList = new ArrayList<>();

    private final static String pluginPermission = "mborder";
    private final static String pluginTitle = "MOVING BORDER";
    private final static String noPermissionMsg = "&cYou don't have permission to use this!";

    public CommandManager() {
        subcommands.add(new Help());
        subcommands.add(new Reload());
        subcommands.add(new DistanceBetween());
        subcommands.add(new ToggleActive());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {
        if(getSubCommandsOnlyWithPerms(sender).size() <= 1) {
            sender.sendMessage(Utils.chat(noPermissionMsg));
        }else if(args.length == 0) {
            printHelpMsg(sender);
        }else if(args.length > 0) {
            boolean ran = false;
            if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("info")) {
                printHelpMsg(sender);
            }else{
                for (SubCommand cmd : getSubcommands()) {
                    boolean use = false;
                    if(args[0].equalsIgnoreCase(cmd.getName())) {
                        use = true;
                    }
                    if(!use) {
                        for(String alias : cmd.getAliases()) {
                            if(args[0].equalsIgnoreCase(alias)) {
                                use = true;
                            }
                        }
                    }
                    if (use) {
                        if(sender.hasPermission(pluginPermission + ".*")) {
                            cmd.perform(sender, args, command);
                        }else if (cmd.getPermission().equalsIgnoreCase("")) {
                            cmd.perform(sender, args, command);
                        } else if (!cmd.getPermission().equalsIgnoreCase("") && sender.hasPermission(cmd.getPermission())) {
                            cmd.perform(sender, args, command);
                        } else {
                            sender.sendMessage(Utils.chat(noPermissionMsg));
                        }
                        ran = true;
                    }
                }
                if(!ran) {
                    sender.sendMessage(Utils.chat("&cUnknown sub-command: &8" + args[0]));
                }
            }
        }



        return true;
    }

    public SubCommand getSubCommand(String subCommandName) {
        for(SubCommand command : getSubcommands()) {
            if(command.getName().equalsIgnoreCase(subCommandName)) {
                return command;
            }
        }
        return null;
    }

    public void printHelpMsg(CommandSender sender) {
        if(sender instanceof Player) {
            sender.sendMessage(Utils.getSeperator(ChatColor.BLUE));
        }
        Utils.sendCenteredMessage(sender, "&d&l" + pluginTitle + " COMMANDS:");
        for(String cmdStr : getSubCommandsOnlyWithPerms(sender)) {
            if(sender.hasPermission(pluginPermission + ".*")) {
                printHelpMsgSpecific(sender, getSubCommand(cmdStr).getDescription(), getSubCommand(cmdStr).getUsage(), getSubCommand(cmdStr).getPermission(), Arrays.toString(getSubCommand(cmdStr).getAliases()));
            }else if (getSubCommand(cmdStr).getPermission().equalsIgnoreCase("")) {
                printHelpMsgSpecific(sender, getSubCommand(cmdStr).getDescription(), getSubCommand(cmdStr).getUsage(), getSubCommand(cmdStr).getPermission(), Arrays.toString(getSubCommand(cmdStr).getAliases()));
            } else if (!getSubCommand(cmdStr).getPermission().equalsIgnoreCase("") && sender.hasPermission(getSubCommand(cmdStr).getPermission())) {
                printHelpMsgSpecific(sender, getSubCommand(cmdStr).getDescription(), getSubCommand(cmdStr).getUsage(), getSubCommand(cmdStr).getPermission(), Arrays.toString(getSubCommand(cmdStr).getAliases()));
            }
        }
        if(sender instanceof Player) {
            sender.sendMessage(Utils.getSeperator(ChatColor.BLUE));
        }
    }

    private static void printHelpMsgSpecific(CommandSender sender, String description, String usage, String permission, String aliases) {
        if(permission.equalsIgnoreCase("")) {
            permission = "Everyone";
        }

        String ifAliases = "";
        if(!aliases.equalsIgnoreCase("[]")) {
            ifAliases = "\n&6Aliases: &f" + aliases;
        }
        BaseComponent component = new TextComponent(Utils.chat("&b" + usage));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.chat("&6Command: &f" + usage + "\n&6Description: &f" + description + "\n&6Permission: &f" + permission + ifAliases)).create()));
        sender.spigot().sendMessage(component);
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

    public ArrayList<String> getSubCommandsOnlyWithPerms(CommandSender sender) {
        ArrayList<String> cmdNames = new ArrayList<>();
        for(SubCommand cmd : getSubcommands()) {
            if(sender.hasPermission(pluginPermission + ".*")) {
                cmdNames.add(cmd.getName());
            }else if(cmd.getPermission().equalsIgnoreCase("")) {
                cmdNames.add(cmd.getName());
            }else if(!cmd.getPermission().equalsIgnoreCase("") && sender.hasPermission(cmd.getPermission())) {
                cmdNames.add(cmd.getName());
            }
        }
        return cmdNames;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(getSubCommandsOnlyWithPerms(sender).size() <= 1) {
            return emptyList;
        }else if(args.length <= 1) {
            return createReturnList(getSubCommandsOnlyWithPerms(sender), args[0]);
        }else{
            try {
                for(SubCommand cmd : getSubcommands()) {
                    for(int i = 1; i < 100; i++) {
                        boolean use = false;
                        if(args[0].equalsIgnoreCase(cmd.getName())) {
                            use = true;
                        }
                        if(!use) {
                            for(String cmdAlias : cmd.getAliases()) {
                                if(args[0].equalsIgnoreCase(cmdAlias)) {
                                    use = true;
                                }
                            }
                        }
                        if(use) {
                            if(args.length - 1 == i) {
                                if(sender.hasPermission(pluginPermission + ".*")) {
                                    return createReturnList(cmd.tab(sender, args), args[i]);
                                }else if(cmd.getPermission().equalsIgnoreCase("")) {
                                    return createReturnList(cmd.tab(sender, args), args[i]);
                                }else if(!cmd.getPermission().equalsIgnoreCase("") && sender.hasPermission(cmd.getPermission())) {
                                    return createReturnList(cmd.tab(sender, args), args[i]);
                                }else{
                                    return emptyList;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                return emptyList;
            }
        }
        return emptyList;
    }

    public static List<String> createReturnList(List<String> list, String string) {
        if (string.length() == 0) {
            return list;
        }

        String input = string.toLowerCase(Locale.ROOT);
        List<String> returnList = new ArrayList<>();

        for (String item : list) {
            if (item.toLowerCase(Locale.ROOT).contains(input)) {
                returnList.add(item);
            } else if (item.equalsIgnoreCase(input)) {
                return emptyList;
            }
        }

        return returnList;
    }
}
