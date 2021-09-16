package de.shiirroo.manhunt.world;

import de.shiirroo.manhunt.utilis.ZombieSpawner;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerWorld {

    private HashMap<World, Location> worldLocationHashMap = new HashMap<>();
    private ZombieSpawner zombieSpawner;

    public PlayerWorld(World world, Location location){
        this.worldLocationHashMap.put(world, location);
    }


    public void setWorldLocationHashMap(World world, Location location) {
        this.worldLocationHashMap.put(world, location);
    }

    public Location getPlayerLocationInWold(World world){
        return this.worldLocationHashMap.get(world);
    }

    public void SpawnZombie(Player player) {
        this.zombieSpawner = new ZombieSpawner(player);
    }

    public ZombieSpawner getZombieSpawner() {
        return zombieSpawner;
    }

    public void killZombieSpawner() {
        this.zombieSpawner.KillZombie();
        this.zombieSpawner = null;
    }
}
