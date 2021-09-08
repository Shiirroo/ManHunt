package de.shiirroo.manhunt.event.entity;

import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.utilis.Worker;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;

public class onEntityMountEvent implements Listener {

    private final Config config;


    public onEntityMountEvent(Config config){
        this.config = config;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityMountEvent(EntityMountEvent event) {
        if (!event.getEntity().getType().equals(EntityType.PLAYER)) return;
        Player p = (Player) event.getEntity();

        if (config.isBossbarCompass() && !BossBarCoordinates.hasCoordinatesBossbar(p)) {
            BossBarCoordinates.addPlayerCoordinatesBossbar(p);
        }

        if(StartGame.gameRunning != null) {
            Worker.setPlayerlast(p);
        }
    }
}
