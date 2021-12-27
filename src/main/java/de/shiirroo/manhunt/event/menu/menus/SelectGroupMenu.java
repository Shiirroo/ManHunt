package de.shiirroo.manhunt.event.menu.menus;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Join;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.PlayerMenuUtility;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        Player player = (Player) e.getWhoClicked();
        if(Objects.equals(e.getCurrentItem(), setAssassinMeta())){
            if(Join.joinGroup(player, ManHuntRole.Assassin)){
                player.closeInventory();
            }
        } else if(Objects.equals(e.getCurrentItem(), setHunterMeta())){
            if(Join.joinGroup(player,ManHuntRole.Hunter)){
                player.closeInventory();
            }

        }else if(Objects.equals(e.getCurrentItem(), setSpeedrunnerMeta())){
            if(Join.joinGroup(player,ManHuntRole.Speedrunner)){
                player.closeInventory();
            }
        }else if(Objects.equals(e.getCurrentItem(), setCancel())){
            if(Join.joinGroup(player,ManHuntRole.Unassigned)){
                player.closeInventory();
            }
        }
        if(Objects.equals(e.getCurrentItem(), CLOSE_ITEM)) {
            player.closeInventory();
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
        if(ManHuntPlugin.getGameData().getPlayerData() != null && ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(uuid).equals(ManHuntRole.Assassin)){
            inventory.setItem(11, setCancel());
        } else {
            inventory.setItem(11, setAssassinMeta());
        }

        if(ManHuntPlugin.getGameData().getPlayerData() != null && ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(uuid).equals(ManHuntRole.Hunter)){
            inventory.setItem(13, setCancel());
        } else {
            inventory.setItem(13, setHunterMeta());
        }

        if(ManHuntPlugin.getGameData().getPlayerData() != null && ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(uuid).equals(ManHuntRole.Speedrunner)){
            inventory.setItem(15, setCancel());
        } else {
            inventory.setItem(15, setSpeedrunnerMeta());
        }
        inventory.setItem(31, CLOSE_ITEM);
        setFillerGlass(false);
    }

    private ItemStack setAssassinMeta(){
        List<DyeColor> colorAssassin = Arrays.asList(DyeColor.BLACK, DyeColor.BLUE, DyeColor.BLACK,DyeColor.BLACK,DyeColor.BLACK,DyeColor.BLACK);
        List<PatternType> patternTypeAssassin = Arrays.asList(PatternType.HALF_HORIZONTAL, PatternType.RHOMBUS_MIDDLE, PatternType.CURLY_BORDER,PatternType.STRIPE_BOTTOM,PatternType.CIRCLE_MIDDLE,PatternType.TRIANGLE_BOTTOM);
        return getItemStackBanner(ManHuntRole.Assassin.toString(), Material.BLUE_BANNER, colorAssassin, patternTypeAssassin, ChatColor.BLUE);
    }

    private ItemStack setHunterMeta(){
        List<DyeColor> colorHunter = Arrays.asList(DyeColor.RED, DyeColor.BLACK, DyeColor.RED,DyeColor.BLACK,DyeColor.BLACK,DyeColor.BLACK);
        List<PatternType> patternTypeHunter = Arrays.asList(PatternType.FLOWER, PatternType.HALF_HORIZONTAL, PatternType.STRIPE_CENTER,PatternType.CURLY_BORDER,PatternType.SQUARE_BOTTOM_LEFT,PatternType.SQUARE_BOTTOM_RIGHT);
        return getItemStackBanner(ManHuntRole.Hunter.toString(),Material.BLACK_BANNER, colorHunter, patternTypeHunter, ChatColor.RED);
    }


    private ItemStack setSpeedrunnerMeta(){
        List<DyeColor> colorSpeedrunner = Arrays.asList(DyeColor.MAGENTA,DyeColor.BLACK, DyeColor.BLACK, DyeColor.BLACK, DyeColor.BLACK, DyeColor.BLACK);
        List<PatternType> patternTypeHSpeedrunner = Arrays.asList(PatternType.TRIANGLE_BOTTOM, PatternType.TRIANGLE_TOP, PatternType.MOJANG, PatternType.FLOWER, PatternType.CREEPER, PatternType.CURLY_BORDER);
        return getItemStackBanner(ManHuntRole.Speedrunner.toString(),Material.PURPLE_BANNER, colorSpeedrunner, patternTypeHSpeedrunner, ChatColor.DARK_PURPLE);
    }

    private ItemStack setCancel(){
        List<DyeColor> colorSpeedrunner = List.of(DyeColor.RED);
        List<PatternType> patternTypeHSpeedrunner = List.of(PatternType.CROSS);
        return getItemStackBanner("Leave",Material.BLACK_BANNER, colorSpeedrunner, patternTypeHSpeedrunner, ChatColor.DARK_RED);
    }

}


