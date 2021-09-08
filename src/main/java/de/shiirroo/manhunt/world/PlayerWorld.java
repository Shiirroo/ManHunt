package de.shiirroo.manhunt.world;

import org.bukkit.Location;
import org.bukkit.World;
import java.util.HashMap;

public class PlayerWorld {

    private HashMap<World, Location> worldLocationHashMap = new HashMap<>();

    public PlayerWorld(World world, Location location){
        this.worldLocationHashMap.put(world, location);
    }


    public void setWorldLocationHashMap(World world, Location location) {
        this.worldLocationHashMap.put(world, location);
    }

    public Location getPlayerLocationInWold(World world){
        return this.worldLocationHashMap.get(world);

    }

}
