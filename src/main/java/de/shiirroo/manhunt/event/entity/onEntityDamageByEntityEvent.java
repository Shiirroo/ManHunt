package de.shiirroo.manhunt.event.entity;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.utilis.repeatingtask.ZombieSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Optional;

public class onEntityDamageByEntityEvent implements Listener{


    @EventHandler(priority = EventPriority.HIGH)
    private void EntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType().equals(EntityType.PLAYER)){
            Player attacker = (Player) event.getDamager();
            if (Events.cancelEvent(attacker)) {
                event.setCancelled(true);
            } else if (event.getEntity().getType().equals(EntityType.ZOMBIE)) {
                Optional<ZombieSpawner> optionalZombieSpawner = ManHuntPlugin.getGameData().getGamePlayer().getZombieHashMap().values().stream().filter(z -> z.getZombieUUID().equals(event.getEntity().getUniqueId())).findFirst();
                if (optionalZombieSpawner.isPresent()) {
                    ZombieSpawner zombieSpawner = optionalZombieSpawner.get();
                    if (ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(zombieSpawner.getUUID()) != null) {
                        if (ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(zombieSpawner.getUUID()).equals(ManHuntRole.Speedrunner) && ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(attacker.getUniqueId()).equals(ManHuntRole.Speedrunner)) {
                            event.setCancelled(true);
                        } else if (!ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(zombieSpawner.getUUID()).equals(ManHuntRole.Speedrunner) && !ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(attacker.getUniqueId()).equals(ManHuntRole.Speedrunner)) {
                            event.setCancelled(true);
                        }
                    }
                }
            } else if (event.getEntity().getType().equals(EntityType.PLAYER)) {
                Player player = (Player) event.getEntity();

                if (ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(attacker.getUniqueId()) == ManHuntRole.Hunter && ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(player.getUniqueId()) == ManHuntRole.Assassin ||
                        ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(attacker.getUniqueId()) == ManHuntRole.Assassin && ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(player.getUniqueId()) == ManHuntRole.Hunter) {
                    event.setCancelled(true);
                }


                if (ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(attacker.getUniqueId()) == ManHuntRole.Assassin && ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(player.getUniqueId()) == ManHuntRole.Speedrunner) {
                    if (Config.getAssassinsInstaKill()) {
                        player.setHealth(0);
                    } else {


                        if (player.getInventory().getBoots() != null) {
                            player.getInventory().setBoots(null);
                        } else if (player.getInventory().getHelmet() != null) {
                            player.getInventory().setHelmet(null);
                        } else if (player.getInventory().getLeggings() != null) {
                            player.getInventory().setLeggings(null);
                        } else if (player.getInventory().getChestplate() != null) {
                            player.getInventory().setChestplate(null);
                        } else player.setHealth(0);
                    }
                }
            }
        }
    }
}
