package de.shiirroo.manhunt.event.menu.menus.setting;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.TeamChat;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.PlayerMenuUtility;
import de.shiirroo.manhunt.utilis.Utilis;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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

    public PlayerConfigMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    List<String> gameTimer = new ArrayList<>(Arrays.asList("", ChatColor.GRAY + "Show | Hide Game Timer"));
    List<String> teamChat = new ArrayList<>(Arrays.asList("", ChatColor.GRAY + "Join | Leave TeamChat"));
    List<String> spectator = new ArrayList<>(Arrays.asList("", ChatColor.GRAY + "Join | Leave Spectator Mode "));

    @Override
    public String getMenuName() {
        return ChatColor.GREEN +""+ ChatColor.BOLD + "User Config: " + ChatColor.GOLD + Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName();
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
      if(Objects.equals(e.getCurrentItem(), BACK_ITEM)) {
          back();
      } else if (Objects.equals(e.getCurrentItem(), NO("GameTimer",gameTimer))) {
          ManHuntPlugin.getGameData().getGamePlayer().getPlayerShowGameTimer().add(uuid);
      } else if (Objects.equals(e.getCurrentItem(), Yes("GameTimer",gameTimer))) {
          ManHuntPlugin.getGameData().getGamePlayer().getPlayerShowGameTimer().remove(uuid);
      } else if (Objects.equals(e.getCurrentItem(), NO("TeamChat",teamChat)) && !e.getWhoClicked().getGameMode().equals(GameMode.SPECTATOR)) {
          ManHuntPlugin.getGameData().getGamePlayer().getTeamchat().add(uuid);
      } else if (Objects.equals(e.getCurrentItem(), Yes("TeamChat",teamChat)) && !e.getWhoClicked().getGameMode().equals(GameMode.SPECTATOR)) {
          TeamChat.leaveChat((Player) e.getWhoClicked());
      } else if (Objects.equals(e.getCurrentItem(), NO("Spectator",spectator))) {
          if(!ManHuntPlugin.getGameData().getGameStatus().isGame() && Ready.ready != null && Ready.ready.getbossBarCreator().getTimer() >= 3){
              e.getWhoClicked().setGameMode(GameMode.SPECTATOR);
              TeamChat.leaveChat((Player) e.getWhoClicked());
          }
      } else if (Objects.equals(e.getCurrentItem(), Yes("Spectator",spectator))) {
          if(!ManHuntPlugin.getGameData().getGameStatus().isGame() && Ready.ready != null && Ready.ready.getbossBarCreator().getTimer() >= 3){
              e.getWhoClicked().teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
              e.getWhoClicked().setGameMode(GameMode.ADVENTURE);
          }
      }

      setMenuItems();

    }

    @Override
    public void handlePlayerDropItemEvent(PlayerDropItemEvent e) throws MenuManagerNotSetupException, MenuManagerException {

    }

    @Override
    public void handlePlayerInteractEvent(PlayerInteractEvent e) throws MenuManagerNotSetupException, MenuManagerException {

    }

    @Override
    public void setMenuItems() {
        if(ManHuntPlugin.getGameData().getGamePlayer().getPlayerShowGameTimer().contains(uuid)){
            inventory.setItem(11, Yes("GameTimer",gameTimer));
        } else {
            inventory.setItem(11, NO("GameTimer",gameTimer));
        }

        if(ManHuntPlugin.getGameData().getGamePlayer().getTeamchat().contains(uuid)){
            inventory.setItem(13, Yes("TeamChat",teamChat));
        } else {
            inventory.setItem(13, NO("TeamChat",teamChat));
        }

        if(Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)){
            inventory.setItem(15, Yes("Spectator",spectator));
        } else {
            inventory.setItem(15, NO("Spectator",spectator));
        }


        inventory.setItem(31, BACK_ITEM);
        setFillerGlass(false);
    }


    private ItemStack Yes(String configname, List<String> lore) {
        ItemStack GroupMenuGUI = new ItemStack(Material.GREEN_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("§l" + configname).color(TextColor.fromHexString("#55FF55")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        im.lore(Utilis.lore(lore));
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack NO(String configname, List<String> lore){
        ItemStack GroupMenuGUI = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("§l" + configname).color(TextColor.fromHexString("#FF5555")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        im.lore(Utilis.lore(lore));
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }
}
