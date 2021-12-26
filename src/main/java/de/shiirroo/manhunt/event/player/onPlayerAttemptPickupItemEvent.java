package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.event.Events;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

public class onPlayerAttemptPickupItemEvent implements Listener{

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerAttemptPickupItemEvent(PlayerAttemptPickupItemEvent event) {
        if (Events.cancelEvent(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
