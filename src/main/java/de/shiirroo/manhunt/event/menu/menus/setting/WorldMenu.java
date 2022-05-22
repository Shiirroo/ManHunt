package de.shiirroo.manhunt.event.menu.menus.setting;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.event.menu.*;
import de.shiirroo.manhunt.event.menu.menus.ConfirmationMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.gamesave.GameSaveMenu;
import de.shiirroo.manhunt.world.save.SaveGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class WorldMenu extends Menu {
    public static final List<SaveGame> gameList = getGameSave();
    public static final HashMap<UUID, Menu> gameSaveMenuHashMap = new HashMap<>();

    public WorldMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public static List<SaveGame> getGameSave() {
        List<SaveGame> saveGameList = new ArrayList<>();
        for (int i = 1; i != 6; i++) {
            try {
                saveGameList.add(SaveGame.class.getDeclaredConstructor().newInstance().setSlot(i).setSaveName("Save-" + i));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.RED + "Something went wrong while open create save games");
            }
        }

        return saveGameList;
    }

    @Override
    public String getMenuName() {
        return "World";
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
        if (getPlayer().isOp() & !ManHuntPlugin.getGameData().getGameStatus().isGame() && Ready.ready != null) {
            if (Objects.equals(e.getCurrentItem(), ResetWorld())) {
                MenuManager.getMenu(ConfirmationMenu.class, uuid).setName("World Reset?").setBack(true).open();
            } else if (Objects.equals(e.getCurrentItem(), SaveGameItem())) {
                gameSaveMenuHashMap.put(uuid, MenuManager.getMenu(GameSaveMenu.class, uuid).setBack(true).open());
            }
        }
        if (Objects.equals(e.getCurrentItem(), CLOSE_ITEM)) {
            e.getWhoClicked().closeInventory();
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
        inventory.setItem(12, ResetWorld());
        inventory.setItem(14, SaveGameItem());
        inventory.setItem(31, CLOSE_ITEM);
        setFillerGlass(false);
    }

    private ItemStack ResetWorld() {
        ItemStack GroupMenuGUI = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.setDisplayName(ChatColor.DARK_RED + "Reset World");
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack SaveGameItem() {
        ItemStack GroupMenuGUI = new ItemStack(Material.BOOKSHELF);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.setDisplayName(ChatColor.DARK_GREEN + "Game Saves");
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }
}
