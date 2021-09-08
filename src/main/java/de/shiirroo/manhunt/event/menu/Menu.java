package de.shiirroo.manhunt.event.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/*
    Defines the behavior and attributes of all menus in our plugin
 */
public abstract class Menu implements InventoryHolder {

    //Protected values that can be accessed in the menus
    protected PlayerMenuUtility playerMenuUtility;
    protected Player p;
    protected Inventory inventory;
    protected PlayerInventory inventoryPlayer;
    protected ItemStack FILLER_GLASS = makeItem(Material.GRAY_STAINED_GLASS_PANE, " ");
    protected InventoryType inventoryType;
    protected String name;

    //Constructor for Menu. Pass in a PlayerMenuUtility so that
    // we have information on who's menu this is and
    // what info is to be transferred
    public Menu(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
        this.p = playerMenuUtility.getOwner();
    }

    //let each menu decide their name
    public abstract String getMenuName();

    //let each menu decide their slot amount
    public abstract InventoryType getInventoryType();

    public abstract int getSlots();

    public abstract boolean cancelAllClicks();

    //let each menu decide how the items in the menu will be handled when clicked
    public abstract void handleMenuClickEvent(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void handlePlayerDropItemEvent(PlayerDropItemEvent e) throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void handlePlayerInteractEvent(PlayerInteractEvent e) throws MenuManagerNotSetupException, MenuManagerException;

    //let each menu decide what items are to be placed in the inventory menu
    public abstract void setMenuItems();

    //When called, an inventory is created and opened for the player
    public void open(String name) {
        this.name = name;
        //The owner of the inventory created is the Menu itself,
        // so we are able to reverse engineer the Menu object from the
        // inventoryHolder in the MenuListener class when handling clicks

        if((getInventoryType() == null || getInventoryType().equals(InventoryType.CHEST)) && getSlots() != 0) {
            inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        } else if(getInventoryType().equals(InventoryType.PLAYER)){
            inventory = playerMenuUtility.getOwner().getInventory();
            playerMenuUtility.setData(this.playerMenuUtility.getOwner().getUniqueId().toString(), this);

        } else {
            inventory = Bukkit.createInventory(this, getInventoryType(), getMenuName());
        }

        //grab all the items specified to be used for this menu and add to inventory
        this.setMenuItems();

        //open the inventory for the player
       if(!getInventoryType().equals(InventoryType.PLAYER))
           playerMenuUtility.getOwner().openInventory(inventory);
        playerMenuUtility.pushMenu(this);
    }

    public void back() throws MenuManagerException, MenuManagerNotSetupException {
        MenuManager.openMenu(playerMenuUtility.lastMenu().getClass(), playerMenuUtility.getOwner(), null);
    }

    protected void reloadItems() {
        for (int i = 0; i < inventory.getSize(); i++){
            inventory.setItem(i, null);
        }
        setMenuItems();
    }

    protected void reload() throws MenuManagerException, MenuManagerNotSetupException {
        p.closeInventory();
        MenuManager.openMenu(this.getClass(), p, null);
    }

    //Overridden method from the InventoryHolder interface
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    /**
     * This will fill all of the empty slots with "filler glass"
     */
    //Helpful utility method to fill all remaining slots with "filler glass"
    public void setFillerGlass(){
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null){
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }

    /**
     * @param itemStack Placed into every empty slot when ran
     */
    public void setFillerGlass(ItemStack itemStack) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null){
                inventory.setItem(i, itemStack);
            }
        }
    }

    /**
     * @param material The material to base the ItemStack on
     * @param displayName The display name of the ItemStack
     * @return The constructed ItemStack object
     */
    public ItemStack makeItem(Material material, String displayName) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);

        //Automatically translate color codes provided
        item.setItemMeta(itemMeta);

        return item;
    }

}

