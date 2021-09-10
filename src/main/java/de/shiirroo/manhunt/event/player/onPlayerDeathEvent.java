package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.StartGame;
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

public class onPlayerDeathEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if(p.getPlayer() == null ) return;

        e.setCancelled(false);
        if (Config.getGiveCompass() && ManHuntPlugin.getPlayerData().getPlayerRole(p) != ManHuntRole.Speedrunner) {
            p.getInventory().addItem(new ItemStack(Material.COMPASS));
        }

        if(ManHuntPlugin.getPlayerData().getPlayerRole(p.getPlayer()) == ManHuntRole.Assassin || ManHuntPlugin.getPlayerData().getPlayerRole(p.getPlayer()) == ManHuntRole.Hunter){
            e.deathMessage(Component.text(ManHuntPlugin.getprefix() + ChatColor.GOLD+ ManHuntPlugin.getPlayerData().getPlayerRole(p.getPlayer() )+ChatColor.GRAY+ " dies and is immediately back" ));
            Bukkit.getScheduler().scheduleSyncDelayedTask(ManHuntPlugin.getPlugin(), () -> {
                if(e.getEntity().isDead()){
                    e.getEntity().spigot().respawn();
                }

            }, 20);

        } else if(ManHuntPlugin.getPlayerData().getPlayerRole(p.getPlayer()) == ManHuntRole.Speedrunner )
        {
            e.setCancelled(true);
            p.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix()).append(p.displayName()).append(Component.text(ChatColor.GRAY +" has left this world")));
            p.sendMessage(ManHuntPlugin.getprefix()  + ChatColor.RED + "You are now in the Spectator mode because you died");
            p.getPlayer().setGameMode(GameMode.SPECTATOR);
            boolean allSpeedrunnerdead = true;
            for(Player player : ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner)){
                if(player.getPlayer() == null) continue;
                if(!player.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
                    allSpeedrunnerdead = false;
                }
            }

            for(OfflinePlayer pl : Bukkit.getOfflinePlayers()){
                if(ManHuntPlugin.getPlayerData().getPlayerRole(pl.getPlayer()) == ManHuntRole.Speedrunner){
                    if(pl.getPlayer() == null) continue;
                    if(!pl.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
                        allSpeedrunnerdead = false;
                    }
                }
            }
            if(allSpeedrunnerdead){
                Bukkit.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix() + "All " +  ChatColor.DARK_PURPLE + "Speedrunners" +ChatColor.GRAY+ " are dead. " + ChatColor.RED + "Hunters " + ChatColor.GRAY+"win!!"));
                if(!ManHuntPlugin.debug) {
                    StartGame.gameStartTime = null;
                    Worldreset.setBoosBar(ManHuntPlugin.getPlugin());
                }
            }
        }
    }
}
