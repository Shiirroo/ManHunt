package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.utilis.repeatingtask.CompassTracker;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Date;

public class onPlayerInteractEvent implements Listener{

    @EventHandler(priority = EventPriority.HIGH)
    private void PlayerInteractEvent(PlayerInteractEvent event) {
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && ManHuntPlugin.getGameData().getGamePause().isPause()){
            event.setCancelled(true);
        }
        if((!Config.getCompassAutoUpdate() && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COMPASS) && ManHuntPlugin.getGameData().getGameStatus().isGameRunning())  && !ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(event.getPlayer().getUniqueId()).equals(ManHuntRole.Speedrunner)){
            if(ManHuntPlugin.getGameData().getGamePlayer().getCompassPlayerClickDelay().get(event.getPlayer().getUniqueId() ) == null){
                ManHuntPlugin.getGameData().getGamePlayer().getCompassPlayerClickDelay().put(event.getPlayer().getUniqueId(), new Date().getTime());
                CompassTracker.updateCompass(event.getPlayer());
            } else {
                Long Time = ManHuntPlugin.getGameData().getGamePlayer().getCompassPlayerClickDelay().get(event.getPlayer().getUniqueId());
                Long TimeNow = new Date().getTime();
                if((TimeNow - Time) > (Config.getCompassTriggerTimer()* 1000L)){
                    ManHuntPlugin.getGameData().getGamePlayer().getCompassPlayerClickDelay().put(event.getPlayer().getUniqueId(), new Date().getTime());
                    CompassTracker.updateCompass(event.getPlayer());
                } else {
                    event.getPlayer().sendActionBar(Component.text(ChatColor.GOLD + "Wait another "+  ChatColor.RED + ((Time - TimeNow)/1000 +Config.getCompassTriggerTimer()) +ChatColor.GOLD +" seconds"));
                }
            }
        }
    }
}
