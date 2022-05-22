package de.shiirroo.manhunt.event.menu.menus.setting.gamemode;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.PlayerMenuUtility;
import de.shiirroo.manhunt.event.menu.menus.setting.SettingsMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.Objects;

public class GameModeMenu extends Menu implements Serializable {


    public GameModeMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Game Mode";
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
        ItemStack currentItem = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();
        if (p.isOp() && Ready.ready != null) {
            if (Objects.equals(currentItem, ManHuntPlugin.getGameData().getGameMode().getWorldBorderSize().displayItem())) {
                ManHuntPlugin.getGameData().getGameMode().getWorldBorderSize().init(p);
            } else if (Objects.equals(currentItem, ManHuntPlugin.getGameData().getGameMode().getRandomEffects().displayItem())) {
                ManHuntPlugin.getGameData().getGameMode().getRandomEffects().init(p);
            } else if (Objects.equals(currentItem, ManHuntPlugin.getGameData().getGameMode().getRandomTP().displayItem())) {
                ManHuntPlugin.getGameData().getGameMode().getRandomTP().init(p);
            } else if (Objects.equals(currentItem, ManHuntPlugin.getGameData().getGameMode().getRandomBlocks().displayItem())) {
                ManHuntPlugin.getGameData().getGameMode().getRandomBlocks().init(p);
            } else if (Objects.equals(currentItem, ManHuntPlugin.getGameData().getGameMode().getRandomItems().displayItem())) {
                ManHuntPlugin.getGameData().getGameMode().getRandomItems().init(p);
            }
        }
        if (Objects.equals(e.getCurrentItem(), BACK_ITEM)) {
            back();
        }
        SettingsMenu.GameMode.values().forEach(Menu::setMenuItems);
    }

    @Override
    public void handlePlayerDropItemEvent(PlayerDropItemEvent e) {

    }

    @Override
    public void handlePlayerInteractEvent(PlayerInteractEvent e) {

    }

    @Override
    public void setMenuItems() {
        inventory.setItem(11, ManHuntPlugin.getGameData().getGameMode().getWorldBorderSize().displayItem());
        inventory.setItem(12, ManHuntPlugin.getGameData().getGameMode().getRandomEffects().displayItem());
        inventory.setItem(13, ManHuntPlugin.getGameData().getGameMode().getRandomTP().displayItem());
        inventory.setItem(14, ManHuntPlugin.getGameData().getGameMode().getRandomBlocks().displayItem());
        inventory.setItem(15, ManHuntPlugin.getGameData().getGameMode().getRandomItems().displayItem());
        inventory.setItem(31, BACK_ITEM);
        setFillerGlass(false);
    }
}
