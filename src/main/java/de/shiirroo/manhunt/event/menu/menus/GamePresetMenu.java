package de.shiirroo.manhunt.event.menu.menus;

import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.PlayerMenuUtility;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GamePresetMenu extends Menu {

    public static int preset = 0;

    public GamePresetMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ChatColor.BLUE +"Game Preset";
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
            if (e.getWhoClicked().isOp() && StartGame.gameRunning == null) {
                if(e.getCurrentItem().getItemMeta().lore() != null && e.getCurrentItem().getItemMeta().lore().equals(DreamConfig().getItemMeta().lore()) && preset != 1){
                    p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 2.0f, 3.0f);
                    preset = 1;
                } else if(e.getCurrentItem().getItemMeta().lore() != null && e.getCurrentItem().equals(CustomConfig()) && preset != 0){
                    preset = 0;
                    p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 2.0f, 3.0f);
                }else if(e.getCurrentItem().equals(BACK_ITEM)) {
                    back();
                }
            }
            for(Menu menu : SettingsMenu.GamePreset.values()){
                menu.setMenuItems();
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
        inventory.setItem(11, DreamConfig());
        inventory.setItem(12, CommingSoon());
        inventory.setItem(13, CommingSoon());
        inventory.setItem(14, CommingSoon());
        inventory.setItem(15, CustomConfig());
        inventory.setItem(31, BACK_ITEM);

        setFillerGlass(true);
    }



    private ItemStack DreamConfig(){
        ItemStack playHead = getPlayHead();
        SkullMeta im = (SkullMeta) playHead.getItemMeta();
        im.displayName(Component.text(ChatColor.GREEN +""+ ChatColor.BOLD + ChatColor.UNDERLINE+ "DREAM"));
        im.setOwner("Dream");
        List<String> loreString = Arrays.asList("", ChatColor.GOLD +  "➤ "+ChatColor.GRAY +  "Play "+ ChatColor.GREEN + "Dream's"+ChatColor.GRAY +" classic ManHunt mode.",
                "",ChatColor.YELLOW +  "● " + ChatColor.GOLD + "1x " + ManHuntRole.Speedrunner.getChatColor() + ManHuntRole.Speedrunner,
                ChatColor.YELLOW +  "● " + ChatColor.GOLD + "1x " + ManHuntRole.Assassin.getChatColor() + ManHuntRole.Assassin,ChatColor.YELLOW
                        +  "● " + ChatColor.GOLD + "ထx " + ManHuntRole.Hunter.getChatColor() + ManHuntRole.Hunter,
                "",ChatColor.YELLOW +  "● " + ChatColor.GRAY + "Classic config","", preset == 1? ChatColor.GREEN +""+ ChatColor.BOLD+"⇨ Selected Preset": ChatColor.DARK_GRAY + "⇨ Select Preset");
        im.lore(lore(loreString));
        playHead.setItemMeta(im);
        return playHead;
    }


    private ItemStack CustomConfig(){
        ItemStack itemStack = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta im = itemStack.getItemMeta();
        im.displayName(Component.text(ChatColor.GRAY +""+ ChatColor.BOLD + ChatColor.UNDERLINE+ "Custom"));
        List<String> loreString = Arrays.asList("", ChatColor.GOLD +  "➤ "+ChatColor.GRAY +  "Play "+ ChatColor.GREEN + "Dream's"+ChatColor.GRAY +" classic ManHunt mode.",
                "",ChatColor.YELLOW +  "● " + ChatColor.GOLD + "min. 1 " + ManHuntRole.Speedrunner.getChatColor() + ManHuntRole.Speedrunner,
                ChatColor.YELLOW +  "● " + ChatColor.GOLD + "ထx " + ManHuntRole.Assassin.getChatColor() + ManHuntRole.Assassin,ChatColor.YELLOW
                        +  "● " + ChatColor.GOLD + "ထx " + ManHuntRole.Hunter.getChatColor() + ManHuntRole.Hunter,
                "",ChatColor.YELLOW +  "● " + ChatColor.GRAY + "Custom config","", preset == 0? ChatColor.GREEN +""+ ChatColor.BOLD+ "⇨ Selected Preset": ChatColor.DARK_GRAY + "⇨ Select Preset");
        im.lore(lore(loreString));
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(im);
        return itemStack;
    }


    private ItemStack CommingSoon(){
        ItemStack itemStack = new ItemStack(Material.RED_TERRACOTTA, 1);
        ItemMeta im = itemStack.getItemMeta();
        im.displayName(Component.text(ChatColor.RED + "" +ChatColor.BOLD +"Comming Soon.."));
        itemStack.setItemMeta(im);
        return itemStack;
    }

    private List<Component> lore(List<String> lore){
        List<Component> componentList = new ArrayList<>();
            for(String s : lore) componentList.add(Component.text(s));
        return componentList;
    };
}
