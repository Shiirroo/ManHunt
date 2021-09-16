package de.shiirroo.manhunt.event.entity;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.menu.menus.ConfigMenu;
import de.shiirroo.manhunt.event.player.onPlayerDeathEvent;
import de.shiirroo.manhunt.event.player.onPlayerLeave;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Utilis;
import de.shiirroo.manhunt.utilis.ZombieSpawner;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.world.Worldreset;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class onEntityDeathEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeathEvent(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        Player killer = e.getEntity().getKiller();
        if(StartGame.gameRunning != null && Config.getSpawnPlayerLeaveZombie()) {
            if (entity.getType().equals(EntityType.ZOMBIE)) {
                for (UUID uuid : onPlayerLeave.zombieHashMap.keySet()) {
                    ZombieSpawner zombieSpawner = onPlayerLeave.zombieHashMap.get(uuid);
                    if (entity.equals(zombieSpawner.getZombie())) {
                        Player p = zombieSpawner.getPlayer();
                        e.getDrops().clear();
                        e.setDroppedExp(p.getTotalExperience());
                        if (Events.players.get(uuid) != null && Events.players.get(uuid).equals(ManHuntRole.Speedrunner)) {
                            onPlayerDeathEvent.SpeedrunnerDied(p);
                        } else {;
                            Bukkit.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix() + Events.players.get(uuid).getChatColor()+ Events.players.get(uuid) + ChatColor.GRAY + " Zombie dies and will respawn on reconnect"));
                        }
                        break;
                    }
                }
            }
        }
        if(killer == null) return;
        if(killer.getType() == EntityType.PLAYER && entity.getType() == EntityType.ENDER_DRAGON && StartGame.gameRunning != null){
            if(ManHuntPlugin.getPlayerData().getPlayerRole(killer).equals(ManHuntRole.Speedrunner)) {
                Bukkit.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix() + ChatColor.GOLD + "The Ender Dragon" + ChatColor.GRAY + " has been slain " + ChatColor.DARK_PURPLE + "Speedrunners" + ChatColor.GRAY + " WIN!!!"));
            } else{
                Bukkit.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix() + ChatColor.GOLD + "The Ender Dragon" + ChatColor.GRAY + " has been slain " + ChatColor.DARK_PURPLE + "Speedrunners" + ChatColor.GRAY + " Lose!!!"));
            }
            StartGame.gameStartTime = null;
            Worldreset.resetBossBar();

        }
    }


}
