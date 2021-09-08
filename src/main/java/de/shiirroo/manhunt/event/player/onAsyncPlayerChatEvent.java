package de.shiirroo.manhunt.event.player;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class onAsyncPlayerChatEvent implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerChatEvent(AsyncChatEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
            event.setCancelled(true);
            return;
        }

        Component displayname = event.getPlayer().displayName();
        boolean co = displayname.color() == null;
        Component message = event.message().color(TextColor.fromHexString("#AAAAAA"));
        event.setCancelled(true);
        Bukkit.getServer().sendMessage(displayname.color(co ? TextColor.fromHexString("#FFFF55") : displayname.color()).append(Component.text(" >>> ").color(TextColor.fromHexString("#FFAA00"))).append(message));
    }
}
