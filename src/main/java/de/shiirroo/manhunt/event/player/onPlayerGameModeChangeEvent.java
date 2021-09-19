package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.repeatingtask.CompassTracker;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class onPlayerGameModeChangeEvent implements Listener {

        @EventHandler(priority = EventPriority.HIGH)
        public void PlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
             if(VoteCommand.pause){
                event.setCancelled(true);
            }

            if(VoteCommand.vote != null && event.getNewGameMode().equals(GameMode.SPECTATOR)){
                VoteCommand.vote.removeVote(event.getPlayer());
            }

            if(Ready.ready != null && event.getNewGameMode().equals(GameMode.SPECTATOR)) {
                Ready.readyRemove(event.getPlayer(), true);
            }
            ManHuntPlugin.getPlayerData().switchGameMode(event.getPlayer(), ManHuntPlugin.getTeamManager(), event.getNewGameMode());

            if(Events.playerWorldMap.get(event.getPlayer().getUniqueId()) != null){
                Events.playerWorldMap.get(event.getPlayer().getUniqueId()).updatePlayer(event.getPlayer());
                CompassTracker.setPlayerlast(event.getPlayer());
            }

            if(Events.playerMenu.get(event.getPlayer().getUniqueId()) != null && Events.playerMenu.get(event.getPlayer().getUniqueId()).getInventory().equals(event.getPlayer().getInventory())){
                Events.playerMenu.get(event.getPlayer().getUniqueId()).setGameMode(event.getNewGameMode());
                Events.playerMenu.get(event.getPlayer().getUniqueId()).setMenuItems();
            }


        }
}
