package de.shiirroo.manhunt.utilis.repeatingtask;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ZombieSpawner implements Serializable {

    private final UUID uuid;
    private final UUID zombieUUID;
    private final ItemStack[] inventory;
    private final int totalExperience;

    public ZombieSpawner(Player player) {
        Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
        zombie.setAdult();
        zombie.setCustomName(player.getDisplayName());
        zombie.setCustomNameVisible(true);
        zombie.setRemoveWhenFarAway(false);
        Objects.requireNonNull(zombie.getEquipment()).clear();
        zombie.getEquipment().setHelmet(player.getEquipment().getHelmet());
        zombie.getEquipment().setChestplate(player.getEquipment().getChestplate());
        zombie.getEquipment().setLeggings(player.getEquipment().getLeggings());
        zombie.getEquipment().setBoots(player.getEquipment().getBoots());
        zombie.getEquipment().setItemInMainHand(player.getEquipment().getItemInMainHand());
        zombie.getEquipment().setItemInMainHandDropChance(0);
        zombie.getEquipment().setHelmetDropChance(0);
        zombie.getEquipment().setChestplateDropChance(0);
        zombie.getEquipment().setLeggingsDropChance(0);
        zombie.getEquipment().setBootsDropChance(0);
        zombie.setAI(false);
        zombie.setHealth(player.getHealth());
        zombie.setSilent(true);
        zombie.setCanPickupItems(false);
        zombie.setGravity(false);
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
        this.zombieUUID = zombie.getUniqueId();
        this.uuid = player.getUniqueId();
        this.inventory = player.getInventory().getStorageContents();
        this.totalExperience = player.getTotalExperience();
    }


    public UUID getUUID() {
        return uuid;
    }

    public UUID getZombieUUID() {
        return zombieUUID;
    }

    public void KillZombie() {
        Zombie zombie = (Zombie) Bukkit.getEntity(zombieUUID);
        if (zombie != null) {
            zombie.remove();
        }
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public int getTotalExperience() {
        return totalExperience;
    }
}