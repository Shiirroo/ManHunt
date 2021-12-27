package de.shiirroo.manhunt.utilis.repeatingtask;


import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.bossbar.BossBarUtilis;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Utilis;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.world.PlayerWorld;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class CompassTracker implements Runnable{


    @Override
    public void run() {
        if (ManHuntPlugin.getGameData().getGameStatus().isGame() && !ManHuntPlugin.getGameData().getGamePause().isPause()) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                setBossBarCompass(player);

                if (Config.getFreezeAssassin() && ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(player.getUniqueId()).equals(ManHuntRole.Speedrunner)) {
                    Player target = (Player) Utilis.getTarget(player);
                    if (target != null){
                        if(target.getGameMode() == GameMode.SURVIVAL && !target.isFlying()
                            && ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(target.getUniqueId()).equals(ManHuntRole.Assassin) && target.getVehicle() == null) {
                            ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().put(player.getUniqueId(), target.getUniqueId());
                        }
                        if(ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().get(player.getUniqueId()) != null){
                            target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Frozen " + ChatColor.GRAY + "by " + ChatColor.GOLD + player.getDisplayName()));
                            Utilis.drawLine(player.getEyeLocation(), target.getEyeLocation(), 1);
                            ManHuntPlugin.getGameData().getGamePlayer().getPlayerFrozenTime().put(target.getUniqueId(), (Calendar.getInstance().getTime().getTime() + 3500));
                        }
                    } else if(ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().get(player.getUniqueId()) != null){
                        ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().remove(player.getUniqueId());
                    }
                }

                ManHuntRole mht = ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(player.getUniqueId());
                if (mht != null && !mht.equals(ManHuntRole.Speedrunner)) {
                    if ((Config.getCompassParticleToSpeedrunner()) )
                        updateParticle(player);
                    if (Config.getCompassTracking() && Config.getCompassAutoUpdate())
                        updateCompass(player);
                }
            }
        for(OfflinePlayer player : ManHuntPlugin.getPlugin().getServer().getOfflinePlayers()){
            if (Config.getFreezeAssassin() && ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(player.getUniqueId()) != null  && ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(player.getUniqueId()).equals(ManHuntRole.Speedrunner) && ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().get(player.getUniqueId()) != null && !player.isOnline()) {
                Player p = Bukkit.getPlayer(ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().get(player.getUniqueId()));
                if(p != null) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Frozen " + ChatColor.GRAY + "by" + ChatColor.DARK_GREEN + " Zombie " + ChatColor.GOLD + (ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(player.getUniqueId()).getChatColor() + player.getName())));
                    }
                }
             }
        }
    }

    private static void setBossBarCompass(Player player){
        if (Config.getBossbarCompass() && ManHuntPlugin.debug) {
            if (!BossBarCoordinates.hasCoordinatesBossbar(player))
                BossBarCoordinates.addPlayerCoordinatesBossbar(player);
            if (BossBarCoordinates.hasCoordinatesBossbar(player) && !Objects.requireNonNull(BossBarCoordinates.getCoordinatesBossbarTitle(player)).equalsIgnoreCase(BossBarUtilis.setBossBarLoc(player)))
                BossBarCoordinates.editPlayerCoordinatesBossbar(player, BossBarUtilis.setBossBarLoc(player));
        } else if (BossBarCoordinates.hasCoordinatesBossbar(player)) {
            BossBarCoordinates.deletePlayerCoordinatesBossbar(player);
        }
    }

    private static void updateParticle(Player player){
        Map.Entry<UUID, PlayerWorld> findPlayer =  getClosedPlayerLocation(player);
        if(findPlayer == null) return;
        PlayerWorld playerWorld = findPlayer.getValue();
        Location location = playerWorld.getPlayerLocationInWold(player.getWorld());
        if(location == null) return;
        PlayerInventory inventory = player.getInventory();

        if ((inventory.getItemInMainHand().getType() == Material.COMPASS || inventory.getItemInOffHand().getType() == Material.COMPASS) && Config.getCompassParticleToSpeedrunner())  {
                Utilis.drawDirection(player.getLocation(), location, 3);
        }
    }

    public static void updateCompass(Player player) {
        if(ManHuntPlugin.getGameData().getGameStatus().isGame()) {
            Map.Entry<UUID, PlayerWorld> findPlayer =  getClosedPlayerLocation(player);
            Location cp = player.getCompassTarget();

            if (findPlayer == null) {
                float angle = (float) (Math.random() * Math.PI * 2);
                float dx = (float) (Math.cos(angle) * 5);
                float dz = (float) (Math.sin(angle) * 5);

                for (ItemStack it : player.getInventory())
                    if (it != null)
                        if (it.getType().equals(Material.COMPASS))
                            it.setItemMeta(getCompassMeta(it, player.getLocation().add(new Vector(dx, 0, dz)), ChatColor.RED + "Players have disappeared"));
                if ((Config.getCompassTracking() && player.getGameMode().equals(GameMode.SURVIVAL)) && ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().entrySet().stream().noneMatch(uuiduuidEntry -> uuiduuidEntry.getValue().equals(player.getUniqueId())) && !ManHuntPlugin.getGameData().getGamePlayer().getPlayerShowGameTimer().contains(player.getUniqueId()))
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Players have disappeared"));

            } else {
                Player newPlayer = Bukkit.getPlayer(findPlayer.getValue().getPlayerUUID());
                if(newPlayer != null) {
                    PlayerWorld playerWorld = findPlayer.getValue();
                    Location location = playerWorld.getPlayerLocationInWold(player.getWorld());
                    if (location == null) return;

                    if ((Config.getCompassTracking() && newPlayer.getGameMode().equals(GameMode.SURVIVAL)) && ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().entrySet().stream().noneMatch(uuiduuidEntry -> uuiduuidEntry.getValue().equals(player.getUniqueId())) && !ManHuntPlugin.getGameData().getGamePlayer().getPlayerShowGameTimer().contains(player.getUniqueId())) {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Following : " + ChatColor.DARK_PURPLE + newPlayer.getName()));
                    }
                    if(cp != null) {
                        if (cp.getWorld() == location.getWorld() && !(cp.getBlockX() == location.getBlockX() && cp.getBlockY() == location.getBlockY() && cp.getBlockZ() == location.getBlockZ()) && Config.getCompassTracking()) {
                            for (ItemStack it : player.getInventory()) {
                                if (it != null) {
                                    if (it.getType().equals(Material.COMPASS)) {
                                        it.setItemMeta(getCompassMeta(it, location, ChatColor.GOLD + "Following : " + ChatColor.DARK_PURPLE + newPlayer.getName()));
                                    }
                                }
                            }
                            newPlayer.updateInventory();
                        }
                        newPlayer.setCompassTarget(location);

                        }
                    }
                }
            }
        }


    public static void setPlayerlast(Player p){
        if(ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(p.getUniqueId()).equals(ManHuntRole.Speedrunner)) {
            if (ManHuntPlugin.getGameData().getGamePlayer().getPlayerWorldMap().get(p.getUniqueId()) == null) {
                ManHuntPlugin.getGameData().getGamePlayer().getPlayerWorldMap().put(p.getUniqueId(), new PlayerWorld(p.getWorld(), p));
            } else {
                PlayerWorld playerWorld = ManHuntPlugin.getGameData().getGamePlayer().getPlayerWorldMap().get(p.getUniqueId());
                playerWorld.setWorldLocationHashMap(p.getWorld(), p.getLocation());
                ManHuntPlugin.getGameData().getGamePlayer().getPlayerWorldMap().put(p.getUniqueId(), playerWorld);
            }
        }
    }

    public static Map.Entry<UUID, PlayerWorld>  getClosedPlayerLocation(Player p){
        return ManHuntPlugin.getGameData().getGamePlayer().getPlayerWorldMap().entrySet().stream()
                .filter(entry -> !entry.getKey().equals(p.getUniqueId()))
                .filter(entry ->{
                    Player player =  Bukkit.getPlayer(entry.getValue().getPlayerUUID());
                        if(player != null) return player.getGameMode().equals(GameMode.SURVIVAL);
                        return false;})
                .filter(entry -> {
                    if(Objects.requireNonNull(Bukkit.getPlayer(entry.getValue().getPlayerUUID())).isOnline()) {return ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(entry.getKey()) == ManHuntRole.Speedrunner; }
                    else return Config.getSpawnPlayerLeaveZombie() && ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(entry.getKey()) == ManHuntRole.Speedrunner;
                })
                .min(Comparator.comparing(entry -> entry.getValue().getPlayerLocationInWold(p.getWorld()).distance(p.getLocation())))
                .orElse(null);
    }



    public static CompassMeta getCompassMeta(ItemStack compass, Location loc, String name){
        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        meta.setDisplayName(name);
        meta.setLodestone(loc);
        meta.setLodestoneTracked(false);
        return meta;
    }

}
