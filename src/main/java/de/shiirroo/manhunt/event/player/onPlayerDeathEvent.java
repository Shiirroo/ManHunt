package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.world.Worldreset;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class onPlayerDeathEvent implements Listener {

    private static PlayerData playerData;
    private final Config config;
    private final Plugin plugin;

    public onPlayerDeathEvent(PlayerData playerData, Config config, Plugin plugin) {
        this.playerData = playerData;
        this.config = config;
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if(p.getPlayer() == null ) return;

        e.setCancelled(false);
        if (config.giveCompass() && playerData.getRole(p) != ManHuntRole.Speedrunner) {
            p.getInventory().addItem(new ItemStack(Material.COMPASS));
        }

        if(playerData.getRole(p.getPlayer()) == ManHuntRole.Assassin || playerData.getRole(p.getPlayer()) == ManHuntRole.Hunter){
            e.deathMessage(Component.text(config.getprefix() + ChatColor.GOLD+ playerData.getRole(p.getPlayer() )+ChatColor.GRAY+ " dies and is immediately back" ));
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if(e.getEntity().isDead()){
                    e.getEntity().spigot().respawn();
                }

            }, 20);

        } else if(playerData.getRole(p.getPlayer()) == ManHuntRole.Speedrunner )
        {
            e.setCancelled(true);
            p.getServer().sendMessage(Component.text(config.getprefix()).append(p.displayName()).append(Component.text(ChatColor.GRAY +" has left this world")));
            p.sendMessage(config.getprefix()  + ChatColor.RED + "You are now in the Spectator mode because you died");
            p.getPlayer().setGameMode(GameMode.SPECTATOR);
            boolean allSpeedrunnerdead = true;
            for(Player player : playerData.getPlayersByRole(ManHuntRole.Speedrunner)){
                if(player.getPlayer() == null) continue;
                if(!player.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
                    allSpeedrunnerdead = false;
                }
            }

            for(OfflinePlayer pl : Bukkit.getOfflinePlayers()){
                if(playerData.getRole(pl.getPlayer()) == ManHuntRole.Speedrunner){
                    if(pl.getPlayer() == null) continue;
                    if(!pl.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
                        allSpeedrunnerdead = false;
                    }
                }
            }
            if(allSpeedrunnerdead){
                Bukkit.getServer().sendMessage(Component.text(config.getprefix() + "All " +  ChatColor.DARK_PURPLE + "Speedrunners" +ChatColor.GRAY+ " are dead. " + ChatColor.RED + "Hunters " + ChatColor.GRAY+"win!!"));
                if(!ManHuntPlugin.debug) {
                    StartGame.gameStartTime = null;
                    Worldreset.setBoosBar(plugin);
                }
            }
        }
    }
}
