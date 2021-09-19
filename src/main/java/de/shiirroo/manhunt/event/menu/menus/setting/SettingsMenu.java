package de.shiirroo.manhunt.event.menu.menus.setting;

import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.menu.*;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePreset;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePresetMenu;
import de.shiirroo.manhunt.utilis.Utilis;
import net.kyori.adventure.text.Component;
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
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class SettingsMenu extends Menu {

    public static HashMap<UUID, Menu> ConfigMenu = new HashMap<>();
    public static HashMap<UUID, Menu> GamePreset = new HashMap<>();
    public static HashMap<UUID, Menu> PlayerConfigMenu = new HashMap<>();

    public SettingsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ChatColor.DARK_GRAY + "Setting Menu:";
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
        if(StartGame.gameRunning == null && Ready.ready != null && Objects.equals(e.getCurrentItem(), ConfigGame())) {
            ConfigMenu.put(p.getUniqueId(), MenuManager.openMenu(ConfigMenu.class, (Player) e.getWhoClicked(), null));
        } else if(Objects.equals(e.getCurrentItem(), CLOSE_ITEM)){
            p.closeInventory();
        } else if(StartGame.gameRunning == null && Ready.ready != null && Objects.equals(e.getCurrentItem(), GamePresets())){
            GamePreset.put(p.getUniqueId(), MenuManager.openMenu(GamePresetMenu.class, (Player) e.getWhoClicked(), null));
        } else if(StartGame.gameRunning == null && Ready.ready != null && Objects.equals(e.getCurrentItem(), PlayerSetting())){
            PlayerConfigMenu.put(p.getUniqueId(), MenuManager.openMenu(PlayerConfigMenu.class, (Player) e.getWhoClicked(), null));
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
        inventory.setItem(11, ConfigGame());
        inventory.setItem(13, PlayerSetting());
        inventory.setItem(15, GamePresets());
        inventory.setItem(31, CLOSE_ITEM);

        setFillerGlass(false);
    }

    private ItemStack ConfigGame(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.COMPARATOR);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text(ChatColor.GOLD + "Man" + ChatColor.RED + "Hunt"+ ChatColor.DARK_GRAY+ ChatColor.BOLD + " Configuration:"));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

    private ItemStack PlayerSetting(){
        ItemStack playHead = Utilis.getPlayHead();
        SkullMeta im = (SkullMeta) playHead.getItemMeta();
        im.displayName(Component.text(ChatColor.GREEN +""+ ChatColor.BOLD + "User Config"));
        im.setPlayerProfile(p.getPlayerProfile());
        playHead.setItemMeta(im);
        return playHead;
    }

    private ItemStack GamePresets(){
        ItemStack GroupMenuGUI =  new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta im = GroupMenuGUI.getItemMeta();
        im.displayName(Component.text(ChatColor.BLUE +"Game Preset"));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        GroupMenuGUI.setItemMeta(im);
        return GroupMenuGUI;
    }

}
