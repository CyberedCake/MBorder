package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Start extends SubCommand {

    public Start() {
        super("toggleactive", "wborder.toggleactive", "Start or stop the game.", "/mborder toggleactive", new String[]{"start", "stop"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        // work in progress
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return CommandManager.emptyList;
    }
}
