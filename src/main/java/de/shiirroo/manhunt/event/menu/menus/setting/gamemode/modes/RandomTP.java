package de.shiirroo.manhunt.event.menu.menus.setting.gamemode.modes;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.event.menu.menus.setting.gamemode.CustomGameMode;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Utilis;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class RandomTP extends CustomGameMode implements Serializable {
    @Override
    public ItemStack displayItem() {
        return randomTPItem();
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
        if(getValue().equals(maxValue())){
            value = minValue();
        } else {
            value = maxValue();
        }
    }

    @Override
    public void execute() {
        if((boolean) value){
            if(ManHuntPlugin.getGameData().getGameStatus().isStarting()){
                for(UUID speedrunnerUUID : ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner)) {
                        Player player = Bukkit.getPlayer(speedrunnerUUID);
                        if (player != null) {
                            if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
                                randomTP(player);
                            }
                        }

                }
            } else if(ManHuntPlugin.getGameData().getGameStatus().isGameRunning()){
                for(UUID hunterUUID : ManHuntPlugin.getGameData().getPlayerData().getPlayersWithOutSpeedrunner()){
                    Player hunter = Bukkit.getPlayer(hunterUUID);
                    if(hunter != null) {
                        if (!hunter.getGameMode().equals(GameMode.SPECTATOR)) {
                            randomTP(hunter);
                        }
                    }
                }
            }
        }
    }


    private void randomTP(Player player){
        Location location = player.getWorld().getSpawnLocation();

        double x = calcTeleportPos(location.getX());
        double z = calcTeleportPos(location.getZ());
        int y = location.getWorld().getHighestBlockAt((int) x, (int)z).getY() + 1;
        location.setX(x);
        location.setY(y);
        location.setZ(z);
        player.teleport(location);
    }

    private double calcTeleportPos(double blockPostion){
        double random = Utilis.generateRandomInt(500);

        if(Utilis.generateRandomInt(2) == 1){
            return (blockPostion - random);
        } else {
            return (blockPostion + random);
        }
    }



    private ItemStack randomTPItem(){
        ItemStack itemStack = new ItemStack(Material.COMPASS, 1);
        ItemMeta meta = itemStack.getItemMeta();
        String s = value.toString().substring(0, 1).toUpperCase() +  value.toString().substring(1).toLowerCase();
        meta.setDisplayName(ChatColor.DARK_GREEN + DisplayName() + ChatColor.GRAY + ": " + ((boolean) value? ChatColor.GREEN : ChatColor.RED) +  s);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setLore(new ArrayList<>( Arrays.asList("",ChatColor.GRAY + "At the start of the game,", ChatColor.GRAY + "all players will be teleported", ChatColor.GRAY + "randomly to a " + ChatColor.GREEN +"500x500" + ChatColor.GRAY + " range.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
