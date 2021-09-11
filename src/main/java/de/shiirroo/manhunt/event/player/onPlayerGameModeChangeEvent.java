package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class onPlayerGameModeChangeEvent implements Listener {

        @EventHandler(priority = EventPriority.HIGH)
        public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
            if(VoteCommand.pause){
                event.setCancelled(true);
            }

            if(VoteCommand.vote != null && event.getNewGameMode().equals(GameMode.SPECTATOR)){
                VoteCommand.vote.removeVote(event.getPlayer());
            }

            if(Ready.ready != null && event.getNewGameMode().equals(GameMode.SPECTATOR)){
                Ready.readyRemove(event.getPlayer(),  (int) (Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count()) - 1);
                ManHuntPlugin.getPlayerData().switchGameMode(event.getPlayer(), ManHuntPlugin.getTeamManager());

            } else {
                ManHuntPlugin.getPlayerData().setUpdateRole(event.getPlayer(), ManHuntPlugin.getTeamManager());
            }
    }

}
