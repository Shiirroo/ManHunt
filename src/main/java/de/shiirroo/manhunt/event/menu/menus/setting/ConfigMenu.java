package de.shiirroo.manhunt.event.menu.menus.setting;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.ConfigManHunt;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.PlayerMenuUtility;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePresetMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.presets.Custom;
import de.shiirroo.manhunt.utilis.config.ConfigCreator;
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

import java.util.List;
import java.util.Objects;

public class ConfigMenu extends Menu {
    public ConfigMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ChatColor.GOLD + "Man" + ChatColor.RED + "Hunt" + ChatColor.DARK_GRAY + ChatColor.BOLD + " Configuration:";
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
        Player p = (Player) e.getWhoClicked();
        if (p.isOp()) {
            for (ConfigCreator configCreator : ManHuntPlugin.getGameData().getGameConfig().getConfigCreatorsSett()) {
                if (!configCreator.getConfigName().equalsIgnoreCase("BossbarCompass")) {
                    if (configCreator.getConfigSetting() instanceof Integer) {
                        if (Objects.equals(e.getCurrentItem(), Time(configCreator))) {
                            ConfigManHunt.AnvilGUISetup(p, configCreator);
                        }
                    } else if (configCreator.getConfigSetting() instanceof Boolean) {
                        if (Objects.equals(e.getCurrentItem(), Yes(configCreator))) {
                            ConfigManHunt.resetPreset(p);
                            if (configCreator.getConfigName().equalsIgnoreCase("ShowAdvancement"))
                                ConfigManHunt.ShowAdvancement(false);
                            if (GamePresetMenu.preset.presetName().equalsIgnoreCase(new Custom().presetName()) && GamePresetMenu.customHashMap != null) {
                                GamePresetMenu.customHashMap.put(configCreator.getConfigName(), false);
                            }
                            configCreator.setConfigSetting(false, ManHuntPlugin.getPlugin());
                            break;
                        } else if (Objects.equals(e.getCurrentItem(), NO(configCreator))) {
                            ConfigManHunt.resetPreset(p);
                            if (configCreator.getConfigName().equalsIgnoreCase("ShowAdvancement"))
                                ConfigManHunt.ShowAdvancement(true);
                            if (GamePresetMenu.preset.presetName().equalsIgnoreCase(new Custom().presetName()) && GamePresetMenu.customHashMap != null) {
                                GamePresetMenu.customHashMap.put(configCreator.getConfigName(), true);
                            }
                            configCreator.setConfigSetting(true, ManHuntPlugin.getPlugin());
                            break;
                        }
                    }
                }
            }
            for (Menu menu : SettingsMenu.ConfigMenu.values()) {
                menu.setMenuItems();
            }
        }
        if (Objects.equals(e.getCurrentItem(), BACK_ITEM)) {
            back();
        }
    }


    @Override
    public void handlePlayerDropItemEvent(PlayerDropItemEvent e) {

    }

    @Override
    public void handlePlayerInteractEvent(PlayerInteractEvent e) {

    }

    @Override
    public void setMenuItems() {
        int run = 0;
        int runInt = 19;

        for (ConfigCreator configCreator : ManHuntPlugin.getGameData().getGameConfig().getConfigCreatorsSett()) {
            if (configCreator.getConfigSetting() instanceof Integer) {
                inventory.setItem(runInt, Time(configCreator));
                if (runInt == 21) runInt++;
                runInt++;
                run--;
            } else if (configCreator.getConfigSetting() instanceof Boolean) {
                checkConfig(run, (Boolean) configCreator.getConfigSetting(), configCreator);
            }
            run++;
        }
        inventory.setItem(31, BACK_ITEM);

        setFillerGlass(false);
    }

    private void checkConfig(Integer Slot, Boolean b, ConfigCreator config) {
        if (b) {
            inventory.setItem(Slot, Yes(config));
        } else {
            inventory.setItem(Slot, NO(config));
        }
    }

    private ItemStack Yes(ConfigCreator config) {
        ItemStack GroupMenuGUI = new ItemStack(Material.GREEN_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "§l" + config.getConfigName());
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (config.getLore() != null) {
            im.setLore(config.getLore());
        }
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack NO(ConfigCreator config) {
        ItemStack GroupMenuGUI = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.setDisplayName(ChatColor.RED + "§l" + config.getConfigName());
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (config.getLore() != null) {
            im.setLore(config.getLore());
        }
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack Time(ConfigCreator config) {
        ItemStack GroupMenuGUI = new ItemStack(Material.CLOCK);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        if (config.getLore() != null) {
            List<String> lore = findConfigValuePlaceHolder(config.getLore(), config.getConfigSetting());
            if (lore == config.getLore()) {
                im.setDisplayName("§l" + ChatColor.GOLD + config.getConfigName() + ": " + ChatColor.GREEN + config.getConfigSetting());
            } else {
                im.setDisplayName("§l" + ChatColor.GOLD + config.getConfigName());
            }
            im.setLore(lore);
        } else {
            im.setDisplayName("§l" + ChatColor.GOLD + config.getConfigName() + ": " + ChatColor.GREEN + config.getConfigSetting());
        }
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private List<String> findConfigValuePlaceHolder(List<String> list, Object configSetting) {
        for (int i = list.size() - 1; i != 0; i--) {
            if (list.get(i).contains("%value")) {
                list.set(i, list.get(i).replace("%value", "" + ChatColor.GREEN + configSetting));
            }
        }

        return list;
    }

}
