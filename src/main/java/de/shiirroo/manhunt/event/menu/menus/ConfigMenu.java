package de.shiirroo.manhunt.event.menu.menus;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.ConfigManHunt;
import de.shiirroo.manhunt.event.menu.*;
import de.shiirroo.manhunt.utilis.config.ConfigCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.UUID;

public class ConfigMenu extends Menu {
    public ConfigMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ChatColor.GOLD + "Man" + ChatColor.RED + "Hunt"+ ChatColor.DARK_GRAY +ChatColor.BOLD+" Configuration:";
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.CHEST;
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenuClickEvent(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if(p.isOp()) {
            for(ConfigCreator configCreator: ManHuntPlugin.getConfigCreatorsSett()){
                if(configCreator.getConfigSetting() instanceof Integer){
                    if (e.getCurrentItem().equals(Time(configCreator.getConfigName() + ": " +ChatColor.GREEN + configCreator.getConfigSetting()))) {
                        ConfigManHunt.AnvilGUISetup(p, configCreator);
                    }
                } else if(configCreator.getConfigSetting() instanceof Boolean){
                    if (e.getCurrentItem().equals(Yes(configCreator.getConfigName()))) {
                        configCreator.setConfigSetting(false);
                        break;
                    } else if (e.getCurrentItem().equals(NO(configCreator.getConfigName()))){
                        configCreator.setConfigSetting(true);
                        break;
                    }
                }
            }

            for(Menu menu : SettingsMenu.ConfigMenu.values()){
                menu.setMenuItems();
            }
        }
        if(e.getCurrentItem().equals(BACK_ITEM)){
            back();
        }
    }


    @Override
    public void handlePlayerDropItemEvent(PlayerDropItemEvent e) throws MenuManagerNotSetupException, MenuManagerException {

    }

    @Override
    public void handlePlayerInteractEvent(PlayerInteractEvent e) throws MenuManagerNotSetupException, MenuManagerException {

    }

    @Override
    public void setMenuItems() {
        Integer run = 0;
        Integer runInt = 18;

        for(ConfigCreator configCreator: ManHuntPlugin.getConfigCreatorsSett()){
            if(configCreator.getConfigSetting() instanceof Integer){
                inventory.setItem(runInt,Time(configCreator.getConfigName() + ": " +ChatColor.GREEN + configCreator.getConfigSetting()));
                runInt = runInt + 2;
                run--;
            } else if(configCreator.getConfigSetting() instanceof Boolean){
                checkConfig(run, (Boolean) configCreator.getConfigSetting(), configCreator.getConfigName());
            }
            run++;
        }
        inventory.setItem(31, BACK_ITEM);

        setFillerGlass(false);
    }

    private void checkConfig(Integer Slot,Boolean b, String name){
        if(b){
            if (!name.equalsIgnoreCase("BossbarCompass"))
                inventory.setItem(Slot,Yes(name));
            else
                inventory.setItem(Slot,Yes("BossbarCompass" + ChatColor.RED + " BETA "));
        } else{
            if (!name.equalsIgnoreCase("BossbarCompass"))
                inventory.setItem(Slot,NO(name));
            else
                inventory.setItem(Slot,NO("BossbarCompass" + ChatColor.RED + " BETA "));
        }
    }

    private ItemStack Yes(String configname) {
        ItemStack GroupMenuGUI = new ItemStack(Material.GREEN_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("§l" + configname).color(TextColor.fromHexString("#55FF55")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack NO(String configname) {
        ItemStack GroupMenuGUI = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("§l" + configname).color(TextColor.fromHexString("#FF5555")));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack Time(String configname) {
        ItemStack GroupMenuGUI = new ItemStack(Material.CLOCK);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text("§l" + ChatColor.GOLD + configname));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }
}
