package de.shiirroo.manhunt.gamedata.game;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.world.save.SaveGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.UUID;

public class GameStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean gameRunning;
    private boolean isStarting;
    private boolean isReadyForVote;
    private final HashSet<UUID> livePlayerList =  new HashSet<>();
    private final HashSet<UUID> startPlayerList = new HashSet<>();
    private long gameStartTime;
    private Integer elapsedTime;
    private long gameElapsedTime ;

    public GameStatus(GameStatus gameStatus) {
        gameRunning = gameStatus.isGameRunning();
        isStarting = gameStatus.isStarting();
        isReadyForVote = gameStatus.isReadyForVote();
        livePlayerList.addAll(gameStatus.getLivePlayerList());
        startPlayerList.addAll(gameStatus.getStartPlayerList());
        gameStartTime = gameStatus.getGameStartTime();
        elapsedTime = gameStatus.getElapsedTime();
        gameElapsedTime = gameStatus.getGameElapsedTime();
    }

    public GameStatus(){
        gameRunning = false;
        isStarting = false;
        isReadyForVote = true;
        gameStartTime = 0;
        elapsedTime = 1;
        gameElapsedTime = 0;
    }

    public boolean isGame(){ return  ((gameRunning || isStarting) && !isReadyForVote);}

    public boolean isGameRunning() {
        return gameRunning;
    }

    public boolean isStarting() {
        return isStarting;
    }
    public void setStarting(boolean starting) {
        isStarting = starting;
    }

    public boolean isReadyForVote() {
        return isReadyForVote;
    }

    public void setReadyForVote(boolean ready) {
        isReadyForVote = ready;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public HashSet<UUID> getLivePlayerList() {
        return livePlayerList;
    }

    public long getGameStartTime() {
        return gameStartTime;
    }

    public void setGameStartTime(long gameStartTime) {
        this.gameStartTime = gameStartTime;
    }


    public Integer getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Integer elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getGameElapsedTime() {
        return gameElapsedTime;
    }

    public void setGameElapsedTime(long gameElapsedTime) {
        this.gameElapsedTime = gameElapsedTime;
    }

    public HashSet<UUID> getStartPlayerList() {
        return startPlayerList;
    }

    public SaveGame getAutoSave(){
        try {
            return SaveGame.class.getDeclaredConstructor().newInstance().setSaveName("AutoSave").setSlot(0);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.RED + "Something went wrong while AutoSave.");
            return null;
        }
    }
}
