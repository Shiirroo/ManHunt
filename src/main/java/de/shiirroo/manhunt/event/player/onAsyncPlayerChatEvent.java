package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class onAsyncPlayerChatEvent implements Listener {


    public static void sendTeamChatMessage(Player player, String displayname, String message) {
        if (ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(player.getUniqueId()).equals(ManHuntRole.Speedrunner)) {
            for (UUID uuid : ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner)) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null)
                    p.sendMessage(displayname + ChatColor.GRAY + " [" + ChatColor.AQUA + "TC" + ChatColor.GRAY + "]" + ChatColor.GOLD + " >>> " + message);
            }
        } else {
            for (UUID uuid : ManHuntPlugin.getGameData().getPlayerData().getPlayersWithOutSpeedrunner()) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null)
                    p.sendMessage(displayname + ChatColor.GRAY + " [" + ChatColor.AQUA + "TC" + ChatColor.GRAY + "]" + ChatColor.GOLD + " >>> " + message);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            event.setCancelled(true);
            return;
        }

        String displayname = event.getPlayer().getDisplayName();
        String message = ChatColor.GRAY + "" + event.getMessage();
        event.setCancelled(true);
        if (ManHuntPlugin.getGameData().getGamePlayer().getTeamchat().contains(event.getPlayer().getUniqueId()) && !ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(event.getPlayer().getUniqueId()).equals(ManHuntRole.Unassigned)) {
            sendTeamChatMessage(event.getPlayer(), displayname, message);
        } else {
            Bukkit.getServer().broadcastMessage(displayname + ChatColor.GOLD + " >>> " + "" + message);
        }
    }
}
