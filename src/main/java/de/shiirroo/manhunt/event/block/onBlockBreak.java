package de.shiirroo.manhunt.event.block;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class onBlockBreak implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private void onBlockBreak(BlockBreakEvent event) {
        if(VoteCommand.pause) event.setCancelled(true);
        if(StartGame.gameRunning != null && StartGame.gameRunning.isRunning()) {
            if (ManHuntPlugin.getPlayerData().getPlayerRole(event.getPlayer()) != ManHuntRole.Speedrunner) {
                event.setCancelled(true);
            }
        } else if(StartGame.gameRunning == null && event.getPlayer().getGameMode() == GameMode.SURVIVAL){
            event.setCancelled(true);

        }
        if (ManHuntPlugin.getPlayerData().isFrozen(event.getPlayer()))
            event.setCancelled(true);
    }
}
