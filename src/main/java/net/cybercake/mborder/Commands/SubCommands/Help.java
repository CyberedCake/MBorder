package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Help extends SubCommand {

    public Help() {
        super("help", "", "Prints this help message.", "/mborder help", "");
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        // Overridden and moved to main CommandManager class
        sender.sendMessage(Utils.chat("&cAn error occurred!"));
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return CommandManager.emptyList;
    }
}
