package de.shiirroo.manhunt.event.menu.menus;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.PlayerMenuUtility;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePresetMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class ConfirmationMenu extends Menu {
    public ConfirmationMenu(PlayerMenuUtility playerMenuUtility) {
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

    public boolean checkSelectGroup(ItemStack itemStack, ItemStack otherItemStack) {
        return itemStack.equals(otherItemStack);
    }

    @Override
    public void handleMenuClickEvent(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if(!ManHuntPlugin.getGameData().getGameStatus().isGame()){
            if (e.getWhoClicked().isOp()) {
                if (checkSelectGroup(Objects.requireNonNull(e.getCurrentItem()), Yes())) {
                    ifYes();
                } else if (checkSelectGroup(e.getCurrentItem(), NO())) {
                    ifNo();
                }
            }
        }
        if(Objects.equals(e.getCurrentItem(), BACK_ITEM)){
            back();
        } else if(Objects.equals(e.getCurrentItem(), CLOSE_ITEM)){
            e.getWhoClicked().closeInventory();
        }
    }

    @Override
    public void handlePlayerDropItemEvent(PlayerDropItemEvent e) throws MenuManagerNotSetupException, MenuManagerException {

    }

    @Override
    public void handlePlayerInteractEvent(PlayerInteractEvent e) throws MenuManagerNotSetupException, MenuManagerException {

    }

    @Override
    public void setMenuItems() {
        inventory.setItem(12, NO());
        inventory.setItem(14, Yes());

        if(hasBack)
            inventory.setItem(31, BACK_ITEM);
        else
            inventory.setItem(31, CLOSE_ITEM);
        setFillerGlass(false);
    }

    private ItemStack Yes() {
        ItemStack GroupMenuGUI = new ItemStack(Material.GREEN_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("§lYES").color(TextColor.fromHexString("#55FF55")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack NO() {
        ItemStack GroupMenuGUI = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("§lNO").color(TextColor.fromHexString("#FF5555")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private void ifYes() {
        if(name.equalsIgnoreCase("Start Game?") && GamePresetMenu.preset.setPlayersGroup()){
            StartGame.Start();
            inventory.close();
        } else if(name.equalsIgnoreCase("World Reset?")){
            WorldReset();
        } else {
            inventory.close();
        }

    }
    private void ifNo(){
        inventory.close();
    }



    private void WorldReset() {
        Bukkit.setWhitelist(true);
        for (Player p : Bukkit.getOnlinePlayers())
            p.kick(Component.text(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Man" + ChatColor.RED + "Hunt" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "World is Resetting.."));
        ManHuntPlugin.getPlugin().getConfig().set("isReset", true);
        ManHuntPlugin.getPlugin().saveConfig();
        Bukkit.spigot().restart();
    }

}
