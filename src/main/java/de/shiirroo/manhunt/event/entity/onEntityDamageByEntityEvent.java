package de.shiirroo.manhunt.event.entity;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.config.Config;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class onEntityDamageByEntityEvent implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(VoteCommand.pause) event.setCancelled(true);
        if (event.getDamager().getType() != EntityType.PLAYER) return;
        if (event.getEntity().getType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();
        if (StartGame.gameRunning == null){ event.setCancelled(true);}
        if (StartGame.gameRunning != null) {
            if (ManHuntPlugin.getPlayerData().getPlayerRole(attacker) == ManHuntRole.Hunter &&
                    ManHuntPlugin.getPlayerData().getPlayerRole(player) == ManHuntRole.Assassin ||
                    ManHuntPlugin.getPlayerData().getPlayerRole(attacker) == ManHuntRole.Assassin &&
                            ManHuntPlugin.getPlayerData().getPlayerRole(player) == ManHuntRole.Hunter
            ) {
                event.setCancelled(true);

            }
        }

        if(ManHuntPlugin.getPlayerData().isFrozen(attacker)){
            event.setCancelled(true);
        }

        if(ManHuntPlugin.getPlayerData().getPlayerRole(attacker) == ManHuntRole.Assassin && ManHuntPlugin.getPlayerData().getPlayerRole(player) == ManHuntRole.Speedrunner && !ManHuntPlugin.getPlayerData().isFrozen(attacker) ){
            if(Config.getAssassinsInstaKill()){
                player.setHealth(0);
            } else {
                if(player.getInventory().getBoots() != null) {
                    player.getInventory().setBoots(null);
                } else if(player.getInventory().getHelmet() != null) {
                    player.getInventory().setHelmet(null);
                } else if(player.getInventory().getLeggings() != null) {
                    player.getInventory().setLeggings(null);
                }else if(player.getInventory().getChestplate() != null) {
                    player.getInventory().setChestplate(null);
                } else player.setHealth(0);
            }
        }
    }

}
