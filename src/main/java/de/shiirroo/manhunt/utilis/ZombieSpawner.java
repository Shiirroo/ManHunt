package de.shiirroo.manhunt.utilis;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import java.util.Objects;

public class ZombieSpawner {

    private final Player player;
    private final Zombie zombie;

    public ZombieSpawner(Player player){
        Zombie zombie = (Zombie)  player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
        zombie.setAdult();
        zombie.customName(player.displayName());
        zombie.setCustomNameVisible(true);
        zombie.setRemoveWhenFarAway(false);
        Objects.requireNonNull(zombie.getEquipment()).clear();
        zombie.getEquipment().setHelmet(player.getEquipment().getHelmet());
        zombie.getEquipment().setChestplate(player.getEquipment().getChestplate());
        zombie.getEquipment().setLeggings(player.getEquipment().getLeggings());
        zombie.getEquipment().setBoots(player.getEquipment().getBoots());
        zombie.getEquipment().setHelmetDropChance(0);
        zombie.getEquipment().setChestplateDropChance(0);
        zombie.getEquipment().setLeggingsDropChance(0);
        zombie.getEquipment().setBootsDropChance(0);
        zombie.clearLootTable();
        zombie.setAI(false);
        zombie.setHealth(player.getHealth());
        zombie.setSilent(true);
        zombie.setCanPickupItems(false);
        zombie.setShouldBurnInDay(false);
        zombie.setGravity(false);
        zombie.setJumping(player.isJumping());
        zombie.setSwimming(player.isSwimming());
        this.zombie = zombie;
        this.player = player;
    };


    public Player getPlayer() {
        return player;
    }

    public Zombie getZombie() {
        return zombie;
    }

    public void KillZombie(){
        zombie.remove();
    }
};