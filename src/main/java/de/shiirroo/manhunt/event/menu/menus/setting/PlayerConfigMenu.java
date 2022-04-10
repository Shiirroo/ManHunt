package de.shiirroo.manhunt.event.menu.menus.setting;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.TeamChat;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.PlayerMenuUtility;
import de.shiirroo.manhunt.event.menu.menus.PlayerMenu;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

public class PlayerConfigMenu extends Menu {

    final List<String> gameTimer = new ArrayList<>(Arrays.asList("", ChatColor.GRAY + "Show | Hide Game Timer"));
    final List<String> teamChat = new ArrayList<>(Arrays.asList("", ChatColor.GRAY + "Join | Leave TeamChat"));
    final List<String> spectator = new ArrayList<>(Arrays.asList("", ChatColor.GRAY + "Join | Leave Spectator Mode "));

    public PlayerConfigMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ChatColor.GREEN + "" + ChatColor.BOLD + "User Config: " + ChatColor.GOLD + Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName();
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
        if (Objects.equals(e.getCurrentItem(), BACK_ITEM)) {
            back();
        } else if (Objects.equals(e.getCurrentItem(), NO("GameTimer", gameTimer))) {
            ManHuntPlugin.getGameData().getGamePlayer().getPlayerShowGameTimer().add(uuid);
        } else if (Objects.equals(e.getCurrentItem(), Yes("GameTimer", gameTimer))) {
            ManHuntPlugin.getGameData().getGamePlayer().getPlayerShowGameTimer().remove(uuid);
        } else if (Objects.equals(e.getCurrentItem(), NO("TeamChat", teamChat)) && !e.getWhoClicked().getGameMode().equals(GameMode.SPECTATOR)) {
            ManHuntPlugin.getGameData().getGamePlayer().getTeamchat().add(uuid);
        } else if (Objects.equals(e.getCurrentItem(), Yes("TeamChat", teamChat)) && !e.getWhoClicked().getGameMode().equals(GameMode.SPECTATOR)) {
            TeamChat.leaveChat((Player) e.getWhoClicked());
        } else if (Objects.equals(e.getCurrentItem(), NO("Spectator", spectator))) {
            if (!ManHuntPlugin.getGameData().getGameStatus().isGame() && Ready.ready != null && Ready.ready.getbossBarCreator().getTimer() >= 3) {
                ManHuntPlugin.getGameData().getGamePlayer().removePlayer(uuid);
                e.getWhoClicked().setGameMode(GameMode.SPECTATOR);
                TeamChat.leaveChat((Player) e.getWhoClicked());
                ManHuntPlugin.getGameData().getPlayerData().setRole(getPlayer(), ManHuntRole.Unassigned, ManHuntPlugin.getTeamManager());
                PlayerMenu.SelectGroupMenu.values().forEach(Menu::setMenuItems);
            }
        } else if (Objects.equals(e.getCurrentItem(), Yes("Spectator", spectator))) {
            if (!ManHuntPlugin.getGameData().getGameStatus().isGame() && Ready.ready != null && Ready.ready.getbossBarCreator().getTimer() >= 3 && ManHuntPlugin.getGameData().getGamePlayer().getPlayers().size() + 1 <= Config.getMaxPlayerSize()) {
                e.getWhoClicked().teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
                ManHuntPlugin.getGameData().getGamePlayer().addPlayer(uuid);
                e.getWhoClicked().setGameMode(GameMode.ADVENTURE);
            }
        }

        setMenuItems();

    }

    @Override
    public void handlePlayerDropItemEvent(PlayerDropItemEvent e) {

    }

    @Override
    public void handlePlayerInteractEvent(PlayerInteractEvent e) {

    }

    @Override
    public void setMenuItems() {
        if (ManHuntPlugin.getGameData().getGamePlayer().getPlayerShowGameTimer().contains(uuid)) {
            inventory.setItem(11, Yes("GameTimer", gameTimer));
        } else {
            inventory.setItem(11, NO("GameTimer", gameTimer));
        }

        if (ManHuntPlugin.getGameData().getGamePlayer().getTeamchat().contains(uuid)) {
            inventory.setItem(13, Yes("TeamChat", teamChat));
        } else {
            inventory.setItem(13, NO("TeamChat", teamChat));
        }

        if (Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)) {
            inventory.setItem(15, Yes("Spectator", spectator));
        } else {
            inventory.setItem(15, NO("Spectator", spectator));
        }


        inventory.setItem(31, BACK_ITEM);
        setFillerGlass(false);
    }


    private ItemStack Yes(String configname, List<String> lore) {
        ItemStack GroupMenuGUI = new ItemStack(Material.GREEN_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "§l" + configname);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        im.setLore(lore);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack NO(String configname, List<String> lore) {
        ItemStack GroupMenuGUI = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.setDisplayName(ChatColor.RED + "§l" + configname);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        im.setLore(lore);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }
}
