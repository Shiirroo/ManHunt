package de.shiirroo.manhunt.event.entity;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class onEntityDamageEvent implements Listener{

    @EventHandler(priority = EventPriority.HIGH)
    private void EntityDamageEvent(EntityDamageEvent event) {
        if(event.getEntity().getType().equals(EntityType.PLAYER)){
            Player player = (Player) event.getEntity();
            if (!player.getGameMode().equals(GameMode.CREATIVE) && ((ManHuntPlugin.getGameData().getGameStatus().isGame() && ManHuntPlugin.getGameData().getGamePause().isPause()) ||
                    (!ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(player.getUniqueId()).equals(ManHuntRole.Speedrunner)
                            && ManHuntPlugin.getGameData().getGameStatus().isStarting()) || (ManHuntPlugin.getGameData().getGameStatus().isReadyForVote() && !ManHuntPlugin.getGameData().getGameStatus().isGame()))) {
                event.setCancelled(true);
            }
        }
    }
}
