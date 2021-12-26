package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.event.menu.menus.setting.gamemode.modes.RandomItems;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class onPrepareItemCraftEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void PrepareItemCraftEvent(PrepareItemCraftEvent event) {
        if((boolean) ManHuntPlugin.getGameData().getGameMode().getRandomItems().getValue()) {
            RandomItems randomItems = (RandomItems) ManHuntPlugin.getGameData().getGameMode().getRandomItems();
            if (event.getRecipe() != null) {
                Material material = randomItems.getRandomItems().get(event.getRecipe().getResult().getType());
                if (material != null) {
                    event.getInventory().setResult(new ItemStack(material));
                }
            }
        }
    }
}
