package de.shiirroo.manhunt.event.menu.menus;

import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.menu.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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

    public PlayerMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return playerMenuUtility.getOwner().getName();
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
        if(StartGame.gameRunning == null && !(p.isOp() && p.getGameMode().equals(GameMode.CREATIVE))){
            e.getAction();
            Command(e.getCurrentItem(), (Player) e.getWhoClicked());
            e.setCancelled(true);
        }
    }

    @Override
    public void handlePlayerDropItemEvent(PlayerDropItemEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if(StartGame.gameRunning == null && !(p.isOp() && p.getGameMode().equals(GameMode.CREATIVE))){
            e.setCancelled(true);
        }
    }

    @Override
    public void handlePlayerInteractEvent(PlayerInteractEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if(StartGame.gameRunning == null || !(p.isOp() && p.getGameMode().equals(GameMode.CREATIVE))){
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
            if(StartGame.gameRunning == null && Ready.ready != null && !Ready.ready.getbossBarCreator().isRunning() && !Ready.ready.hasPlayerVote(player))
                MenuManager.openMenu(SelectGroupMenu.class, player, null);}
        else if(checkSelectGroup(itemStack, StartGame())){ if(player.isOp()) MenuManager.openMenu(ConfirmationMenu.class, player, "Start Game?" );}
        else if(checkSelectGroup(itemStack, ResetWorld())){ if(player.isOp() && StartGame.gameRunning == null && Ready.ready != null)  MenuManager.openMenu(ConfirmationMenu.class, player, "World Reset?" );}
        else if(checkSelectGroup(itemStack, SettingGame())){ if(StartGame.gameRunning == null && Ready.ready != null)  SettingMenu.put(p.getUniqueId(), MenuManager.openMenu(SettingsMenu.class, player, null ));}


    }




    @Override
    public void setMenuItems() {
        setItems(0,SelectGroup());
        boolean isReady = Ready.ready.hasPlayerVote(p);
        if(!isReady) {
            setItems(4, VoteStarting());
        }
         else {
            setItems(4, CancelVoteStarting());
        }
        setItems(2,  SettingGame());

        if(p.isOp()){
            setItems(6, ResetWorld());
            setItems(8, StartGame());

        } else {
            inventory.removeItem(StartGame());
            inventory.removeItem(ResetWorld());
        }
    }

    private ItemStack SelectGroup(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.BOOK);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("Select Group").color(TextColor.fromHexString("#FFAA00")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }


    private ItemStack StartGame(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.NETHER_STAR);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("Start Game").color(TextColor.fromHexString("#FFAA00")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack SettingGame(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("Settings").color(TextColor.fromHexString("#DDD605")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }


    private ItemStack VoteStarting(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.GREEN_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("Game Ready").color(TextColor.fromHexString("#55FF55")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack CancelVoteStarting(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("Cancel").color(TextColor.fromHexString("#FF5555")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack ResetWorld(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.GRASS_BLOCK);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("Reset World").color(TextColor.fromHexString("#AA0000")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }


    private void setItems(Integer integer, ItemStack itemStack){
        inventory.setItem(integer,itemStack);

    }
}
