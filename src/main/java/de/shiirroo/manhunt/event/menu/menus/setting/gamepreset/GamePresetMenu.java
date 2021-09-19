package de.shiirroo.manhunt.event.menu.menus.setting.gamepreset;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.PlayerMenuUtility;
import de.shiirroo.manhunt.event.menu.menus.setting.SettingsMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.presets.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.HashMap;
import java.util.Objects;

public class GamePresetMenu extends Menu {

    public static GamePreset preset = new Default();
    public static HashMap<String, Object> customHashMap;

    public GamePresetMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ChatColor.BLUE +"Game Preset";
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.CHEST;
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenuClickEvent(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if (p.isOp()) {
            if (e.getWhoClicked().isOp() && StartGame.gameRunning == null && Objects.requireNonNull(e.getCurrentItem()).getItemMeta().lore() != null) {
                GamePreset dream = new Dream();
                GamePreset custom = new Custom();
                GamePreset defaultpreset = new Default();
                GamePreset hardcore = new Hardcore();
                GamePreset turtle = new Turtle();
                if ( Objects.requireNonNull(e.getCurrentItem().lore()).equals(Objects.requireNonNull(dream.displayItem().lore())) && !preset.presetName().equals(dream.presetName())) {
                    p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 2.0f, 3.0f);
                    preset = dream;
                    System.out.println(ManHuntPlugin.getprefix() + "Game preset : Dream");
                    preset.setConfig();
                    Ready.ready.cancelVote();
                } else if (Objects.requireNonNull(e.getCurrentItem().lore()).equals(Objects.requireNonNull(custom.displayItem().lore())) && checkCustom()) {
                    if(checkCustom()) {
                        System.out.println(ManHuntPlugin.getprefix() + "Game preset : Custom");
                        p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 2.0f, 3.0f);
                    } else {
                        p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.5f, 3.5f);
                    }
                    preset = custom;
                    preset.setConfig();
                    Ready.ready.cancelVote();
                } else if (Objects.requireNonNull(e.getCurrentItem().lore()).equals(Objects.requireNonNull(defaultpreset.displayItem().lore()))  && !preset.presetName().equals(defaultpreset.presetName())) {
                    preset = defaultpreset;
                    preset.setConfig();
                    System.out.println(ManHuntPlugin.getprefix() + "Game preset : Default");
                    p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 2.0f, 3.0f);
                    Ready.ready.cancelVote();
                } else if (Objects.requireNonNull(e.getCurrentItem().lore()).equals(Objects.requireNonNull(hardcore.displayItem().lore()))  && !preset.presetName().equals(hardcore.presetName())) {
                    preset = hardcore;
                    preset.setConfig();
                    System.out.println(ManHuntPlugin.getprefix() + "Game preset : Hardcore");
                    p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 2.0f, 3.0f);
                    Ready.ready.cancelVote();
                } else if (Objects.requireNonNull(e.getCurrentItem().lore()).equals(Objects.requireNonNull(turtle.displayItem().lore()))  && !preset.presetName().equals(turtle.presetName())) {
                    preset = turtle;
                    preset.setConfig();
                    System.out.println(ManHuntPlugin.getprefix() + "Game preset : Turtle");
                    p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 2.0f, 1.0f);
                    Ready.ready.cancelVote();
                }
            }

        }
        if (Objects.equals(e.getCurrentItem(), BACK_ITEM)) {
            back();
        }

        Bukkit.getOnlinePlayers().forEach(GamePresetMenu::setFooderPreset);
        Events.playerMenu.values().forEach(Menu::setMenuItems);
        SettingsMenu.GamePreset.values().forEach(Menu::setMenuItems);
        SettingsMenu.ConfigMenu.values().forEach(Menu::setMenuItems);
    }

    @Override
    public void handlePlayerDropItemEvent(PlayerDropItemEvent e) throws MenuManagerNotSetupException, MenuManagerException {

    }

    @Override
    public void handlePlayerInteractEvent(PlayerInteractEvent e) throws MenuManagerNotSetupException, MenuManagerException {

    }

    @Override
    public void setMenuItems() {
        inventory.setItem(10, CommingSoon());
        inventory.setItem(11, new Dream().displayItem());
        inventory.setItem(12, new Turtle().displayItem());
        inventory.setItem(13, new Hardcore().displayItem());
        inventory.setItem(14, new Default().displayItem());
        inventory.setItem(15, new Custom().displayItem());
        inventory.setItem(16, CommingSoon());
        inventory.setItem(31, BACK_ITEM);

        setFillerGlass(true);
    }

    public static void  setFooderPreset(Player player){
        String[] name = GamePresetMenu.preset.presetName().split("\\.");
        player.sendPlayerListFooter(Component.text(ChatColor.GOLD + "Game Preset: " + ChatColor.GREEN+  name[name.length-1]));
    }

    public static boolean checkCustom(){
        GamePreset custom = new Custom();
            for (String s : custom.makeConfig().keySet()) {
                if (!ManHuntPlugin.getConfigCreators(s).getConfigSetting().equals(custom.makeConfig().get(s))) {
                    return true;
                }
            }
            return false;
        }

}
