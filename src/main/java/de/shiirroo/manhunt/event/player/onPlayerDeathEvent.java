package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Utilis;
import de.shiirroo.manhunt.utilis.config.Config;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class onPlayerDeathEvent implements Listener{

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerDeathEvent(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (p.getPlayer() == null) return;

        if (Config.getGiveCompass() && ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(p.getUniqueId()) != ManHuntRole.Speedrunner) {
            e.getDrops().removeIf(is -> is.equals(new ItemStack(Material.COMPASS)));
        }

        if (ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(p.getUniqueId()) == ManHuntRole.Assassin || ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(p.getUniqueId()) == ManHuntRole.Hunter) {
            e.setDeathMessage(ManHuntPlugin.getprefix() + ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(p.getUniqueId()).getChatColor()+ ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(p.getUniqueId()) + ChatColor.GRAY+ " dies and is immediately back" );
            Bukkit.getScheduler().scheduleSyncDelayedTask(ManHuntPlugin.getPlugin(), () -> {
                if (e.getEntity().isDead()) {
                    e.getEntity().spigot().respawn();
                }
            }, 20);

        } else if (ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(p.getUniqueId()) == ManHuntRole.Speedrunner) {
            e.setDeathMessage("");
            Bukkit.getLogger().info(ManHuntPlugin.getprefix() + "Speedrunner died :" + p.getDisplayName());
            SpeedrunnerDied(p.getUniqueId());
        }
    }

    public static void SpeedrunnerDied(UUID uuid){
            ChatColor chatColor;
            Player p = Bukkit.getPlayer(uuid);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if (p != null  &&  p.isOnline()) {
                p.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED + "You are now in the Spectator mode because you died");
                Objects.requireNonNull(p).setGameMode(GameMode.SPECTATOR);
                chatColor = ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(p.getUniqueId()).getChatColor();
            }
            else chatColor = ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(uuid).getChatColor();

            if(ManHuntPlugin.getGameData().getGameStatus().getLivePlayerList().contains(uuid)) {
                ManHuntPlugin.getGameData().getGameStatus().getLivePlayerList().remove(uuid);
                ManHuntPlugin.getGameData().getGamePlayer().getPlayerWorldMap().remove(uuid);
                Bukkit.getServer().broadcastMessage(ManHuntPlugin.getprefix() + chatColor + (p != null ? p.getName() : offlinePlayer.getName()) + ChatColor.GRAY + " has left this world");
                if (!ManHuntPlugin.getWorldreset().getWorldReset().isRunning()) {
                    Utilis.allSpeedrunnersDead();
                }

        }
    }
}
