package de.shiirroo.manhunt.gamedata;


import de.shiirroo.manhunt.gamedata.game.*;
import de.shiirroo.manhunt.teams.PlayerData;
import org.bukkit.plugin.Plugin;

import java.io.Serial;
import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;


public class GameData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private GamePause gamePause;
    private GameStatus gameStatus;
    private GamePlayer gamePlayer;
    private final GameMode gameMode;
    private GameConfig gameConfig;
    private final PlayerData playerData;


    public GameData(Plugin plugin){
        this.id = UUID.randomUUID();
        gameConfig = new GameConfig(plugin);
        gamePause = new GamePause();
        gameStatus = new GameStatus();
        gamePlayer = new GamePlayer();
        playerData = new PlayerData();
        gameMode = new GameMode();
    }

    public GameData(GameData gameData){
        this.id = gameData.getId();
        this.gameConfig = new GameConfig(gameData.getGameConfig());
        this.gamePause = new GamePause(gameData.getGamePause());
        this.gamePlayer = new GamePlayer(gameData.getGamePlayer());
        this.gameStatus = new GameStatus(gameData.getGameStatus());
        this.gameMode =  new GameMode(gameData.getGameMode());
        this.playerData = new PlayerData(gameData.getPlayerData());
        if(!this.gamePause.isPause() && this.gameStatus.isGameRunning()){
            this.gamePause.getPauseList().add(Calendar.getInstance().getTime().getTime());
            this.gamePause.setPause(true);
        }
    }

    public void reset(){
        gamePause = new GamePause();
        gameStatus = new GameStatus();
        gamePlayer = new GamePlayer();
    }

    public void reloadGameConfig(Plugin plugin) {
        gameConfig = new GameConfig(plugin);
    }

    public UUID getId() {
        return id;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public GamePause getGamePause() {
        return gamePause;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }
}




