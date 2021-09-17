package de.shiirroo.manhunt.utilis.repeatingtask;


import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.bossbar.BossBarUtilis;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.TimerCommand;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Utilis;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.world.PlayerWorld;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.org.codehaus.plexus.util.xml.CompactXMLWriter;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class CompassTracker implements Runnable {

    public static HashMap<UUID, Player> isFrozen= new HashMap<>();

    @Override
    public void run() {
        if (StartGame.gameRunning != null && !VoteCommand.pause) {
            for (Player player : ManHuntPlugin.getPlugin().getServer().getOnlinePlayers()) {

                setBossBarCompass(player);

                if (Config.getFreezeAssassin() && ManHuntPlugin.getPlayerData().getPlayerRole(player).equals(ManHuntRole.Speedrunner)) {
                    Player target = (Player) Utilis.getTarget(player);
                    if (target != null){
                        if(target.getGameMode() == GameMode.SURVIVAL && !target.isFlying()
                            && ManHuntPlugin.getPlayerData().getPlayerRole(target).equals(ManHuntRole.Assassin) && target.getVehicle() == null && isFrozen.get(player.getUniqueId()) == null && !ManHuntPlugin.getPlayerData().isFrozen(target)) {
                            isFrozen.put(player.getUniqueId(), target);
                            ManHuntPlugin.getPlayerData().setFrozen(target, true);
                        }
                        if(isFrozen.get(player.getUniqueId()) != null && ManHuntPlugin.getPlayerData().isFrozen(target) ){
                            target.sendActionBar(Component.text(ChatColor.DARK_AQUA + "Frozen " + ChatColor.GRAY + "by " + ChatColor.GOLD).append(player.displayName()));
                            System.out.println("SET FROZEN" + target.getName() + " " + ManHuntPlugin.getPlayerData().isFrozen(target));
                            Utilis.drawLine(player.getEyeLocation(), target.getEyeLocation(), 1);
                            GameTimes.playerBossBar.put(target.getUniqueId(), (Calendar.getInstance().getTime().getTime() + 3500));
                        } else {
                            isFrozen.remove(player.getUniqueId());
                        }
                    } else if(target == null && isFrozen.get(player.getUniqueId()) != null && ManHuntPlugin.getPlayerData().isFrozen(isFrozen.get(player.getUniqueId()))){
                        ManHuntPlugin.getPlayerData().setFrozen(isFrozen.get(player.getUniqueId()), false);
                        isFrozen.remove(player.getUniqueId());
                    }
                }

                ManHuntRole mht = ManHuntPlugin.getPlayerData().getPlayerRole(player);
                if (mht != null && !mht.equals(ManHuntRole.Speedrunner)) {
                    if ((Config.getCompassParticleToSpeedrunner()) )
                        updateParticle(player);
                    if (Config.getCompassTracking() && Config.getCompassAutoUpdate() && !mht.equals(ManHuntRole.Speedrunner))
                        updateCompass(player);
                }
            }
        for(OfflinePlayer player : ManHuntPlugin.getPlugin().getServer().getOfflinePlayers()){
            if (Config.getFreezeAssassin() && Events.players.get(player.getUniqueId()) != null  && Events.players.get(player.getUniqueId()).equals(ManHuntRole.Speedrunner) && isFrozen.get(player.getUniqueId()) != null && !player.isOnline()) {
                isFrozen.get(player.getUniqueId()).sendActionBar(Component.text(ChatColor.DARK_AQUA + "Frozen " + ChatColor.GRAY + "by" +ChatColor.DARK_GREEN +   " Zombie " + ChatColor.GOLD + (Events.players.get(player.getUniqueId()).getChatColor() + player.getName())));
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
        if(StartGame.gameRunning != null && !StartGame.gameRunning.isRunning()) {
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
                if ((Config.getCompassTracking() && player.getGameMode().equals(GameMode.SURVIVAL)) && !ManHuntPlugin.getPlayerData().isFrozen(player) && !TimerCommand.playerShowTimers.contains(player.getUniqueId()))
                    player.sendActionBar(Component.text(ChatColor.RED + "Players have disappeared"));

            } else {
                Player Player = findPlayer.getValue().getPlayer();
                PlayerWorld playerWorld = findPlayer.getValue();
                Location location = playerWorld.getPlayerLocationInWold(player.getWorld());
                if(location == null) return;

                if ((Config.getCompassTracking() && player.getGameMode().equals(GameMode.SURVIVAL)) && !ManHuntPlugin.getPlayerData().isFrozen(player) && !TimerCommand.playerShowTimers.contains(player.getUniqueId())) {
                    player.sendActionBar(Component.text(ChatColor.GOLD + "Following : " + ChatColor.DARK_PURPLE + Player.getName()));
                }

                if (cp.getWorld() == location.getWorld() && !(cp.getBlockX() == location.getBlockX() && cp.getBlockY() == location.getBlockY() && cp.getBlockZ() == location.getBlockZ()) && Config.getCompassTracking()) {
                    for (ItemStack it : player.getInventory()) {
                        if (it != null) {
                            if (it.getType().equals(Material.COMPASS)) {
                                it.setItemMeta(getCompassMeta(it, location,ChatColor.GOLD + "Following : " + ChatColor.DARK_PURPLE + Player.getName()));
                            }
                        }
                        player.updateInventory();
                    }
                    player.setCompassTarget(location);

                }
            }
        }
    }

    public static void setPlayerlast(Player p){
        if(ManHuntPlugin.getPlayerData().getPlayerRole(p).equals(ManHuntRole.Speedrunner)) {
            if (Events.playerWorldMap.get(p.getUniqueId()) == null) {
                Events.playerWorldMap.put(p.getUniqueId(), new PlayerWorld(p.getWorld(), p));
            } else {
                PlayerWorld playerWorld = Events.playerWorldMap.get(p.getUniqueId());
                playerWorld.setWorldLocationHashMap(p.getWorld(), p.getLocation());
                Events.playerWorldMap.put(p.getUniqueId(), playerWorld);
            }
        }
    }

    public static Map.Entry<UUID, PlayerWorld>  getClosedPlayerLocation(Player p){
        Map.Entry<UUID, PlayerWorld> FindPlayer = Events.playerWorldMap.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(p.getUniqueId()))
                .filter(entry -> entry.getValue().getPlayer().getGameMode().equals(GameMode.SURVIVAL))
                .filter(entry -> {
                    if(entry.getValue().getPlayer().isOnline()) {
                        if (ManHuntPlugin.getPlayerData().getPlayerRoleByUUID(entry.getKey()) == ManHuntRole.Speedrunner) {
                            return true;
                        }
                    }
                          else if(Config.getSpawnPlayerLeaveZombie() && Events.players.get(entry.getKey()) == ManHuntRole.Speedrunner)
                              return true;
                return false;
                })
                .min(Comparator.comparing(entry -> entry.getValue().getPlayerLocationInWold(p.getWorld()).distance(p.getLocation())))
                .orElse(null);

        return FindPlayer;
    }



    public static CompassMeta getCompassMeta(ItemStack compass, Location loc, String name){
        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        meta.displayName(Component.text(name));
        meta.setLodestone(loc);
        meta.setLodestoneTracked(false);
        return meta;
    }

}
