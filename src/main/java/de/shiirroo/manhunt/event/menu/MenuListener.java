package de.shiirroo.manhunt.event.menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;


public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) throws MenuManagerException, MenuManagerNotSetupException {
        InventoryHolder inventoryHolder = (InventoryHolder) MenuManager.getPlayerMenuUtility((Player) e.getWhoClicked()).getData(e.getWhoClicked().getUniqueId().toString());
        InventoryHolder holder = e.getInventory().getHolder();
        if(holder instanceof Menu)
            MenuListeners(holder, e);
        else if(inventoryHolder instanceof Menu)
            MenuListeners(inventoryHolder, e);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) throws MenuManagerException, MenuManagerNotSetupException {
        InventoryHolder inventoryHolder = (InventoryHolder) MenuManager.getPlayerMenuUtility(e.getPlayer()).getData(e.getPlayer().getUniqueId().toString());
        if(inventoryHolder instanceof Menu menu) {
            e.getItemDrop();
            try {
                menu.handlePlayerDropItemEvent(e);
            } catch (MenuManagerNotSetupException ignored) {
            } catch (MenuManagerException menuManagerException) {
                menuManagerException.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) throws MenuManagerException, MenuManagerNotSetupException {
        InventoryHolder inventoryHolder = (InventoryHolder) MenuManager.getPlayerMenuUtility(e.getPlayer()).getData(e.getPlayer().getUniqueId().toString());
        if(inventoryHolder instanceof Menu menu) {
            if (e.getItem() == null) {
                return;
            }
            try {
                menu.handlePlayerInteractEvent(e);
            } catch (MenuManagerNotSetupException ignored) {
            } catch (MenuManagerException menuManagerException) {
                menuManagerException.printStackTrace();
            }
        }

    }

    private void MenuListeners(InventoryHolder iv, InventoryClickEvent e){
            if (e.getCurrentItem() == null) {
                return;
            }
            Menu menu = (Menu) iv;

            if (menu.cancelAllClicks()){
                e.setCancelled(true);
            }

            try{
                menu.handleMenuClickEvent(e);
            } catch (MenuManagerNotSetupException ignored) {
            } catch (MenuManagerException menuManagerException) {
                menuManagerException.printStackTrace();
        }
    }


}