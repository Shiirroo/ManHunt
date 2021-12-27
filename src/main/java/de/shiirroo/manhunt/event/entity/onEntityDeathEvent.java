package de.shiirroo.manhunt.event.entity;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.player.onPlayerDeathEvent;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.utilis.repeatingtask.ZombieSpawner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class onEntityDeathEvent implements Listener{

    @EventHandler(priority = EventPriority.HIGH)
    public void EntityDeathEvent(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        Player killer = e.getEntity().getKiller();
        if(killer != null) {
            if (Events.cancelEvent(killer)){
                e.setCancelled(true);
                return;
            }

            if(KillZombie(entity, e)){
                return;
            }
            if(killer.getType() == EntityType.PLAYER && entity.getType() == EntityType.ENDER_DRAGON) {
                if (ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(killer.getUniqueId()).equals(ManHuntRole.Speedrunner)) {
                Bukkit.getServer().broadcastMessage(ManHuntPlugin.getprefix() + ChatColor.GOLD + "The Ender Dragon" + ChatColor.GRAY + " has been slain " + ChatColor.DARK_PURPLE + "Speedrunners" + ChatColor.GRAY + " WIN!!!");
            } else {
                Bukkit.getServer().broadcastMessage(ManHuntPlugin.getprefix() + ChatColor.GOLD + "The Ender Dragon" + ChatColor.GRAY + " has been slain " + ChatColor.DARK_PURPLE + "Speedrunners" + ChatColor.GRAY + " Lose!!!");
            }
                ManHuntPlugin.getWorldreset().resetBossBar();
            }
        }
    }

    private boolean KillZombie(Entity entity, EntityDeathEvent e){
        if(ManHuntPlugin.getGameData().getGameStatus().isGame() && Config.getSpawnPlayerLeaveZombie()) {
            if (entity.getType().equals(EntityType.ZOMBIE)) {
                Optional<ZombieSpawner> optionalZombieSpawner = ManHuntPlugin.getGameData().getGamePlayer().getZombieHashMap().values().stream().filter(z -> z.getZombieUUID().equals(e.getEntity().getUniqueId())).findFirst();
                if (optionalZombieSpawner.isPresent()) {
                    ZombieSpawner zombieSpawner = optionalZombieSpawner.get();
                    if (entity.getUniqueId().equals(zombieSpawner.getZombieUUID())) {
                        OfflinePlayer p = Bukkit.getOfflinePlayer(zombieSpawner.getUUID());
                        e.getDrops().clear();
                        e.setDroppedExp(zombieSpawner.getTotalExperience());
                        for (ItemStack itemStack : zombieSpawner.getInventory())
                            e.getDrops().add(itemStack);
                        if (ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(p.getUniqueId()) != null && ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(p.getUniqueId()).equals(ManHuntRole.Speedrunner)) {
                            onPlayerDeathEvent.SpeedrunnerDied(p.getUniqueId());
                            ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().remove(p.getUniqueId());
                            if (ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().get(p.getUniqueId()) != null) {
                                ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().remove(p.getUniqueId());
                            }
                            return true;
                        } else {
                            Bukkit.getServer().broadcastMessage(ManHuntPlugin.getprefix() + ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(p.getUniqueId()).getChatColor() + ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(p.getUniqueId()) + ChatColor.GRAY + " Zombie dies and will respawn on reconnect");
                        }
                    }
                }
            }
        }
        return false;
    }

}
