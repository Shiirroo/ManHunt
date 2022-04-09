package de.shiirroo.manhunt.gamedata.game;

import de.shiirroo.manhunt.event.menu.menus.setting.gamemode.CustomGameMode;
import de.shiirroo.manhunt.event.menu.menus.setting.gamemode.modes.*;

import java.io.Serial;
import java.io.Serializable;

public class GameMode implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final CustomGameMode worldBorderSize = new WorldBorderSize();
    private final CustomGameMode randomEffects = new RandomEffects();
    private final CustomGameMode randomTP = new RandomTP();
    private final CustomGameMode randomBlocks = new RandomBlocks();
    private final CustomGameMode randomItems = new RandomItems();

    public GameMode(GameMode gameMode) {
        randomEffects.value = gameMode.getRandomEffects().value;
        worldBorderSize.value = gameMode.getWorldBorderSize().value;
        randomTP.value = gameMode.getRandomTP().value;
        randomBlocks.value = gameMode.getRandomBlocks().value;
        ((RandomBlocks) randomBlocks).setRandomBlocks(((RandomBlocks) gameMode.randomBlocks).getRandomBlocks());
        randomItems.value = gameMode.getRandomItems().value;
        ((RandomItems) randomItems).setRandomItems(((RandomItems) gameMode.randomItems).getRandomItems());
    }

    public GameMode() {
    }

    public CustomGameMode getRandomBlocks() {
        return randomBlocks;
    }

    public CustomGameMode getRandomTP() {
        return randomTP;
    }

    public CustomGameMode getRandomEffects() {
        return randomEffects;
    }

    public CustomGameMode getWorldBorderSize() {
        return worldBorderSize;
    }

    public CustomGameMode getRandomItems() {
        return randomItems;
    }
}
