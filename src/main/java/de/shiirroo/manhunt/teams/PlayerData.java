package de.shiirroo.manhunt.teams;

import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Instance of this class contains data per player, such as player's team, and if they are frozen or not
 */
public class PlayerData implements Serializable {

    private Map<UUID, ManHuntRole> players = new HashMap<>();;

    public PlayerData (){
    }

    public PlayerData(PlayerData playerData){
        players.putAll(playerData.getPlayersMap());
    }

    public Map<UUID, ManHuntRole> getPlayersMap(){
        return players;
    }

    /**
     * Get players with given role
     *
     * @param role role of players to be returned
     * @return list of players with given role
     */
    public List<UUID> getPlayersByRole(ManHuntRole role) {
        return players.entrySet().stream()
                .filter(e -> e.getValue() == role)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<UUID> getPlayersWithOutSpeedrunner(){
        return players.entrySet().stream()
                .filter(e -> e.getValue() != ManHuntRole.Speedrunner)
                .filter(e -> e.getValue() != ManHuntRole.Unassigned)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }



    public List<UUID> getPlayers() {
        return new ArrayList<>(players.keySet());
    }



    public ManHuntRole getPlayerRoleByUUID(UUID uuid) {
        return Optional.ofNullable(players.get(uuid))
                .orElse(null);
    }


    public void reset(Player player, TeamManager teamManager) {
        teamManager.removePlayer(getPlayerRoleByUUID(player.getUniqueId()), player);
        players.remove(player.getUniqueId());
    }


    public void setRole(Player player, ManHuntRole role, TeamManager teamManager) {
        if(players.get(player.getUniqueId()) != null){
            players.remove(player.getUniqueId());
        }
        players.putIfAbsent(player.getUniqueId(), role);
        teamManager.addPlayer(role,player);
    }

    public void setUpdateRole(Player player, TeamManager teamManager) {
        if(this.players.get(player.getUniqueId()) != null) {
            teamManager.updatePlayer(this.players.get(player.getUniqueId()), player);
        }
    }

    public void switchGameMode(Player player, GameMode gameMode, TeamManager teamManager) {
        teamManager.switchPlayer(this.players.get(player.getUniqueId()), player, gameMode);
    }


    public void updatePlayers(TeamManager teamManager){
        for(Player player : Bukkit.getOnlinePlayers()){
                this.setUpdateRole(player, teamManager);
        }
    }

    public void setPlayers(Map<UUID, ManHuntRole> players) {
        this.players = players;
    }

}
