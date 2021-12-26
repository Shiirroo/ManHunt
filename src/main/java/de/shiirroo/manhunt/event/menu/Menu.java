package de.shiirroo.manhunt.event.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class Menu implements InventoryHolder{

    protected PlayerMenuUtility playerMenuUtility;
    protected UUID uuid;
    protected GameMode gameMode;
    protected Inventory inventory;
    protected boolean hasBack = false;

    protected ItemStack FILLER_GLASS = makeItem(Material.GRAY_STAINED_GLASS_PANE, " ");
    protected ItemStack CLOSE_ITEM = makeItem(Material.BARRIER, ChatColor.RED + "CLOSE");
    List<DyeColor> colorBACK = Arrays.asList(DyeColor.BLACK, DyeColor.BLACK, DyeColor.WHITE,DyeColor.WHITE, DyeColor.WHITE);
    List<PatternType> patternTypeBACK = Arrays.asList(PatternType.STRIPE_LEFT, PatternType.STRIPE_MIDDLE, PatternType.STRIPE_TOP,PatternType.STRIPE_BOTTOM, PatternType.CURLY_BORDER);
    protected ItemStack BACK_ITEM = getItemStackBanner(ChatColor.GREEN + "BACK", Material.WHITE_BANNER, colorBACK, patternTypeBACK, "#5555FF");
    protected String name;
    public Menu(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
        this.uuid = playerMenuUtility.getUuid();
        this.gameMode = Objects.requireNonNull(Bukkit.getPlayer(playerMenuUtility.getUuid())).getGameMode();
    }

    public abstract String getMenuName();

    public abstract InventoryType getInventoryType();

    public abstract int getSlots();

    public abstract boolean cancelAllClicks();

    public abstract void handleMenuClickEvent(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void handlePlayerDropItemEvent(PlayerDropItemEvent e) throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void handlePlayerInteractEvent(PlayerInteractEvent e) throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void setMenuItems();

    public Menu setBack(boolean b){
        this.hasBack = b;
        return this;
    }

    public Menu setName(String name){
        this.name = name;
        return this;
    }

    public Menu open() {
        Player player = Bukkit.getPlayer(playerMenuUtility.getUuid());
        if(player != null) {
            if ((getInventoryType() == null || getInventoryType().equals(InventoryType.CHEST)) && getSlots() != 0) {
                inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

            } else if (getInventoryType().equals(InventoryType.PLAYER)) {
                inventory = player.getInventory();
                playerMenuUtility.setData(player.getUniqueId().toString(), this);

            } else {
                inventory = Bukkit.createInventory(this, getInventoryType(), getMenuName());
            }

            this.setMenuItems();

            if (!getInventoryType().equals(InventoryType.PLAYER))
                player.openInventory(inventory);
            playerMenuUtility.pushMenu(this);
        }
        return this;
    }

    public void back() throws MenuManagerException, MenuManagerNotSetupException {
        Menu menu = MenuManager.getMenu(playerMenuUtility.lastMenu().getClass(), playerMenuUtility.getUuid());
        menu.setBack(hasBack).open();
    }

    protected void reloadItems() {
        for (int i = 0; i < inventory.getSize(); i++){
            inventory.setItem(i, null);
        }
        setMenuItems();
    }

    protected void reload() throws MenuManagerException, MenuManagerNotSetupException {
        Player player = Bukkit.getPlayer(playerMenuUtility.getUuid());
        if(player != null) {
            player.closeInventory();
            MenuManager.getMenu(this.getClass(), uuid).open();
        }
    }

    protected Player getPlayer(){
       return Bukkit.getPlayer(uuid);
    }


    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public GameMode getGameMode() {return gameMode;}

    public void setFillerGlass(Boolean showID){
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null){
                if(showID)
                    inventory.setItem(i, makeItem(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GOLD + (String.valueOf(i))));
                 else
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }

    protected ItemStack makeItem(Material material, String displayName) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);
        item.setItemMeta(itemMeta);

        return item;
    }

    protected ItemStack getItemStackBanner(String displayname, Material banner, List<DyeColor> dyeColors, List<PatternType> patternTypes, String hex){
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

    public ItemStack CommingSoon(){
        ItemStack itemStack = new ItemStack(Material.RED_TERRACOTTA, 1);
        ItemMeta im = itemStack.getItemMeta();
        im.displayName(Component.text(ChatColor.RED + "" +ChatColor.BOLD +"Comming Soon.."));
        itemStack.setItemMeta(im);
        return itemStack;
    }

    public void setGameMode(GameMode gameMode){
        this.gameMode = gameMode;
    }

}

