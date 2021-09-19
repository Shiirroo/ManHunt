package de.shiirroo.manhunt.event.entity;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.player.onPlayerLeave;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.repeatingtask.ZombieSpawner;
import de.shiirroo.manhunt.utilis.config.Config;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Optional;

public class onEntityDamageByEntityEvent implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    private void EntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(VoteCommand.pause) event.setCancelled(true);
        if (event.getDamager().getType() != EntityType.PLAYER) return;
        Player attacker = (Player) event.getDamager();
        if (event.getEntity().getType() != EntityType.PLAYER) {
            if (event.getEntity().getType().equals(EntityType.ZOMBIE)) {
                Optional<ZombieSpawner> optionalZombieSpawner = onPlayerLeave.zombieHashMap.values().stream().filter(z -> z.getZombie().equals(event.getEntity())).findFirst();
                if (optionalZombieSpawner.isPresent()) {
                    ZombieSpawner zombieSpawner = optionalZombieSpawner.get();
                    if(Events.players.get(zombieSpawner.getPlayer().getUniqueId()) != null){
                        if(Events.players.get(zombieSpawner.getPlayer().getUniqueId()).equals(ManHuntRole.Speedrunner) && ManHuntPlugin.getPlayerData().getPlayerRole(attacker).equals(ManHuntRole.Speedrunner)){
                            event.setCancelled(true);
                        } else if(!Events.players.get(zombieSpawner.getPlayer().getUniqueId()).equals(ManHuntRole.Speedrunner) && !ManHuntPlugin.getPlayerData().getPlayerRole(attacker).equals(ManHuntRole.Speedrunner) ){
                            event.setCancelled(true);
                        }
                    }
                }
            }


            return;
        };
        Player player = (Player) event.getEntity();
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
