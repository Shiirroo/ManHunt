package de.shiirroo.manhunt.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerWorld {

    private Player player;
    private HashMap<World, Location> worldLocationHashMap = new HashMap<>();

    public PlayerWorld(World world, Player player){
        this.player = player;
        this.worldLocationHashMap.put(world, player.getLocation());
    }

    public void setWorldLocationHashMap(World world, Location location) {
        this.worldLocationHashMap.put(world, location);
    }

    public Location getPlayerLocationInWold(World world){
        return this.worldLocationHashMap.get(world);
    }

    public Player getPlayer() {
        return player;
    }

    public void updatePlayer(Player player){
        this.player = player;
    }
}
