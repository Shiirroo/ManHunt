package de.shiirroo.manhunt.event.menu;

import de.shiirroo.manhunt.ManHuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Used to interface with the Menu Manager API
 */
public class MenuManager {

    private static final HashMap<UUID, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static boolean isSetup = false;

    private static void registerMenuListener(Server server, Plugin plugin) {

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
    public static void setup(Server server, Plugin plugin) {

        registerMenuListener(server, plugin);
        isSetup = true;

    }


    /**
     * @param menuClass The class reference of the Menu you want to open for a player
     * @param player    The player to open the menu for
     * @throws MenuManagerNotSetupException Thrown if the setup() method has not been called and used properly
     */

        public static Menu getMenu(Class<? extends Menu> menuClass, UUID uuid) throws MenuManagerException, MenuManagerNotSetupException {
        try {
            return menuClass.getConstructor(PlayerMenuUtility.class).newInstance(getPlayerMenuUtility(uuid));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.RED + "Something went wrong while getting menu");
            throw new MenuManagerException();
        }
    }

    public static PlayerMenuUtility getPlayerMenuUtility(UUID uuid) throws MenuManagerNotSetupException {

        if (!isSetup) {
            throw new MenuManagerNotSetupException();
        }

        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(uuid))) { //See if the player has a pmu "saved" for them

            //Construct PMU
            playerMenuUtility = new PlayerMenuUtility(uuid);
            playerMenuUtilityMap.put(uuid, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(uuid); //Return the object by using the provided player
        }
    }
}
