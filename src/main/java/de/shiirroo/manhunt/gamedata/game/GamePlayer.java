package de.shiirroo.manhunt.gamedata.game;

import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.repeatingtask.ZombieSpawner;
import de.shiirroo.manhunt.world.PlayerWorld;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class GamePlayer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final HashSet<UUID> playerShowGameTimer = new HashSet<>();
    private final HashSet<UUID> teamchat = new HashSet<>();
    private final HashMap<UUID, ManHuntRole> playerOfflineRole = new HashMap<>();
    private final HashMap<UUID, ManHuntRole> livePlayerRole = new HashMap<>();
    private final HashMap<UUID, Long> playerExitGameAreaTimer = new HashMap<>();
    private final HashMap<UUID, PlayerWorld> playerWorldMap = new HashMap<>();
    private final HashMap<UUID, Long> playerBlockReadyTime = new HashMap<>();
    private final HashMap<UUID, Long> compassPlayerClickDelay = new HashMap<>();
    private final HashMap<String, UUID> playerIP = new HashMap<>();
    private final HashMap<UUID, Long> playerFrozenTime = new HashMap<>();
    private final HashMap<UUID, ZombieSpawner> zombieHashMap = new HashMap<>();
    public final HashMap<UUID, UUID> isFrozen= new HashMap<>();
    public List<UUID> players = new ArrayList<>();


    public GamePlayer(GamePlayer gamePlayer){
        playerShowGameTimer.addAll(gamePlayer.getPlayerShowGameTimer());
        teamchat.addAll(gamePlayer.getPlayerShowGameTimer());
        playerOfflineRole.putAll(gamePlayer.getPlayerOfflineRole());
        livePlayerRole.putAll(gamePlayer.getLivePlayerRole());
        playerExitGameAreaTimer.putAll(gamePlayer.getPlayerExitGameAreaTimer());
        playerWorldMap.putAll(gamePlayer.getPlayerWorldMap());
        playerBlockReadyTime.putAll(gamePlayer.getPlayerBlockReadyTime());
        compassPlayerClickDelay.putAll(gamePlayer.getCompassPlayerClickDelay());
        playerIP.putAll(gamePlayer.getPlayerIP());
        playerFrozenTime.putAll(gamePlayer.getPlayerFrozenTime());
        zombieHashMap.putAll(gamePlayer.getZombieHashMap());
        isFrozen.putAll(gamePlayer.getIsFrozen());
        players.addAll(gamePlayer.getPlayers());
    }


    public GamePlayer() {
    }

    public void addPlayer(UUID uuid) {
        this.players.add(uuid);
    }

    public void removePlayer(UUID uuid) {
        this.players.remove(uuid);
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public HashMap<UUID, ZombieSpawner> getZombieHashMap() {
        return zombieHashMap;
    }

    public HashSet<UUID> getPlayerShowGameTimer() {
        return playerShowGameTimer;
    }

    public HashSet<UUID> getTeamchat() {
        return teamchat;
    }

    public HashMap<UUID, ManHuntRole> getPlayerOfflineRole() {
        return playerOfflineRole;
    }

    public HashMap<UUID, Long> getPlayerExitGameAreaTimer() {
        return playerExitGameAreaTimer;
    }

    public HashMap<UUID, PlayerWorld> getPlayerWorldMap() {
        return playerWorldMap;
    }

    public HashMap<UUID, Long> getPlayerBlockReadyTime() {
        return playerBlockReadyTime;
    }

    public HashMap<String, UUID> getPlayerIP() {
        return playerIP;
    }

    public HashMap<UUID, Long> getPlayerFrozenTime() {
        return playerFrozenTime;
    }

    public HashMap<UUID, Long> getCompassPlayerClickDelay() {
        return compassPlayerClickDelay;
    }

    public HashMap<UUID, ManHuntRole> getLivePlayerRole() {
        return livePlayerRole;
    }

    public HashMap<UUID, UUID> getIsFrozen() {
        return isFrozen;
    }
}
