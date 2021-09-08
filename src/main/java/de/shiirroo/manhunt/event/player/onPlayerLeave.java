package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class onPlayerLeave implements Listener {

    private static PlayerData playerData;
    private final Config config;
    private final TeamManager teamManager;

    public onPlayerLeave(PlayerData playerData, Config config, TeamManager teamManager) {
        this.playerData = playerData;
        this.config = config;
        this.teamManager = teamManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Component displayname = event.getPlayer().displayName();

        if (playerData.getPlayerRole(event.getPlayer()) == null || playerData.getPlayerRole(event.getPlayer()).equals(ManHuntRole.Unassigned)){
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            if(StartGame.gameRunning != null)
                event.quitMessage(Component.text(""));
            else
                event.quitMessage(Component.text("- ").color(TextColor.fromHexString("#FF5555")).append(displayname.color(displayname.color())));
        } else {
            event.quitMessage(Component.text("- ").color(TextColor.fromHexString("#FF5555")).append(displayname.color(displayname.color())));

        }



        ManHuntRole mhr = playerData.getPlayerRole(event.getPlayer());
        if(mhr == null) return;
        Events.players.put(event.getPlayer().getUniqueId(), mhr);
        playerData.reset(event.getPlayer(),teamManager);
        if(config.isBossbarCompass() && !BossBarCoordinates.hasCoordinatesBossbar(event.getPlayer())){
            BossBarCoordinates.deletePlayerCoordinatesBossbar(event.getPlayer());
        }
        if(StartGame.gameRunning == null){
            Ready.readyRemove(event.getPlayer(), (Bukkit.getOnlinePlayers().size() - 1));
        }
    }
}
