package de.shiirroo.manhunt.event.menu;

import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;

public class PlayerMenuUtility {

    private final UUID uuid;
    private final HashMap<String, Object> dataMap = new HashMap<>();
    private final Stack<Menu> history = new Stack<>();

    public PlayerMenuUtility(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    /**
     * @param identifier A key to store the data by
     * @param data       The data itself to be stored
     */
    public void setData(String identifier, Object data) {
        this.dataMap.put(identifier, data);
    }

    /**
     * @param identifier The key for the data stored in the PMC
     * @return The retrieved value or null if not found
     */
    public Object getData(String identifier) {
        return this.dataMap.get(identifier);
    }


    /**
     * @return Get the previous menu that was opened for the player
     */
    public Menu lastMenu() {
        this.history.pop(); //Makes back() work for some reason
        return this.history.pop();
    }


    public void pushMenu(Menu menu) {
        if (history.size() >= 1 && menu.equals(history.get(history.size() - 1)))
            return;
        this.history.push(menu);
    }
}

