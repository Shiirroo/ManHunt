package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.command.subcommands.StartGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class onPlayerSwapHandItemsEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        if(StartGame.gameRunning == null){
            event.setCancelled(true);
        }
    }

}
