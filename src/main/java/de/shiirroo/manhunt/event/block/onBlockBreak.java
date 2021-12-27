package de.shiirroo.manhunt.event.block;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.menu.menus.setting.gamemode.modes.RandomBlocks;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class onBlockBreak implements Listener{

    @EventHandler(priority = EventPriority.HIGH)
    private void BlockBreak(BlockBreakEvent event) {
        if ((Events.cancelEvent(event.getPlayer()))) {
            event.setCancelled(true);
        } else if((boolean) ManHuntPlugin.getGameData().getGameMode().getRandomBlocks().getValue()){
            RandomBlocks randomBlocks = (RandomBlocks) ManHuntPlugin.getGameData().getGameMode().getRandomBlocks();
            ItemStack randpart;
            try {
                randpart = new ItemStack(randomBlocks.getRandomBlocks().get(event.getBlock().getType()));
            } catch (Exception e) {
                randpart = new ItemStack(event.getBlock().getType());
            }
            if(!randpart.getType().equals(Material.AIR)) {
                try {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), randpart);
                event.setDropItems(false);
                } catch (IllegalArgumentException ignored) {

                }
            }
        }
    }
}
