package de.shiirroo.manhunt.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class PlayerWorld implements Serializable {

    private final UUID uuid;
    private final HashMap<String, PlayerLocactionData> worldLocationHashMap = new HashMap<>();

    public PlayerWorld(World world, Player player){
        this.uuid = player.getUniqueId();
        this.worldLocationHashMap.put(world.getName(), new PlayerLocactionData(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
    }

    public void setWorldLocationHashMap(World world, Location location) {
        this.worldLocationHashMap.put(world.getName(), new PlayerLocactionData(location.getX(), location.getY(), location.getZ()));
    }

    public Location getPlayerLocationInWold(World world){
        PlayerLocactionData playerLocaction = this.worldLocationHashMap.get(world.getName());
        return new Location(world, playerLocaction.getX(),playerLocaction.getY(), playerLocaction.getZ());
    }

    public UUID getPlayerUUID()
    {
        return uuid;
    }

}




