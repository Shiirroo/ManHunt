package de.shiirroo.manhunt.event.entity;

import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.world.Worldreset;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

public class onEntityDeathEvent implements Listener {

    private static PlayerData playerData;
    private final Config config;
    private final Plugin plugin;

    public onEntityDeathEvent(PlayerData playerData, Config config, Plugin plugin) {
        this.playerData = playerData;
        this.config = config;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeathEvent(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        Player killer = e.getEntity().getKiller();
        if(killer == null) return;
        if(killer.getType() == EntityType.PLAYER && entity.getType() == EntityType.ENDER_DRAGON && StartGame.gameRunning != null){
            if(playerData.getPlayerRole(killer).equals(ManHuntRole.Speedrunner)) {
                Bukkit.getServer().sendMessage(Component.text(config.getprefix() + ChatColor.GOLD + "The Ender Dragon" + ChatColor.GRAY + " has been slain " + ChatColor.DARK_PURPLE + "Speedrunners" + ChatColor.GRAY + " WIN!!!"));
            } else{
                Bukkit.getServer().sendMessage(Component.text(config.getprefix() + ChatColor.GOLD + "The Ender Dragon" + ChatColor.GRAY + " has been slain " + ChatColor.DARK_PURPLE + "Speedrunners" + ChatColor.GRAY + " Lose!!!"));
            }
            StartGame.gameStartTime = null;
            Worldreset.setBoosBar(plugin);

        }
    }
}
