package de.shiirroo.manhunt.event.menu.menus.setting.gamemode;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.event.menu.menus.setting.SettingsMenu;
import de.shiirroo.manhunt.utilis.Utilis;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.*;

public abstract class CustomGameMode implements Serializable {

    public Object value;

    public CustomGameMode() {
        value = defaultValue();
    }

    protected String presetName() {
        return this.getClass().getName();
    }

    public abstract ItemStack displayItem();

    public abstract Object defaultValue();

    protected abstract Object minValue();

    protected abstract Object maxValue();

    public Object getValue() {
        return value;
    }

    public String DisplayName() {
        String[] name = presetName().split("\\.");
        return name[name.length - 1];
    }


    public abstract void init(Player p);

    public abstract void execute();

    public void openAnvilGUI(Player player, String addon) {
        new AnvilGUI.Builder()
                .onClick((i, stateSnapshot) -> {
                    Player p = stateSnapshot.getPlayer();
                    String text = stateSnapshot.getText().replace(DisplayNameToLong(DisplayName()) + " ", "");
                    if (Utilis.isNumeric(text)) {
                        int input = Integer.parseInt(text);
                        if (input >= (int) minValue() && input <= (int) maxValue()) {
                            for (UUID uuid : SettingsMenu.GameMode.keySet()) {
                                SettingsMenu.GameMode.get(uuid).setMenuItems();
                            }
                            p.sendMessage(ManHuntPlugin.getprefix() + ChatColor.GOLD + value + ChatColor.GRAY + " switched to" + " " + ChatColor.GREEN + input + " " + ChatColor.GRAY + addon);
                            value = input;
                            if (SettingsMenu.GameMode.get(p.getUniqueId()) != null)
                                SettingsMenu.GameMode.get(p.getUniqueId()).open();
                            return List.of(AnvilGUI.ResponseAction.close());
                        }
                        p.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED + "This is an invalid input." + ChatColor.GRAY + " Enter a number between " + ChatColor.GOLD + minValue() + ChatColor.GRAY + " - " + ChatColor.GOLD + maxValue());
                    } else {
                        p.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED + "This is an invalid input");
                    }
                    return List.of(AnvilGUI.ResponseAction.replaceInputText(ChatColor.GRAY + DisplayNameToLong(DisplayName()) + " " + ChatColor.GREEN + value));
                })
                .text(ChatColor.GRAY + DisplayNameToLong(DisplayName()) + " " + ChatColor.GREEN + value)
                .itemLeft(displayItem())
                .title(ChatColor.DARK_GRAY + checkSpaces(DisplayName(), addon) + ChatColor.DARK_PURPLE + minValue() + ChatColor.DARK_GRAY + " - " + ChatColor.DARK_PURPLE + maxValue() + " " + addon)
                .plugin(ManHuntPlugin.getPlugin())
                .open(player);
    }


    private String DisplayNameToLong(String name) {
        if (name.length() > 9) {
            char[] charArray = name.toCharArray();
            StringBuilder nameBuilder = new StringBuilder();
            for (char s : charArray) {
                if (Character.isUpperCase(s)) {
                    nameBuilder.append(s);
                }
            }
            name = nameBuilder.toString();
        }
        return name;
    }

    private String checkSpaces(String name, String addon) {
        name += ": ";
        if ((name + value + minValue() + " - " + maxValue() + " " + addon).length() > 21) {
            StringBuilder nameBuilder = new StringBuilder(name);
            while (nameBuilder.length() < 24) {
                nameBuilder.append(" ");
            }
            name = nameBuilder.toString();
        }
        return name;
    }
}

