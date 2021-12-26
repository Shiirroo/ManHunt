package de.shiirroo.manhunt.event.entity;

import de.shiirroo.manhunt.ManHuntPlugin;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class onEntityMoveEvent implements Listener{


    @EventHandler(priority = EventPriority.HIGH)
    public void EntityMoveEvent(EntityMoveEvent event) {
        if (ManHuntPlugin.getGameData().getGamePause().isPause()) event.setCancelled(true);
    }
}