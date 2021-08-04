package net.cybercake.mborder.Commands.SubCommands;

import net.cybercake.mborder.Commands.CommandManager;
import net.cybercake.mborder.Commands.SubCommand;
import net.cybercake.mborder.DefaultItems;
import net.cybercake.mborder.Main;
import net.cybercake.mborder.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.ChatPaginator;

import java.util.ArrayList;
import java.util.List;

public class DefaultItemsCMD extends SubCommand {

    public DefaultItemsCMD() {
        super("defaultitems", "mborder.command.defaultitems", "Gives you the default starter items, defined in default-items.yml", "/mborder defaultitems [player]", new String[]{"starteritems", "items"});
    }

    @Override
    public void perform(CommandSender sender, String[] args, Command command) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(!DefaultItems.getItems().getBoolean("enabled")) {
                p.sendMessage(Utils.chat("&cGiving items to new players has been disabled in the &6default-items.yml&c, ask an administrator with the correct permissions to change it."));
                return;
            }
            if(getCustomItems().size() <= 0) {
                p.sendMessage(Utils.chat("&cCould not detect any custom items in the &6default-items.yml&c, try adding some?"));
                return;
            }

            if(args.length < 2) {
                for(ItemStack items : getCustomItems()) {
                    p.getInventory().addItem(items);
                }
            } else if (args.length > 1) {
                Player target = Bukkit.getPlayerExact(args[1]);
                if(target == null) {p.sendMessage(Utils.chat("&cInvalid online player! &7" + this.getUsage())); return;}

                p.sendMessage(Utils.chat("&fYou gave &e" + target.getName() + " &fthe default items!"));
                for(ItemStack items : getCustomItems()) {
                    target.getInventory().addItem(items);
                }
            }
        }else{
            Main.logError("Only players can execute this sub-command!");
        }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        if(args.length <= 2) {
            ArrayList<String> names = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> names.add(player.getName()));
            return names;
        }
        return CommandManager.emptyList;
    }

    public static ArrayList<Material> getCustomItemMaterials() {
        ArrayList<Material> items = new ArrayList<>();
        for(String str : DefaultItems.getItems().getConfigurationSection("starterItems").getKeys(false)) {
            items.add(Material.valueOf(str));
        }
        return items;
    }

    public static ArrayList<ItemStack> getCustomItems() {
        ArrayList<ItemStack> items = new ArrayList<>();
        for(String str : DefaultItems.getItems().getConfigurationSection("starterItems").getKeys(false)) {
            ItemStack newItem = new ItemStack(Material.valueOf(str), DefaultItems.getItems().getInt("starterItems." + str + ".amount"));
            ItemMeta newItemMeta = newItem.getItemMeta();

            // Default items: Custom name
            if(DefaultItems.getItems().getString("starterItems." + str + ".customName") != null) {
                newItemMeta.setDisplayName(Utils.chat(DefaultItems.getItems().getString("starterItems." + str + ".customName")));
            }

            // Default items: Lore
            List<String> lore = new ArrayList<>();
            if(DefaultItems.getItems().getStringList("starterItems." + str + ".customLore").size() > 0) {
                for(String loreActual : DefaultItems.getItems().getStringList("starterItems." + str + ".customLore")) {
                    if(!loreActual.startsWith("paginate:")) {
                        lore.add(Utils.chat(loreActual));
                    }else{
                        loreActual = loreActual.substring(9);
                        for(String betterLore : ChatPaginator.wordWrap(Utils.chat("&5&o" + loreActual), 40)) {
                            lore.add(Utils.chat(betterLore));
                        }
                    }
                }

            }

            // Default items: no NBT
            if(DefaultItems.getItems().getBoolean("starterItems." + str + ".hideNBT")) {
                newItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
            }

            newItemMeta.setLore(lore);
            newItem.setItemMeta(newItemMeta);

            items.add(newItem);

        }
        return items;
    }

}
