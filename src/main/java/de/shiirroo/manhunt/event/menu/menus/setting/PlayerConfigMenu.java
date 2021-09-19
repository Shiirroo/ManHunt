package de.shiirroo.manhunt.event.menu.menus.setting;

import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.TeamChat;
import de.shiirroo.manhunt.command.subcommands.TimerCommand;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.PlayerMenuUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class PlayerConfigMenu extends Menu {

    public PlayerConfigMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ChatColor.GREEN +""+ ChatColor.BOLD + "User Config: " + ChatColor.GOLD + p.getName();
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
      } else if (Objects.equals(e.getCurrentItem(), Yes("Show GameTimer"))) {
          TimerCommand.playerShowTimers.add(p.getUniqueId());
      } else if (Objects.equals(e.getCurrentItem(), NO("Hide GameTimer"))) {
          TimerCommand.playerShowTimers.remove(p.getUniqueId());
      } else if (Objects.equals(e.getCurrentItem(), Yes("Join TeamChat")) && !p.getGameMode().equals(GameMode.SPECTATOR)) {
          TeamChat.teamchat.add(p.getUniqueId());
      } else if (Objects.equals(e.getCurrentItem(), NO("Leave TeamChat")) && !p.getGameMode().equals(GameMode.SPECTATOR)) {
          TeamChat.leaveChat(p);
      } else if (Objects.equals(e.getCurrentItem(), Yes("Join Spectator"))) {
          if(StartGame.gameRunning == null && Ready.ready != null && Ready.ready.getbossBarCreator().getTimer() >= 3){
              p.setGameMode(GameMode.SPECTATOR);
              TeamChat.leaveChat(p);
          }
      } else if (Objects.equals(e.getCurrentItem(), NO("Leave Spectator"))) {
          if(StartGame.gameRunning == null && Ready.ready != null && Ready.ready.getbossBarCreator().getTimer() >= 3){
              p.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
              p.setGameMode(GameMode.ADVENTURE);
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
        if(TimerCommand.playerShowTimers.contains(p.getUniqueId())){
            inventory.setItem(11, NO("Hide GameTimer"));
        } else {
            inventory.setItem(11, Yes("Show GameTimer"));
        }

        if(TeamChat.teamchat.contains(p.getUniqueId())){
            inventory.setItem(13, NO("Leave TeamChat"));
        } else {
            inventory.setItem(13, Yes("Join TeamChat"));
        }

        if(p.getGameMode().equals(GameMode.SPECTATOR)){
            inventory.setItem(15, NO("Leave Spectator"));
        } else {
            inventory.setItem(15, Yes("Join Spectator"));
        }


        inventory.setItem(31, BACK_ITEM);
        setFillerGlass(false);
    }


    private ItemStack Yes(String configname) {
        ItemStack GroupMenuGUI = new ItemStack(Material.GREEN_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("§l" + configname).color(TextColor.fromHexString("#55FF55")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack NO(String configname) {
        ItemStack GroupMenuGUI = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("§l" + configname).color(TextColor.fromHexString("#FF5555")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }
}
