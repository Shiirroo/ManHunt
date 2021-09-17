package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Utilis;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.utilis.repeatingtask.CompassTracker;
import de.shiirroo.manhunt.world.Worldreset;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class onPlayerDeathEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (p.getPlayer() == null) return;

        if (Config.getGiveCompass() && ManHuntPlugin.getPlayerData().getPlayerRole(p) != ManHuntRole.Speedrunner) {
            for(ItemStack is : e.getDrops()){
                if(is.equals(new ItemStack(Material.COMPASS))){
                    e.getDrops().remove(is);
                }
            }
        }

        if (ManHuntPlugin.getPlayerData().getPlayerRole(p.getPlayer()) == ManHuntRole.Assassin || ManHuntPlugin.getPlayerData().getPlayerRole(p.getPlayer()) == ManHuntRole.Hunter) {
            e.deathMessage(Component.text(ManHuntPlugin.getprefix() + ManHuntPlugin.getPlayerData().getPlayerRole(p.getPlayer()).getChatColor()+ ManHuntPlugin.getPlayerData().getPlayerRole(p.getPlayer() )+ChatColor.GRAY+ " dies and is immediately back" ));
            Bukkit.getScheduler().scheduleSyncDelayedTask(ManHuntPlugin.getPlugin(), () -> {
                if (e.getEntity().isDead()) {
                    e.getEntity().spigot().respawn();
                }
            }, 20);

        } else if (ManHuntPlugin.getPlayerData().getPlayerRole(p.getPlayer()) == ManHuntRole.Speedrunner) {
            e.setCancelled(true);
            SpeedrunnerDied(p);
        }
    }

    public static void SpeedrunnerDied(Player p){
            ChatColor chatColor;
            if (p.isOnline()) {
                p.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED + "You are now in the Spectator mode because you died");
                p.getPlayer().setGameMode(GameMode.SPECTATOR);
                chatColor = ManHuntPlugin.getPlayerData().getPlayerRole(p).getChatColor();
            } else
                chatColor = Events.players.get(p.getUniqueId()).getChatColor();
            if(StartGame.playersonStart.contains(p.getUniqueId())) {
                StartGame.playersonStart.remove(p.getUniqueId());
                Events.playerWorldMap.remove(p.getUniqueId());
            Bukkit.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix() + chatColor + p.getDisplayName() + ChatColor.GRAY + " has left this world"));
            if (Worldreset.worldReset == null || Worldreset.worldReset != null && !Worldreset.worldReset.isRunning()) {
                Utilis.allSpeedrunnersDead();
            }
        }
    }
}
