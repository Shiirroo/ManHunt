package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.ZombieSpawner;
import de.shiirroo.manhunt.utilis.config.Config;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class onPlayerLeave implements Listener {

    public static HashMap<UUID, ZombieSpawner> zombieHashMap =  new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerLeave(PlayerQuitEvent event) {

        Component displayname = event.getPlayer().displayName();

        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
            event.quitMessage(Component.text(""));
        } else {
            event.quitMessage(Component.text(ChatColor.GRAY+ "["+ChatColor.RED +"-"+ ChatColor.GRAY + "] ").append(displayname.color(displayname.color())));
        }
        if(StartGame.gameRunning == null && Ready.ready != null && !event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
            Ready.readyRemove(event.getPlayer(), true);
        }

        if(StartGame.gameRunning != null && Config.getSpawnPlayerLeaveZombie()){
            if(zombieHashMap.get(event.getPlayer().getUniqueId()) == null) {
                zombieHashMap.put(event.getPlayer().getUniqueId(), new ZombieSpawner(event.getPlayer()));
            } else {
                zombieHashMap.get(event.getPlayer().getUniqueId()).KillZombie();
                zombieHashMap.put(event.getPlayer().getUniqueId(), new ZombieSpawner(event.getPlayer()));
            }

        }

        if(VoteCommand.vote != null && !event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
            VoteCommand.vote.removeVote(event.getPlayer());

        }


        ManHuntRole mhr = ManHuntPlugin.getPlayerData().getPlayerRole(event.getPlayer());
        if(mhr != null) {
            Events.players.put(event.getPlayer().getUniqueId(), mhr);
            ManHuntPlugin.getPlayerData().reset(event.getPlayer(), ManHuntPlugin.getTeamManager());
        }
        if(Config.getBossbarCompass() && !BossBarCoordinates.hasCoordinatesBossbar(event.getPlayer())){
            BossBarCoordinates.deletePlayerCoordinatesBossbar(event.getPlayer());
        }
    }



}
