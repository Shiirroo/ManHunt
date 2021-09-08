package de.shiirroo.manhunt.event.menu.menus;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Join;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.PlayerMenuUtility;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.Arrays;
import java.util.List;

public class SelectGroupMenu extends Menu {
    public SelectGroupMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ChatColor.DARK_GRAY + "Join group";
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.HOPPER;
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenuClickEvent(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        Player player = (Player) e.getWhoClicked();
        if(e.getCurrentItem().equals(setAssassinMeta())){
            if(Join.joinGroup(player, ManHuntRole.Assassin)){
                player.closeInventory();
            }
        } else if(e.getCurrentItem().equals(setHunterMeta())){
            if(Join.joinGroup(player,ManHuntRole.Hunter)){
                player.closeInventory();
            }

        }else if(e.getCurrentItem().equals(setSpeedrunnerMeta())){
            if(Join.joinGroup(player,ManHuntRole.Speedrunner)){
                player.closeInventory();
            }
        }else if(e.getCurrentItem().equals(setCancel())){
            if(Join.joinGroup(player,ManHuntRole.Unassigned)){
                player.closeInventory();
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
        if(ManHuntPlugin.getPlayerData() != null && ManHuntPlugin.getPlayerData().getPlayerRole(p).equals(ManHuntRole.Assassin)){
            inventory.setItem(0, setCancel());
        } else {
            inventory.setItem(0, setAssassinMeta());
        }

        if(ManHuntPlugin.getPlayerData() != null && ManHuntPlugin.getPlayerData().getPlayerRole(p).equals(ManHuntRole.Hunter)){
            inventory.setItem(2, setCancel());
        } else {
            inventory.setItem(2, setHunterMeta());
        }

        if(ManHuntPlugin.getPlayerData() != null && ManHuntPlugin.getPlayerData().getPlayerRole(p).equals(ManHuntRole.Speedrunner)){
            inventory.setItem(4, setCancel());
        } else {
            inventory.setItem(4, setSpeedrunnerMeta());
        }

        setFillerGlass();
    }

    private ItemStack setAssassinMeta(){
        List<DyeColor> colorAssassin = Arrays.asList(DyeColor.BLACK, DyeColor.BLUE, DyeColor.BLACK,DyeColor.BLACK,DyeColor.BLACK,DyeColor.BLACK);
        List<PatternType> patternTypeAssassin = Arrays.asList(PatternType.HALF_HORIZONTAL, PatternType.RHOMBUS_MIDDLE, PatternType.CURLY_BORDER,PatternType.STRIPE_BOTTOM,PatternType.CIRCLE_MIDDLE,PatternType.TRIANGLE_BOTTOM);
        return getItemStackBanner(ManHuntRole.Assassin.toString(), Material.BLUE_BANNER, colorAssassin, patternTypeAssassin, "#5555FF");
    }

    private ItemStack setHunterMeta(){
        List<DyeColor> colorHunter = Arrays.asList(DyeColor.RED, DyeColor.BLACK, DyeColor.RED,DyeColor.BLACK,DyeColor.BLACK,DyeColor.BLACK);
        List<PatternType> patternTypeHunter = Arrays.asList(PatternType.FLOWER, PatternType.HALF_HORIZONTAL, PatternType.STRIPE_CENTER,PatternType.CURLY_BORDER,PatternType.SQUARE_BOTTOM_LEFT,PatternType.SQUARE_BOTTOM_RIGHT);
        return getItemStackBanner(ManHuntRole.Hunter.toString(),Material.BLACK_BANNER, colorHunter, patternTypeHunter, "#FF5555");
    }


    private ItemStack setSpeedrunnerMeta(){
        List<DyeColor> colorSpeedrunner = Arrays.asList(DyeColor.MAGENTA,DyeColor.BLACK, DyeColor.BLACK, DyeColor.BLACK, DyeColor.BLACK, DyeColor.BLACK);
        List<PatternType> patternTypeHSpeedrunner = Arrays.asList(PatternType.TRIANGLE_BOTTOM, PatternType.TRIANGLE_TOP, PatternType.MOJANG, PatternType.FLOWER, PatternType.CREEPER, PatternType.CURLY_BORDER);
        return getItemStackBanner(ManHuntRole.Speedrunner.toString(),Material.PURPLE_BANNER, colorSpeedrunner, patternTypeHSpeedrunner, "#AA00AA");
    }

    private ItemStack setCancel(){
        List<DyeColor> colorSpeedrunner = Arrays.asList(DyeColor.RED);
        List<PatternType> patternTypeHSpeedrunner = Arrays.asList(PatternType.CROSS);
        return getItemStackBanner("Leave",Material.BLACK_BANNER, colorSpeedrunner, patternTypeHSpeedrunner, "#AA0000");
    }

    private ItemStack getItemStackBanner(String displayname, Material banner , List<DyeColor> dyeColors, List<PatternType> patternTypes, String hex){
        ItemStack is = new ItemStack(banner);
        BannerMeta bannerMeta = (BannerMeta) is.getItemMeta();
        if(dyeColors.size() == patternTypes.size()) {
            for (int i = 0; i != dyeColors.size(); i++)
                bannerMeta.addPattern(new Pattern(dyeColors.get(i), patternTypes.get(i)));
            bannerMeta.displayName(Component.text(displayname).color(TextColor.fromHexString(hex)));
            bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            is.setItemMeta(bannerMeta);
        }
        return is;
    }
}


