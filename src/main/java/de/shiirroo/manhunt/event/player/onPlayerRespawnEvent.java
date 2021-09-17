package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.config.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class onPlayerRespawnEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerRespawnEvent(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (Config.getGiveCompass() && ManHuntPlugin.getPlayerData().getPlayerRole(player) != ManHuntRole.Speedrunner)
            StartGame.getCompassTracker(player);
    }
}
