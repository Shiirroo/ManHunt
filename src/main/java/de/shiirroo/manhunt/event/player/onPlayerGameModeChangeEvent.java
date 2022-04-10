package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.vote.VoteCommand;
import de.shiirroo.manhunt.utilis.repeatingtask.CompassTracker;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class onPlayerGameModeChangeEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
        if (ManHuntPlugin.getGameData().getGamePause().isPause() && !event.getPlayer().isOp()) {
            event.setCancelled(true);
        }

        if (VoteCommand.getVote() != null && event.getNewGameMode().equals(GameMode.SPECTATOR)) {
            VoteCommand.getVote().getVoteCreator().removeVote(event.getPlayer());
        }

        if (Ready.ready != null && event.getNewGameMode().equals(GameMode.SPECTATOR)) {
            Ready.readyRemove(event.getPlayer(), true);
        }

        if (ManHuntPlugin.getGameData() != null) {
            ManHuntPlugin.getGameData().getPlayerData().switchGameMode(event.getPlayer(), event.getNewGameMode(), ManHuntPlugin.getTeamManager());
            if (ManHuntPlugin.getGameData().getGamePlayer().getPlayerWorldMap().get(event.getPlayer().getUniqueId()) != null) {
                CompassTracker.setPlayerlast(event.getPlayer());
            }
        }

        if (ManHuntPlugin.playerMenu.get(event.getPlayer().getUniqueId()) != null && ManHuntPlugin.playerMenu.get(event.getPlayer().getUniqueId()).getInventory().equals(event.getPlayer().getInventory())) {
            ManHuntPlugin.playerMenu.get(event.getPlayer().getUniqueId()).setGameMode(event.getNewGameMode());
            ManHuntPlugin.playerMenu.get(event.getPlayer().getUniqueId()).setMenuItems();
        }


    }
}
