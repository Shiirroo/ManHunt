package de.shiirroo.manhunt.event.menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Stack;

public class PlayerMenuUtility {

    private final Player owner;
    private final HashMap<String, Object> dataMap = new HashMap<>();
    private final Stack<Menu> history = new Stack<>();

    public PlayerMenuUtility(Player p) {
        this.owner = p;
    }

    public Player getOwner() {
        return owner;
    }

    /**
     * @param identifier A key to store the data by
     * @param data The data itself to be stored
     */
    public void setData(String identifier, Object data){
        this.dataMap.put(identifier, data);
    }
    /**
     * @param identifier The key for the data stored in the PMC
     * @return The retrieved value or null if not found
     */
    public Object getData(String identifier){
        return this.dataMap.get(identifier);
    }


    /**
     * @return Get the previous menu that was opened for the player
     */
    public Menu lastMenu(){
        this.history.pop(); //Makes back() work for some reason
        return this.history.pop();
    }

    public void pushMenu(Menu menu){
        if(history.size() >= 1 &&  menu.equals(history.get(history.size() -1)))
                return;
        this.history.push(menu);
    }
}

