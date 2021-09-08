package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.command.subcommands.StartGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

public class onPlayerAttemptPickupItemEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerAttemptPickupItemEvent(PlayerAttemptPickupItemEvent event) {
        if (StartGame.gameRunning == null) {
            event.setCancelled(true);
        }
    }
}
