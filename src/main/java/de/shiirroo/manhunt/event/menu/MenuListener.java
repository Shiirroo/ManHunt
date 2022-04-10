package de.shiirroo.manhunt.event.menu;

import de.shiirroo.manhunt.ManHuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;


public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        InventoryHolder inventoryHolder;
        try {
            inventoryHolder = (InventoryHolder) MenuManager.getPlayerMenuUtility(e.getWhoClicked().getUniqueId()).getData(e.getWhoClicked().getUniqueId().toString());
            InventoryHolder holder = e.getInventory().getHolder();
            if (holder instanceof Menu)
                MenuListeners(holder, e);
            else if (inventoryHolder instanceof Menu)
                MenuListeners(inventoryHolder, e);
        } catch (MenuManagerNotSetupException | MenuManagerException ex) {
            Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.RED + "Something went wrong while menu click");
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) throws MenuManagerNotSetupException {
        InventoryHolder inventoryHolder = (InventoryHolder) MenuManager.getPlayerMenuUtility(e.getPlayer().getUniqueId()).getData(e.getPlayer().getUniqueId().toString());
        if (inventoryHolder instanceof Menu menu) {
            e.getItemDrop();
            menu.handlePlayerDropItemEvent(e);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        InventoryHolder inventoryHolder = (InventoryHolder) MenuManager.getPlayerMenuUtility(e.getPlayer().getUniqueId()).getData(e.getPlayer().getUniqueId().toString());
        if (inventoryHolder instanceof Menu menu) {
            if (e.getItem() == null) return;
            menu.handlePlayerInteractEvent(e);
        }
    }

    private void MenuListeners(InventoryHolder iv, InventoryClickEvent e) throws MenuManagerException, MenuManagerNotSetupException {
        if (e.getCurrentItem() == null) return;
        Menu menu = (Menu) iv;
        if (menu.cancelAllClicks()) e.setCancelled(true);
        menu.handleMenuClickEvent(e);

    }
}