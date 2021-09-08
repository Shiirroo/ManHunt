package de.shiirroo.manhunt.event.entity;

import de.shiirroo.manhunt.command.subcommands.StartGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class onEntityDamageEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private void onEntityDamageEvent(EntityDamageEvent event) {
            if (StartGame.gameRunning == null) {
                event.setCancelled(true);
        }
    }
}