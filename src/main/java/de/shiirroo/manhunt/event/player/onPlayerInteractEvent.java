package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.utilis.Worker;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Date;

public class onPlayerInteractEvent implements Listener {

    private static PlayerData playerData;
    private final Config config;

    public onPlayerInteractEvent(PlayerData playerData, Config config) {
        this.playerData = playerData;
        this.config = config;
    }


    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerInteractEvent(PlayerInteractEvent event) {
        if((!config.isCompassAutoUpdate() && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COMPASS) && StartGame.gameRunning != null)  && !playerData.getPlayerRole(event.getPlayer()).equals(ManHuntRole.Speedrunner)){
            if(Worker.compassclickdelay.get(event.getPlayer().getUniqueId() ) == null){
                Worker.compassclickdelay.put(event.getPlayer().getUniqueId(), new Date().getTime());
                Worker.updateCompass(event.getPlayer());
            } else {
                Long Time = Worker.compassclickdelay.get(event.getPlayer().getUniqueId());
                Long TimeNow = new Date().getTime();
                if((TimeNow - Time) > (config.compassTriggerTimer()* 1000L)){
                    Worker.compassclickdelay.put(event.getPlayer().getUniqueId(), new Date().getTime());
                    Worker.updateCompass(event.getPlayer());
                } else {
                    event.getPlayer().sendActionBar(Component.text(ChatColor.GOLD + "Wait another "+  ChatColor.RED + ((Time - TimeNow)/1000 +config.compassTriggerTimer()) +ChatColor.GOLD +" seconds"));
                }
            }
        }
    }
}
