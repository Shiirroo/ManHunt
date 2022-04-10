package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.utilis.Utilis;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.utilis.repeatingtask.CompassTracker;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
    public void PlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (p.getPlayer() == null) return;
        if (Events.cancelEvent(p)) {
            if (ManHuntPlugin.getGameData().getGamePause().isPause()) {
                p.setRemainingAir(p.getMaximumAir());
                event.setCancelled(true);
            } else if (ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().entrySet().stream().noneMatch(uuiduuidEntry -> uuiduuidEntry.getValue().equals(p.getUniqueId())))
                GameNotStartetPos(p);
        }

        if (Config.getBossbarCompass() && !BossBarCoordinates.hasCoordinatesBossbar(p)) {
            BossBarCoordinates.addPlayerCoordinatesBossbar(p);
        }
        CompassTracker.setPlayerlast(p);

        if (ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().entrySet().stream().anyMatch(uuiduuidEntry -> uuiduuidEntry.getValue().equals(p.getUniqueId())))
            event.setCancelled(true);
    }


    private void GameNotStartetPos(Player p) {
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
            p.teleport(loc);
            ManHuntPlugin.getGameData().getGamePlayer().getPlayerExitGameAreaTimer().put(p.getUniqueId(), (new Date().getTime() + 2000));
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "You can't leave this area"));
        }
        if (ManHuntPlugin.getGameData().getGamePlayer().getPlayerExitGameAreaTimer().get(p.getUniqueId()) != null) {
            if (new Date().getTime() - ManHuntPlugin.getGameData().getGamePlayer().getPlayerExitGameAreaTimer().get(p.getUniqueId()) > 0) {
                ManHuntPlugin.getGameData().getGamePlayer().getPlayerExitGameAreaTimer().remove(p.getUniqueId());
            }
        }

    }

}
