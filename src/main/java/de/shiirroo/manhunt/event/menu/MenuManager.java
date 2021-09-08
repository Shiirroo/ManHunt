package de.shiirroo.manhunt.event.menu;

import de.shiirroo.manhunt.teams.PlayerData;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Used to interface with the Menu Manager API
 */
public class MenuManager {

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static boolean isSetup = false;

    private static void registerMenuListener(Server server, Plugin plugin, PlayerData playerData) {

        boolean isAlreadyRegistered = false;
        for (RegisteredListener rl : InventoryClickEvent.getHandlerList().getRegisteredListeners()) {
            if (rl.getListener() instanceof MenuListener) {
                isAlreadyRegistered = true;
                break;
            }
        }

        if (!isAlreadyRegistered) {
            server.getPluginManager().registerEvents(new MenuListener(), plugin);
        }

    }


    /**
     * @param server The instance of your server. Provide by calling getServer()
     * @param plugin The instance of the plugin using this API. Can provide in plugin class by passing this keyword
     */
    public static void setup(Server server, Plugin plugin, PlayerData playerData) {


        registerMenuListener(server, plugin, playerData);
        isSetup = true;

    }

    /**
     * @param menuClass The class reference of the Menu you want to open for a player
     * @param player    The player to open the menu for
     * @throws MenuManagerNotSetupException Thrown if the setup() method has not been called and used properly
     * @return
     */
    public static Menu openMenu(Class<? extends Menu> menuClass, Player player,String name) throws MenuManagerException, MenuManagerNotSetupException {
        try {
            Menu OpenMenu = menuClass.getConstructor(PlayerMenuUtility.class).newInstance(getPlayerMenuUtility(player));
            OpenMenu.open(name);
            return OpenMenu;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new MenuManagerException();
        }
    }

    public static Menu openMenu(Class<? extends Menu> menuClass, Player player,String name, PlayerData playerData) throws MenuManagerException, MenuManagerNotSetupException {
        try {
            Menu OpenMenu = menuClass.getConstructor(PlayerMenuUtility.class).newInstance(getPlayerMenuUtility(player, playerData));
            OpenMenu.open(name);
            return OpenMenu;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new MenuManagerException();
        }
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p, PlayerData playerData) throws MenuManagerException, MenuManagerNotSetupException {

        if (!isSetup) {
            throw new MenuManagerNotSetupException();
        }

        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a pmu "saved" for them

            //Construct PMU
            playerMenuUtility = new PlayerMenuUtility(p, playerData);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }
    }
    public static PlayerMenuUtility getPlayerMenuUtility(Player p) throws MenuManagerException, MenuManagerNotSetupException {

        if (!isSetup) {
            throw new MenuManagerNotSetupException();
        }

        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a pmu "saved" for them

            //Construct PMU
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }
    }
}
