package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.menu.MenuManager;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.menus.PlayerMenu;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class onPlayerJoin implements Listener {


    private static PlayerData playerData;
    private final Config config;
    private final TeamManager teamManager;

    public onPlayerJoin(PlayerData playerData, Config config, TeamManager teamManager) {
        this.playerData = playerData;
        this.config = config;
        this.teamManager = teamManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) throws MenuManagerException, MenuManagerNotSetupException {
        Player p = event.getPlayer();
        if(p.getPlayer() == null ) return;

        ManHuntRole mhr = GetRoleOfflinePlayer(p.getPlayer());
        if(mhr != null) playerData.setRole(p.getPlayer(), GetRoleOfflinePlayer(p), teamManager);
        else playerData.setRole(p.getPlayer(), ManHuntRole.Unassigned, teamManager);

        Component displayname = event.getPlayer().displayName();

        if(StartGame.gameRunning == null){
            p.getInventory().clear();
            p.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
            Events.playerMenu.put(event.getPlayer().getUniqueId(), MenuManager.openMenu(PlayerMenu.class, event.getPlayer(), null, playerData));
            event.joinMessage(Component.text("+ ").color(TextColor.fromHexString("#55FF55")).append(displayname.color(displayname.color())));
            if(Ready.ready != null) {
                if (Ready.ready.hasPlayerVote(p)) {
                    Ready.readyRemove(p, Bukkit.getOnlinePlayers().size());
                }
            }
        }


        if(StartGame.gameRunning != null) {
            if (playerData.getPlayerRole(event.getPlayer()) == null || playerData.getPlayerRole(event.getPlayer()).equals(ManHuntRole.Unassigned)){
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
                event.joinMessage(Component.text(""));
            } else {
                event.joinMessage(Component.text("+ ").color(TextColor.fromHexString("#55FF55")).append(displayname.color(displayname.color())));

            }
        }

        if(StartGame.gameRunning != null && StartGame.gameRunning.isRunning()){
            StartGame.gameRunning.setBossBarPlayer(event.getPlayer());
        }

        if(config.isBossbarCompass() && !BossBarCoordinates.hasCoordinatesBossbar(event.getPlayer())){
            BossBarCoordinates.addPlayerCoordinatesBossbar(event.getPlayer());
        }
    }

    private ManHuntRole GetRoleOfflinePlayer(Player player){
        return Events.players.get(player.getUniqueId());
    }
}
