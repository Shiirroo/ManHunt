package de.shiirroo.manhunt.gamedata.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GamePause implements Serializable {

    private final List<Long> pauseList = new ArrayList<>();
    private final List<Long> unPauseList = new ArrayList<>();
    private boolean pause;

    public GamePause(GamePause gamePause) {
        pause = gamePause.isPause();
        pauseList.addAll(gamePause.getPauseList());
        unPauseList.addAll(gamePause.getUnPauseList());
    }

    public GamePause() {
        pause = false;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public List<Long> getPauseList() {
        return pauseList;
    }

    public List<Long> getUnPauseList() {
        return unPauseList;
    }


}
