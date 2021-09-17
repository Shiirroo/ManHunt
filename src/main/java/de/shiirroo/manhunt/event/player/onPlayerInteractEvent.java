package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.repeatingtask.CompassTracker;
import de.shiirroo.manhunt.utilis.config.Config;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class onPlayerInteractEvent implements Listener {

    public static HashMap<UUID, Long> compassClickDelay = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerInteractEvent(PlayerInteractEvent event) {
        if(VoteCommand.pause || Ready.ready != null){
            event.setCancelled(true);
        }
        if((!Config.getCompassAutoUpdate() && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COMPASS) && StartGame.gameRunning != null)  && !ManHuntPlugin.getPlayerData().getPlayerRole(event.getPlayer()).equals(ManHuntRole.Speedrunner)){
            if(compassClickDelay.get(event.getPlayer().getUniqueId() ) == null){
                compassClickDelay.put(event.getPlayer().getUniqueId(), new Date().getTime());
                CompassTracker.updateCompass(event.getPlayer());
            } else {
                Long Time = compassClickDelay.get(event.getPlayer().getUniqueId());
                Long TimeNow = new Date().getTime();
                if((TimeNow - Time) > (Config.getCompassTriggerTimer()* 1000L)){
                    compassClickDelay.put(event.getPlayer().getUniqueId(), new Date().getTime());
                    CompassTracker.updateCompass(event.getPlayer());
                } else {
                    event.getPlayer().sendActionBar(Component.text(ChatColor.GOLD + "Wait another "+  ChatColor.RED + ((Time - TimeNow)/1000 +Config.getCompassTriggerTimer()) +ChatColor.GOLD +" seconds"));
                }
            }
        }
    }
}
