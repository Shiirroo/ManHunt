package de.shiirroo.manhunt.event.menu.menus.setting.gamesave;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.PlayerMenuUtility;
import de.shiirroo.manhunt.event.menu.menus.setting.WorldMenu;
import de.shiirroo.manhunt.gamedata.GameData;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.repeatingtask.GameTimes;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LoadSaveMenu extends Menu {


    public LoadSaveMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return name;
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
        if (e.getWhoClicked().isOp()) {
            if (name.split(" ")[0].equalsIgnoreCase("Load")) {
                if (Objects.equals(e.getCurrentItem(), getLoadWorldSaveItemStack())) {
                    loadWorld();
                } else if (Objects.equals(e.getCurrentItem(), getSaveWorldItemStack(true)) && ManHuntPlugin.getGameData().getGameStatus().isGameRunning()) {//&& StartGame.bossBarGameStart != null && StartGame.bossBarGameStart.isRunning()){
                    saveWorld(true);
                } else if (Objects.equals(e.getCurrentItem(), getDeleteWorldSaveItemStack())) {
                    deleteWorld();
                }
            } else if (name.split(" ")[0].equalsIgnoreCase("Save") && Objects.equals(e.getCurrentItem(), getSaveWorldItemStack(false)) && ManHuntPlugin.getGameData().getGameStatus().isGameRunning()) {//&& StartGame.bossBarGameStart != null && StartGame.bossBarGameStart.isRunning()){
                saveWorld(false);
            }
        }

        if (Objects.equals(e.getCurrentItem(), BACK_ITEM)) {
            back();
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
        if (name.split(" ")[0].equalsIgnoreCase("Load")) {
            inventory.setItem(10, getLoadWorldSaveItemStack());
            inventory.setItem(12, getSaveWorldItemStack(true));
            inventory.setItem(14, getSaveDataItemStack());
            inventory.setItem(16, getDeleteWorldSaveItemStack());
        } else if (name.split(" ")[0].equalsIgnoreCase("Save")) {
            inventory.setItem(13, getSaveWorldItemStack(false));
        }

        inventory.setItem(31, BACK_ITEM);
        setFillerGlass(false);
    }

    private ItemStack getSaveWorldItemStack(Boolean saveAgain) {
        ItemStack saveWorldItem = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta im = saveWorldItem.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + (saveAgain ? "§lSAVE AGAIN" : "§lSAVE"));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        saveWorldItem.setItemMeta(im);
        return saveWorldItem;
    }

    private ItemStack getDeleteWorldSaveItemStack() {
        ItemStack deleteWorldItem = new ItemStack(Material.TNT);
        ItemMeta im = deleteWorldItem.getItemMeta();
        im.setDisplayName(ChatColor.RED + "§lDELETE");
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        deleteWorldItem.setItemMeta(im);
        return deleteWorldItem;
    }

    private ItemStack getLoadWorldSaveItemStack() {
        ItemStack loadWorldItem = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta im = loadWorldItem.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "§lLOAD");
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        loadWorldItem.setItemMeta(im);
        return loadWorldItem;
    }

    private ItemStack getSaveDataItemStack() {
        ItemStack loadWorldItem = new ItemStack(Material.PAPER);
        ItemMeta im = loadWorldItem.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "§lGame-Data");
        List<String> listLore = new ArrayList<>(List.of(""));

        GameData gameData = findSaveGame().getGameSaveData();
        if (gameData.getGameStatus().isGameRunning()) {
            listLore.add(ChatColor.YELLOW + "➢ " + ChatColor.GOLD + "ID : " + ChatColor.GREEN + gameData.getId().getMostSignificantBits());
            listLore.add(ChatColor.YELLOW + "➢ " + ChatColor.GOLD + "Game-Time : " + Events.getTimeString(false, GameTimes.getStartTime(gameData.getGameStatus().getGameStartTime(), gameData.getGamePause().getPauseList(), gameData.getGamePause().getUnPauseList())));
            listLore.add(ChatColor.YELLOW + "➢ " + ChatColor.GOLD + "Used pause : " + ChatColor.GREEN + gameData.getGamePause().getUnPauseList().size());
            listLore.add(ChatColor.YELLOW + "➢ " + ChatColor.GOLD + "Living Players : " + ChatColor.GREEN + gameData.getGameStatus().getLivePlayerList().size() + ChatColor.GRAY + " | " + ChatColor.GREEN + gameData.getGameStatus().getStartPlayerList().size());
            listLore.add("");
            listLore.add(ChatColor.YELLOW + "➢ " + ManHuntRole.Speedrunner.getChatColor() + "Speedrunners : " + ChatColor.GREEN + gameData.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size());
            listLore.add(ChatColor.YELLOW + "➢ " + ManHuntRole.Hunter.getChatColor() + "Hunter : " + ChatColor.GREEN + gameData.getPlayerData().getPlayersByRole(ManHuntRole.Hunter).size());
            listLore.add(ChatColor.YELLOW + "➢ " + ManHuntRole.Assassin.getChatColor() + "Assassins : " + ChatColor.GREEN + gameData.getPlayerData().getPlayersByRole(ManHuntRole.Assassin).size());
            listLore.add("");
            listLore.add(ChatColor.YELLOW + "➢ " + ChatColor.GOLD + "Remaining time :" + ChatColor.GREEN + " " + Events.getTimeString(false, ((((Integer) gameData.getGameConfig().getConfigCreators("GameResetTime").getConfigSetting()) * 60 * 60) - gameData.getGameStatus().getGameElapsedTime()) * 1000));

        }
        listLore.addAll(Arrays.asList("", ChatColor.YELLOW + "● Created on: " + ChatColor.GREEN + findSaveGame().getDateString()));
        im.setLore(listLore);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        loadWorldItem.setItemMeta(im);
        return loadWorldItem;
    }


    private void saveWorld(boolean saveAgain) throws MenuManagerException, MenuManagerNotSetupException {
        long saveTime = findSaveGame().saveGame(true, ManHuntPlugin.getGameData());
        back();
        getPlayer().sendMessage(ManHuntPlugin.getprefix() + ChatColor.GOLD + ManHuntPlugin.getGameData().getGameStatus().getAutoSave().getSaveName()
                + ChatColor.GRAY + " was saved. ( " + ChatColor.GREEN + saveTime + " ms " + ChatColor.GRAY + ")");
    }

    private SaveGame findSaveGame() {
        String[] strings = name.split(" ");
        for (SaveGame saveGame : WorldMenu.gameList) {
            if (saveGame.getSaveName().equalsIgnoreCase(strings[1])) {
                return saveGame;
            }
        }
        return ManHuntPlugin.getGameData().getGameStatus().getAutoSave();
    }

    private void loadWorld() {
        ManHuntPlugin.getPlugin().getConfig().set("LoadSaveGame", findSaveGame().getSaveSlot());
        ManHuntPlugin.getPlugin().saveConfig();
        Bukkit.getServer().getOnlinePlayers().forEach(player -> player.kickPlayer(("SaveGame-" + findSaveGame().getSaveName() + " is loading ...")));
        Bukkit.spigot().restart();
    }


    private void deleteWorld() throws MenuManagerException, MenuManagerNotSetupException {
        long deleteTime = findSaveGame().deleteSave();
        back();
        Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(ManHuntPlugin.getprefix() + ChatColor.GOLD + ManHuntPlugin.getGameData().getGameStatus().getAutoSave().getSaveName()
                + ChatColor.GRAY + " was deleted. ( " + ChatColor.GREEN + deleteTime + " ms " + ChatColor.GRAY + ")");
    }
}