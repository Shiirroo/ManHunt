package de.shiirroo.manhunt.event.block;

import de.shiirroo.manhunt.event.Events;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class onBlockPlace implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    private void BlockPlace(BlockPlaceEvent event) {
        if ((Events.cancelEvent(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }
}
