package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class onPlayerRespawnEvent implements Listener {

    private static PlayerData playerData;
    private final Config config;

    public onPlayerRespawnEvent(PlayerData playerData, Config config) {
        this.playerData = playerData;
        this.config = config;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (config.giveCompass() && playerData.getRole(player) != ManHuntRole.Speedrunner)
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
    }
}
