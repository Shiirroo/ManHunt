package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.TeamChat;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class onAsyncPlayerChatEvent implements Listener{


    @EventHandler(priority = EventPriority.HIGH)
    public void AsyncPlayerChatEvent(AsyncChatEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
            event.setCancelled(true);
            return;
        }

        Component displayname = event.getPlayer().displayName();
        Component message = event.message().color(TextColor.fromHexString("#AAAAAA"));
        event.setCancelled(true);
        if(ManHuntPlugin.getGameData().getGamePlayer().getTeamchat().contains(event.getPlayer().getUniqueId()) && !ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(event.getPlayer().getUniqueId()).equals(ManHuntRole.Unassigned)){
            sendTeamChatMessage(event.getPlayer(), displayname, message);
        } else {
            Bukkit.getServer().sendMessage(displayname.append(Component.text(" >>> ").color(TextColor.fromHexString("#FFAA00"))).append(message));
        }
    }

    public static void sendTeamChatMessage(Player player, Component displayname, Component message){
        if(ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(player.getUniqueId()).equals(ManHuntRole.Speedrunner)){
            for(UUID uuid : ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner)){
                Player p = Bukkit.getPlayer(uuid);
                if(p != null) p.sendMessage(displayname.append(Component.text(ChatColor.GRAY +" ["+ ChatColor.AQUA +"TC" +ChatColor.GRAY+  "]"+ChatColor.RESET+ " >>> ").color(TextColor.fromHexString("#FFAA00"))).append(message));
            }
        } else {
            for (UUID uuid : ManHuntPlugin.getGameData().getPlayerData().getPlayersWithOutSpeedrunner()) {
                Player p = Bukkit.getPlayer(uuid);
                if(p != null) p.sendMessage(displayname.append(Component.text(ChatColor.GRAY + " [" + ChatColor.AQUA + "TC" + ChatColor.GRAY + "]" + ChatColor.RESET + " >>> ").color(TextColor.fromHexString("#FFAA00"))).append(message));
            }
        }
    }
}
