package net.cybercake.mborder.Commands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class CommandListeners implements Listener {

    @EventHandler
    public void onCommandSendEvent(PlayerCommandSendEvent e) {
        Player p = e.getPlayer();

        CommandManager manager = new CommandManager();

        e.getCommands().remove("mborder:mborder");
        e.getCommands().remove("mborder:movingborder");
        e.getCommands().remove("mborder:mb");

        if(manager.getSubCommandsOnlyWithPerms(p).size() <= 1) {
            e.getCommands().remove("movingborder");
            e.getCommands().remove("mborder");
            e.getCommands().remove("mb");
        }
    }

}
