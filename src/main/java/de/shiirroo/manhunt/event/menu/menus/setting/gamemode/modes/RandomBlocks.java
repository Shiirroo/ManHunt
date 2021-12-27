package de.shiirroo.manhunt.event.menu.menus.setting.gamemode.modes;

import de.shiirroo.manhunt.event.menu.menus.setting.gamemode.CustomGameMode;
import de.shiirroo.manhunt.utilis.Utilis;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RandomBlocks extends CustomGameMode {

    public HashMap<Material, Material>  randomBlocks = new HashMap<>();
    public List<Material> mat = Arrays.stream(Material.values()).filter(Material::isBlock).collect(Collectors.toList());
    private final List<Material> airlist = new ArrayList<>();

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
            airlist.clear();
            for (Material material : mat) {
                if(!material.isAir())
                    createRandom(material);
                else
                    airlist.add(material);
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
        Material newMaterial = mat.get(Utilis.generateRandomInt(mat.size() - airlist.size()));
        while (randomBlocks.containsValue(newMaterial)){
            newMaterial = mat.get(Utilis.generateRandomInt(mat.size() - airlist.size()));
        }
        randomBlocks.put(material,newMaterial);
    }

    private ItemStack randomBlocksItem(){
        ItemStack itemStack = new ItemStack(Material.COBBLESTONE, 1);
        ItemMeta meta = itemStack.getItemMeta();
        String s = value.toString().substring(0, 1).toUpperCase() +  value.toString().substring(1).toLowerCase();
        meta.setDisplayName(ChatColor.DARK_GRAY + DisplayName() + ChatColor.GRAY + ": " + ((boolean) value? ChatColor.GREEN : ChatColor.RED) +  s);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setLore(new ArrayList<>( Arrays.asList("",ChatColor.GRAY + "All dropped blocks,", ChatColor.GRAY + "are created randomly.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
