package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.vote.VoteCommand;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.menus.PlayerMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.SettingsMenu;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.utilis.repeatingtask.ZombieSpawner;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.Serializable;

public class onPlayerLeave implements Listener, Serializable {


    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerLeave(PlayerQuitEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
            event.setQuitMessage("");
        } else {
            event.setQuitMessage(ChatColor.GRAY+ "["+ChatColor.RED +"-"+ ChatColor.GRAY + "] " + event.getPlayer().getDisplayName());
        }

        PlayerMenu.SelectGroupMenu.remove(event.getPlayer().getUniqueId());
        SettingsMenu.GamePreset.remove(event.getPlayer().getUniqueId());


        if(!ManHuntPlugin.getGameData().getGameStatus().isGame()){
            ManHuntPlugin.getGameData().getGamePlayer().removePlayer(event.getPlayer().getUniqueId());
        }


        if(!ManHuntPlugin.getGameData().getGameStatus().isGame() && ManHuntPlugin.getGameData().getGameStatus().isReadyForVote() && !event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
            Ready.readyRemove(event.getPlayer(), true);
            PlayerMenu.SelectGroupMenu.values().forEach(Menu::setMenuItems);
        }

        if(ManHuntPlugin.getGameData().getGameStatus().isGame() && Config.getSpawnPlayerLeaveZombie()){
            if (ManHuntPlugin.getGameData().getGamePlayer().getZombieHashMap().get(event.getPlayer().getUniqueId()) != null) {
                ManHuntPlugin.getGameData().getGamePlayer().getZombieHashMap().get(event.getPlayer().getUniqueId()).KillZombie();
            }
            ManHuntPlugin.getGameData().getGamePlayer().getZombieHashMap().put(event.getPlayer().getUniqueId(), new ZombieSpawner(event.getPlayer()));

        }

        if(VoteCommand.getVote() != null){
            SettingsMenu.GamePreset.values().forEach(Menu::setMenuItems);
            if(!event.getPlayer().getGameMode().equals(GameMode.SPECTATOR))
                VoteCommand.getVote().getVoteCreator().removeVote(event.getPlayer());
        }


        ManHuntRole mhr = ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(event.getPlayer().getUniqueId());
        if(mhr != null) {
            ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().put(event.getPlayer().getUniqueId(), mhr);
            ManHuntPlugin.getGameData().getPlayerData().reset(event.getPlayer(),ManHuntPlugin.getTeamManager());
        }
        if(Config.getBossbarCompass() && !BossBarCoordinates.hasCoordinatesBossbar(event.getPlayer())){
            BossBarCoordinates.deletePlayerCoordinatesBossbar(event.getPlayer());
        }
    }

}
