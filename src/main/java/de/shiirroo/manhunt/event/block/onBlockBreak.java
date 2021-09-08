package de.shiirroo.manhunt.event.block;

import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class onBlockBreak implements Listener {

    private static PlayerData playerData;

    public onBlockBreak(PlayerData playerData) {
        this.playerData = playerData;
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onBlockBreak(BlockBreakEvent event) {
        if(StartGame.gameRunning != null && StartGame.gameRunning.isRunning()) {
            if (playerData.getRole(event.getPlayer()) != ManHuntRole.Speedrunner) {
                event.setCancelled(true);
            }
        } else if(StartGame.gameRunning == null && event.getPlayer().getGameMode() == GameMode.SURVIVAL){
            event.setCancelled(true);

        }
        if (playerData.isFrozen(event.getPlayer()))
            event.setCancelled(true);
    }
}
