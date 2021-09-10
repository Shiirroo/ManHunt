package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.utilis.Utilis;
import de.shiirroo.manhunt.utilis.Worker;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.Date;

public class onPlayerMove implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if(p.getPlayer() == null ) return;

        if(VoteCommand.pause) event.setCancelled(true);

        if(Config.getBossbarCompass() && !BossBarCoordinates.hasCoordinatesBossbar(event.getPlayer())){
            BossBarCoordinates.addPlayerCoordinatesBossbar(event.getPlayer());
        }
        if(StartGame.gameRunning != null) {
            Worker.setPlayerlast(p);
        }



        if(StartGame.gameRunning == null || StartGame.gameRunning != null && StartGame.gameRunning.isRunning()  && !ManHuntPlugin.getPlayerData().getPlayerRole(event.getPlayer()).equals(ManHuntRole.Speedrunner)){
            GameNotStartetPos(p);
        }

        if (ManHuntPlugin.getPlayerData().isFrozen(event.getPlayer()))
            event.setCancelled(true);
    }



    private void GameNotStartetPos (Player p){
        double dX = p.getLocation().getX() - p.getWorld().getSpawnLocation().getX();
        double dZ = p.getLocation().getZ() - p.getWorld().getSpawnLocation().getZ();
        Location loc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
        if (dX > 10d) {
            loc.setX(loc.getX() - 0.2);
        }
        if (dZ > 10d) {
            loc.setZ(loc.getZ() - 0.2);
        }
        if (dX < -10d) {
            loc.setX(loc.getX() + 0.2);
        }
        if (dZ < -10d) {
            loc.setZ(loc.getZ() + 0.2);
        }
        if ((dX > 6d || dZ > 6d || dX < -6d || dZ < -6d)) {
            Utilis.drawWorldBorder(p, dX, dZ);
        }
        if ((dX > 30d || dZ > 30d || dX < -30d || dZ < -30d)) {
            p.teleport(p.getLocation().getWorld().getSpawnLocation());
        } else if (dX > 10d || dZ > 10d || dX < -10d || dZ < -10d) {
            if (!p.isJumping() && !p.isFlying()) {
                p.teleport(loc);
                Events.playerExit.put(p.getUniqueId(), (new Date().getTime() + 2000));
                p.sendActionBar(Component.text(ChatColor.RED + "You can't leave this area"));
            }
        }
        if(Events.playerExit.get(p.getUniqueId()) != null){
            if(new Date().getTime() - Events.playerExit.get(p.getUniqueId()).longValue() > 0){
                Events.playerExit.remove(p.getUniqueId());
            }
        }

    }

}
