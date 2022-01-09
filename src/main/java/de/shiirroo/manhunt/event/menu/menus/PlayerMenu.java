package de.shiirroo.manhunt.event.menu.menus;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.event.menu.*;
import de.shiirroo.manhunt.event.menu.menus.setting.SettingsMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.WorldMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class PlayerMenu extends Menu {
    public static HashMap<UUID, Menu> SettingMenu = new HashMap<>();
    public static HashMap<UUID, Menu> SelectGroupMenu = new HashMap<>();

    public PlayerMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return Bukkit.getPlayer(playerMenuUtility.getUuid()).getName();
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.PLAYER;
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public boolean cancelAllClicks() {
        return false;
    }

    @Override
    public void handleMenuClickEvent(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if(!ManHuntPlugin.getGameData().getGameStatus().isGame() && !(e.getWhoClicked().isOp() && e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE))){
            Command(e.getCurrentItem(), (Player) e.getWhoClicked());
            e.setCancelled(true);
        }
    }

    @Override
    public void handlePlayerDropItemEvent(PlayerDropItemEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if(!ManHuntPlugin.getGameData().getGameStatus().isGame() && !(e.getPlayer().isOp() && e.getPlayer().getGameMode().equals(GameMode.CREATIVE))){
            e.setCancelled(true);
        }
    }

    @Override
    public void handlePlayerInteractEvent(PlayerInteractEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if(!ManHuntPlugin.getGameData().getGameStatus().isGame()){
            if(!e.getAction().equals(Action.LEFT_CLICK_AIR) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Command(e.getItem(), e.getPlayer());
                e.setCancelled(true);
            }
        }
    }


    public boolean checkSelectGroup(ItemStack itemStack, ItemStack otherItemStack){
        return itemStack.equals(otherItemStack);
    }

    private void Command(ItemStack itemStack, Player player) throws MenuManagerException, MenuManagerNotSetupException {
        if(checkSelectGroup(itemStack, VoteStarting())){
            Ready.setReady(player);
        }
        else if(checkSelectGroup(itemStack, CancelVoteStarting())){
            Ready.setReady(player);}
        else if(checkSelectGroup(itemStack, SelectGroup())){
            if(!ManHuntPlugin.getGameData().getGameStatus().isGame() && Ready.ready != null && !Ready.ready.getbossBarCreator().isRunning() && !Ready.ready.hasPlayerVote(player))
                SelectGroupMenu.put(uuid, MenuManager.getMenu(SelectGroupMenu.class, player.getUniqueId()).open());
        }
        else if(checkSelectGroup(itemStack, StartGame()) && player.isOp()){
                MenuManager.getMenu(ConfirmationMenu.class, player.getUniqueId()).setName("Start Game?").open();
        }
        else if(checkSelectGroup(itemStack, World()) && player.isOp() && !ManHuntPlugin.getGameData().getGameStatus().isGame() && Ready.ready != null){
            MenuManager.getMenu(WorldMenu.class, player.getUniqueId()).open();
        }
        else if(checkSelectGroup(itemStack, SettingGame())){ if(!ManHuntPlugin.getGameData().getGameStatus().isGame() && Ready.ready != null) {
            SettingMenu.put(player.getUniqueId(), MenuManager.getMenu(SettingsMenu.class, player.getUniqueId()).open());
        }
        }
    }




    @Override
    public void setMenuItems() {
        inventory.clear();
        Player p = Bukkit.getPlayer(uuid);
        if(p != null) {
            if (ManHuntPlugin.getGameData().getGameStatus().isGame())
                return;
            boolean isReady = Ready.ready.hasPlayerVote(p);
            if (getGameMode() != GameMode.SPECTATOR) {
                setItems(0, SelectGroup());
                if (!isReady) {
                    setItems(4, VoteStarting());
                } else {
                    setItems(4, CancelVoteStarting());
                }
            } else {
                inventory.removeItem(SelectGroup());
                if (!isReady) {
                    inventory.removeItem(VoteStarting());
                } else {
                    inventory.removeItem(CancelVoteStarting());
                }
            }

            setItems(2, SettingGame());

            if (p.isOp() && getGameMode() != GameMode.SPECTATOR) {
                setItems(6, World());
                setItems(8, StartGame());

            } else {
                inventory.removeItem(StartGame());
                inventory.removeItem(World());
            }
        }
    }

    private ItemStack SelectGroup(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.BOOK);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "Select Group");
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }


    private ItemStack StartGame(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.NETHER_STAR);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "Start Game");
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack SettingGame(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "Settings");
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }


    private ItemStack VoteStarting(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.GREEN_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Game Ready");
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack CancelVoteStarting(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.setDisplayName(ChatColor.RED + ("Cancel"));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }


    private ItemStack World(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.GRASS_BLOCK);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.setDisplayName(ChatColor.AQUA + "World");
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }


    private void setItems(Integer integer, ItemStack itemStack){
        inventory.setItem(integer,itemStack);

    }

    public static GameMode gameMode;
}
