package de.shiirroo.manhunt.event.entity;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.utilis.repeatingtask.CompassTracker;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;

public class onEntityMountEvent implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void EntityMountEvent(EntityMountEvent event) {
        if (!event.getEntity().getType().equals(EntityType.PLAYER)) return;

        Player p = (Player) event.getEntity();

        if (Events.cancelEvent(p))
            event.setCancelled(true);

        if (Config.getBossbarCompass() && !BossBarCoordinates.hasCoordinatesBossbar(p) && ManHuntPlugin.debug) {
            BossBarCoordinates.addPlayerCoordinatesBossbar(p);
        }

        if (ManHuntPlugin.getGameData().getGameStatus().isGame()) {
            CompassTracker.setPlayerlast(p);
        }
    }
}
