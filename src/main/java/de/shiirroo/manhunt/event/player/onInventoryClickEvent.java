package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class onInventoryClickEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void InventoryClickEvent(InventoryClickEvent event) {
        if (ManHuntPlugin.getGameData().getGamePause().isPause() && event.getInventory().equals(event.getWhoClicked().getInventory())) {
            event.setCancelled(true);
        }
    }
}
