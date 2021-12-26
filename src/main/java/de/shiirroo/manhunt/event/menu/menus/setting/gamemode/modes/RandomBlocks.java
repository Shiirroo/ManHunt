package de.shiirroo.manhunt.event.menu.menus.setting.gamemode.modes;

import de.shiirroo.manhunt.event.menu.menus.setting.gamemode.CustomGameMode;
import de.shiirroo.manhunt.utilis.Utilis;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class RandomBlocks extends CustomGameMode {

    public HashMap<Material, Material>  randomBlocks = new HashMap<>();
    public List<Material> mat = Arrays.stream(Material.values()).filter(Material::isBlock).collect(Collectors.toList());

    @Override
    public ItemStack displayItem() {
        return randomBlocksItem();
    }

    @Override
    public Object defaultValue() {
        return minValue();
    }

    @Override
    protected Object minValue() {
        return false;
    }

    @Override
    protected Object maxValue() {
        return true;
    }

    @Override
    public void init(Player p) {
        if (value.equals(maxValue())) {
            value = minValue();
        } else {
            value = maxValue();
        }
    }

    @Override
    public void execute() {
        if((boolean) value) {
            randomBlocks.clear();
            for (Material material : mat) {
                if(!material.isAir())
                    createRandom(material);
            }
        }
    }

    public HashMap<Material, Material> getRandomBlocks() {
        return randomBlocks;
    }

    public void setRandomBlocks(HashMap<Material, Material> randomBlocks) {
        this.randomBlocks = randomBlocks;
    }

    public void createRandom(Material material){
        Material newMaterial = mat.get(Utilis.generateRandomInt(mat.size() -1));
        while (randomBlocks.containsValue(newMaterial)){
            newMaterial = mat.get(Utilis.generateRandomInt(mat.size() -1));
        }
        randomBlocks.put(material,newMaterial);
    }

    private ItemStack randomBlocksItem(){
        ItemStack itemStack = new ItemStack(Material.COBBLESTONE, 1);
        ItemMeta meta = itemStack.getItemMeta();
        String s = value.toString().substring(0, 1).toUpperCase() +  value.toString().substring(1).toLowerCase();
        meta.displayName(Component.text(ChatColor.DARK_GRAY + DisplayName() + ChatColor.GRAY + ": " + ((boolean) value? ChatColor.GREEN : ChatColor.RED) +  s));
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.lore(Utilis.lore(new ArrayList<>( Arrays.asList("",ChatColor.GRAY + "All dropped blocks,", ChatColor.GRAY + "are created randomly."))));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
