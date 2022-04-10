package de.shiirroo.manhunt.event.menu.menus.setting.gamepreset;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.PlayerMenuUtility;
import de.shiirroo.manhunt.event.menu.menus.setting.SettingsMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.presets.*;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Objects;

public class GamePresetMenu extends Menu {

    public static GamePreset preset = new Default();
    public static HashMap<String, Object> customHashMap;

    public GamePresetMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public static void setFooderPreset(Player player) {
        String[] name = GamePresetMenu.preset.presetName().split("\\.");
        player.setPlayerListFooter(ChatColor.GOLD + "Game Preset: " + ChatColor.GREEN + name[name.length - 1]);
    }

    public static boolean checkCustom() {
        GamePreset custom = new Custom();
        for (String s : custom.makeConfig().keySet()) {
            if (!ManHuntPlugin.getGameData().getGameConfig().getConfigCreators(s).getConfigSetting().equals(custom.makeConfig().get(s))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getMenuName() {
        return ChatColor.BLUE + "Game Preset";
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
        Player p = (Player) e.getWhoClicked();
        if (p.isOp() && Ready.ready != null) {
            if (e.getWhoClicked().isOp() && !ManHuntPlugin.getGameData().getGameStatus().isGame()
                    && Objects.requireNonNull(e.getCurrentItem()).getItemMeta().getLore() != null) {
                GamePreset dream = new Dream();
                GamePreset custom = new Custom();
                GamePreset defaultpreset = new Default();
                GamePreset hardcore = new Hardcore();
                GamePreset turtle = new Turtle();
                if (e.getCurrentItem().equals(custom.displayItem()) && checkCustom()) {
                    if (checkCustom()) {
                        Bukkit.getLogger().info(ManHuntPlugin.getprefix() + "Game preset : Custom");
                        p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 2.0f, 3.0f);
                    } else {
                        p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.5f, 3.5f);
                    }
                    preset = custom;
                    preset.setConfig();
                    Ready.ready.cancelVote();

                } else if (e.getCurrentItem().equals(defaultpreset.displayItem()) && !preset.presetName().equals(defaultpreset.presetName())) {
                    preset = defaultpreset;
                    preset.setConfig();
                    Bukkit.getLogger().info(ManHuntPlugin.getprefix() + "Game preset : Default");
                    p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 2.0f, 3.0f);
                    Ready.ready.cancelVote();

                } else if (e.getCurrentItem().equals(hardcore.displayItem()) && !preset.presetName().equals(hardcore.presetName())) {
                    preset = hardcore;
                    preset.setConfig();
                    Bukkit.getLogger().info(ManHuntPlugin.getprefix() + "Game preset : Hardcore");
                    p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 2.0f, 3.0f);
                    Ready.ready.cancelVote();

                } else if (e.getCurrentItem().equals(turtle.displayItem()) && !preset.presetName().equals(turtle.presetName())) {
                    preset = turtle;
                    preset.setConfig();
                    Bukkit.getLogger().info(ManHuntPlugin.getprefix() + "Game preset : Turtle");
                    p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 2.0f, 1.0f);
                    Ready.ready.cancelVote();

                } else if (Objects.requireNonNull(e.getCurrentItem()).getItemMeta() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta() instanceof SkullMeta i) {
                    if (Objects.equals(i.getOwner(), "Dream")) {
                        p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 2.0f, 3.0f);
                        preset = dream;
                        Bukkit.getLogger().info(ManHuntPlugin.getprefix() + "Game preset : Dream");
                        preset.setConfig();
                        Ready.ready.cancelVote();
                    }
                }
            }


        }
        if (e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(FILLER_GLASS.getType()) &&
                !e.getCurrentItem().getType().equals(BACK_ITEM.getType())
        ) {
            Bukkit.getOnlinePlayers().forEach(
                    player -> ManHuntPlugin.getGameData().getPlayerData().setRole(player, ManHuntRole.Unassigned, ManHuntPlugin.getTeamManager())
            );

        }


        if (Objects.equals(e.getCurrentItem(), BACK_ITEM)) {
            back();
        }
        ManHuntPlugin.playerMenu.values().forEach(Menu::setMenuItems);
        Bukkit.getOnlinePlayers().forEach(GamePresetMenu::setFooderPreset);
        SettingsMenu.GamePreset.values().forEach(Menu::setMenuItems);
        SettingsMenu.ConfigMenu.values().forEach(Menu::setMenuItems);
        for (Menu menu : SettingsMenu.ConfigMenu.values()) {
            menu.setMenuItems();
        }
    }

    @Override
    public void handlePlayerDropItemEvent(PlayerDropItemEvent e) {

    }

    @Override
    public void handlePlayerInteractEvent(PlayerInteractEvent e) {

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

        setFillerGlass(false);
    }

}
