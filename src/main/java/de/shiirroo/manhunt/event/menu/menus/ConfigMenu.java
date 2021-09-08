package de.shiirroo.manhunt.event.menu.menus;

import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.ConfigManHunt;
import de.shiirroo.manhunt.event.menu.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedHashMap;
import java.util.UUID;

public class ConfigMenu extends Menu {
    public ConfigMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Config this ManHunt world";
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.CHEST;
    }

    @Override
    public int getSlots() {
        return 18;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenuClickEvent(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if(p.isOp()) {
            Integer run = 0;
            LinkedHashMap<String, Boolean> c = ManHuntPlugin.getConfigSetting().getConfig();


            for (String entry : c.keySet()) {
                if (c.get(entry)) {
                    if (e.getCurrentItem().equals(Yes(entry))) {
                        c.put(entry, false);
                        Config.setConfig(c);
                        setMenuItems();
                        break;

                    }
                } else if (e.getCurrentItem().equals(NO(entry))) {

                    c.put(entry, true);
                    Config.setConfig(c);
                    setMenuItems();
                    break;
                }

                run++;
            }


            if (e.getCurrentItem().equals(Time(ChatColor.GOLD + "HuntStartTime: " + ChatColor.GREEN + ManHuntPlugin.getConfigSetting().huntStartTime()))) {
                ConfigManHunt.huntStartTimeGUI((Player) e.getWhoClicked());
            } else if (e.getCurrentItem().equals(Time(ChatColor.GOLD + "SpeedrunnerOpportunity: " + ChatColor.GREEN + ManHuntPlugin.getConfigSetting().getSpeedrunnerOpportunity()))) {
                ConfigManHunt.speedrunnerOpportunityGUI((Player) e.getWhoClicked());
            } else if (e.getCurrentItem().equals(Time(ChatColor.GOLD + "VoteStartTime: " + ChatColor.GREEN + ManHuntPlugin.getConfigSetting().getVoteStartTime()))) {
                ConfigManHunt.voteStartTime((Player) e.getWhoClicked());
            } else if (e.getCurrentItem().equals(Time(ChatColor.GOLD + "CompassTriggerTimer: " + ChatColor.GREEN + ManHuntPlugin.getConfigSetting().compassTriggerTimer()))) {
                ConfigManHunt.compassTriggerTimer((Player) e.getWhoClicked());
            } else if (e.getCurrentItem().equals(Time(ChatColor.GOLD + "GameResetTime: " + ChatColor.GREEN + ManHuntPlugin.getConfigSetting().getGameResetTime()))) {
                ConfigManHunt.gameResetTime((Player) e.getWhoClicked());
            }

            for(UUID uuid :PlayerMenu.ConfigMenu.keySet()){
                PlayerMenu.ConfigMenu.get(uuid).setMenuItems();
            }
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
        for(String entry : ManHuntPlugin.getConfigSetting().getConfig().keySet()){
            if(run==9) break;
            checkConfig(run,ManHuntPlugin.getConfigSetting().getConfig().get(entry), entry);
            run++;
        }
        inventory.setItem(9,Time(ChatColor.GOLD + "VoteStartTime: "+ChatColor.GREEN + ManHuntPlugin.getConfigSetting().getVoteStartTime()));
        inventory.setItem(11,Time(ChatColor.GOLD + "HuntStartTime: "+ChatColor.GREEN + ManHuntPlugin.getConfigSetting().huntStartTime()));
        inventory.setItem(13,Time(ChatColor.GOLD +"SpeedrunnerOpportunity: " +ChatColor.GREEN  + ManHuntPlugin.getConfigSetting().getSpeedrunnerOpportunity()));
        inventory.setItem(15,Time(ChatColor.GOLD + "CompassTriggerTimer: "+ChatColor.GREEN + ManHuntPlugin.getConfigSetting().compassTriggerTimer()));
        inventory.setItem(17,Time(ChatColor.GOLD + "GameResetTime: "+ChatColor.GREEN + ManHuntPlugin.getConfigSetting().getGameResetTime()));
        setFillerGlass();
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
        im.displayName(Component.text("§l" + configname));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }
}
