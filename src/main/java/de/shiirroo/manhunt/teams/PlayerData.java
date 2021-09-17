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

    private final Map<Player, PlayerDetails> players = new HashMap<>();

    /**
     * Get players with given role
     *
     * @param role role of players to be returned
     * @return list of players with given role
     */
    public List<Player> getPlayersByRole(ManHuntRole role) {
        return players.entrySet().stream()
                .filter(e -> e.getValue().role == role)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Player> getPlayersWithOutSpeedrunner(){
        return players.entrySet().stream()
                .filter(e -> e.getValue().role != ManHuntRole.Speedrunner)
                .filter(e -> e.getValue().role != ManHuntRole.Unassigned)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }



    public boolean isFrozen(Player player) {
        return Optional.ofNullable(players.get(player))
                .map(PlayerDetails::isFrozen)
                .orElse(false);
    }




    /**
     * get role of player (ManHunt/speedrunner)
     *
     * @param player player
     * @return role of given player, or null player is not assigned to any of the teams
     */

    public ManHuntRole getPlayerRole(Player player) {
        return Optional.ofNullable(players.get(player))
                .map(PlayerDetails::getRole)
                .orElse(null);
    }

    public ManHuntRole getPlayerRoleByUUID(UUID uuid) {
        return Optional.ofNullable(players.get(players.keySet().stream().filter(e -> e.getUniqueId().equals(uuid)).findFirst().get()))
                .map(PlayerDetails::getRole)
                .orElse(null);
    }



    /**
     * Remove player's data from memory
     *
     * @param player player to be removed
     */
    public void addUnassigned(Player player, TeamManager teamManager) {
        PlayerDetails details = players.getOrDefault(player, new PlayerDetails());
        details.setRole(ManHuntRole.Unassigned);
        players.putIfAbsent(player, details);
        teamManager.addPlayer(ManHuntRole.Unassigned,player);
    }

    public void reset(Player player, TeamManager teamManager) {
        teamManager.removePlayer(getPlayerRole(player), player);
        players.remove(player);
    }

    public void setFrozen(Player player, boolean frozen) {
        PlayerDetails details = players.getOrDefault(player, new PlayerDetails());
        details.setFrozen(frozen);
        players.putIfAbsent(player, details);
    }

    public void setRole(Player player, ManHuntRole role,TeamManager teamManager) {
        if(players.get(player) != null){
            players.remove(player);
        }
        PlayerDetails details = players.getOrDefault(player, new PlayerDetails());
        details.setRole(role);
        players.putIfAbsent(player, details);
        teamManager.addPlayer(role,player);
    }

    public void setUpdateRole(Player player,TeamManager teamManager) {
        teamManager.updatePlayer(this.players.get(player).getRole(), player);
    }

    public void switchGameMode(Player player,TeamManager teamManager, GameMode gameMode) {
        teamManager.switchPlayer(this.players.get(player).getRole(), player, gameMode);
    }


    public void updatePlayers(TeamManager teamManager){
        for(Player player : Bukkit.getOnlinePlayers()){
                this.setUpdateRole(player, teamManager);
        }
    }



    private static class PlayerDetails {
        private boolean isFrozen = false;
        private ManHuntRole role;

        public boolean isFrozen() {
            return isFrozen;
        }

        public void setFrozen(boolean frozen) {
            isFrozen = frozen;
        }

        public ManHuntRole getRole() {
            return role;
        }

        public void setRole(ManHuntRole role) {
            this.role = role;
        }
    }

}
