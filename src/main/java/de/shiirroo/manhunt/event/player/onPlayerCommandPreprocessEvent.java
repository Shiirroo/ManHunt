package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.world.Worldreset;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;
import java.util.*;

public class onPlayerCommandPreprocessEvent implements Listener {

    private static PlayerData playerData;
    private final Config config;
    private final TeamManager teamManager;

    public onPlayerCommandPreprocessEvent(PlayerData playerData, Config config, TeamManager teamManager) {
        this.playerData = playerData;
        this.config = config;
        this.teamManager = teamManager;

    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandPreprocessEvent (PlayerCommandPreprocessEvent event) throws IOException {
        if(StartGame.gameRunning == null && event.getPlayer().isOp()){
            List<String> chars = new ArrayList<>(Arrays.asList(event.getMessage().split(" ")));
            boolean eventBool =  Events.UpdatePlayerInventory(chars, event.getPlayer().getName());
            if(eventBool) {
                event.setCancelled(eventBool);
                if(!event.getPlayer().getName().equalsIgnoreCase(chars.get(1))) {
                    if(chars.get(0).equalsIgnoreCase("/op"))
                        event.getPlayer().sendMessage(config.getprefix() + ChatColor.GOLD +chars.get(1) + ChatColor.GRAY + " has been promoted to operator and can now execute ManHunt commands.");
                    else if(chars.get(0).equalsIgnoreCase("/deop"))
                        event.getPlayer().sendMessage(config.getprefix() +ChatColor.GOLD +chars.get(1) + ChatColor.GRAY + " has been removed from the operator.");
                }
            }

        }
        if(event.getPlayer().isOp()) {
            List<String> chars = new ArrayList<>(Arrays.asList(event.getMessage().split(" ")));
            if(chars.size() >= 2){
                if(chars.get(0).equalsIgnoreCase("/reload") && chars.get(1).equalsIgnoreCase("confirm")){
                    Worldreset.reset();
                }
            }
        }
    }

}
