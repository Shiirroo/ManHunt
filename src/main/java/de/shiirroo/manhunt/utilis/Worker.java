package de.shiirroo.manhunt.utilis;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.bossbar.BossBarUtilis;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.world.PlayerWorld;
import de.shiirroo.manhunt.world.Worldreset;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.*;


public class Worker implements Runnable {

    private static Plugin plugin;
    private static PlayerData playerData;
    private static Config config;
    public static Map<UUID, Long> compassclickdelay = new HashMap<>();
    public static Integer reminderTime = 1;



    public Worker(Plugin plugin, PlayerData playerData, Config config) {
        Worker.plugin = plugin;
        Worker.playerData = playerData;
        Worker.config = config;
    }

    @Override
    public void run() {


        Set<Player> frozeThisTick = new HashSet<>();
        if(StartGame.gameRunning == null) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                Long playerExit = Events.playerExit.get(player.getUniqueId());
                if(playerExit == null) {
                    player.sendActionBar(Component.text(ChatColor.GOLD + String.valueOf(Ready.ready.getPlayers().size()) + ChatColor.BLACK + " | " + ChatColor.GOLD + Bukkit.getOnlinePlayers().size() + ChatColor.GREEN + " Ready"));
                }
            }
        }

        if(StartGame.gameRunning != null) {
            if (Events.gameStartTime != null) {
                long l = (Calendar.getInstance().getTime().getTime() - Events.gameStartTime.getTime()) / 1000;
                long gameReset = l - (config.getGameResetTime() * 60 * 60);
                if (gameReset >= 0) {
                    Events.gameStartTime = null;
                    Bukkit.broadcast(Component.text(config.getprefix() + ChatColor.RED + "The game time has expired, the map resets itself"));

                    Worldreset.setBoosBar(plugin);
                } else if (reminderTime == l / (60 *60)) {
                    long diffHours = config.getGameResetTime() - l / (60 *60);
                    Bukkit.broadcast(Component.text(config.getprefix() + "Game time has elapsed " + ChatColor.GOLD + reminderTime + ChatColor.GRAY + (reminderTime > 1 ? " hour" : " hours") + ". There are still " + ChatColor.GOLD + diffHours + ChatColor.GRAY + (diffHours > 1 ? " hour" : " hours") + " left"));
                    reminderTime = reminderTime + 1;
                }
            }


            if (config.freeze()) {
                for (Player player : playerData.getPlayersByRole(ManHuntRole.Speedrunner)) {
                    Entity target = Utilis.getTarget(player);
                    if (target == null || target.getType() != EntityType.PLAYER) continue;
                    Player targetPlayer = (Player) target;
                    if (player.getGameMode() != GameMode.SURVIVAL || targetPlayer.isFlying())
                        return;
                    if (playerData.getRole(targetPlayer) != ManHuntRole.Assassin) continue;
                    if (targetPlayer.getVehicle() != null) return;
                    playerData.setFrozen(targetPlayer, true);
                    targetPlayer.sendActionBar(Component.text(ChatColor.DARK_AQUA + "Frozen " + ChatColor.GRAY + "by " + ChatColor.GOLD).append(player.displayName()));
                    frozeThisTick.add(targetPlayer);
                    Utilis.drawLine(player.getEyeLocation(), targetPlayer.getEyeLocation(), 1);
                }
            }
        }

        if(StartGame.gameRunning != null) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {

                if (config.isBossbarCompass() && ManHuntPlugin.debug) {
                    if (!BossBarCoordinates.hasCoordinatesBossbar(player))
                        BossBarCoordinates.addPlayerCoordinatesBossbar(player);
                    if (BossBarCoordinates.hasCoordinatesBossbar(player) && !Objects.requireNonNull(BossBarCoordinates.getCoordinatesBossbarTitle(player)).equalsIgnoreCase(BossBarUtilis.setBossBarLoc(player)))
                        BossBarCoordinates.editPlayerCoordinatesBossbar(player, BossBarUtilis.setBossBarLoc(player));
                } else if (BossBarCoordinates.hasCoordinatesBossbar(player)) {
                    BossBarCoordinates.deletePlayerCoordinatesBossbar(player);
                }


                if (playerData.isFrozen(player) && !frozeThisTick.contains(player))
                    playerData.setFrozen(player, false);


                if (config.isCompassTracking() && config.isCompassAutoUpdate()) {
                    ManHuntRole mht = playerData.getPlayerRole(player);
                    if (mht != null) {
                        if (!mht.equals(ManHuntRole.Speedrunner)) {
                                     updateCompass(player);
                            }
                        }
                    }
                if (config.isCompassParticleInWorld() || config.isCompassParticleInNether()) {
                    ManHuntRole mht = playerData.getPlayerRole(player);
                    if (mht != null) {
                        updateParticle(player);
                    }
                }

            }
        }

}

    private  static void updateParticle(Player player){
        Map.Entry findPlayer =  getClosedPlayerLocation(player);
        if(findPlayer == null) return;
        PlayerWorld playerWorld = (PlayerWorld) findPlayer.getValue();
        Location location = playerWorld.getPlayerLocationInWold(player.getWorld());
        if(location == null) return;
        PlayerInventory inventory = player.getInventory();

        if (inventory.getItemInMainHand().getType() == Material.COMPASS || inventory.getItemInOffHand().getType() == Material.COMPASS) {
            if(config.isCompassParticleInWorld() && player.getWorld().getEnvironment().equals(World.Environment.NORMAL)
                || config.isCompassParticleInNether() && player.getWorld().getEnvironment().equals(World.Environment.NETHER))
                Utilis.drawDirection(player.getLocation(), location, 3);
        }
    }



    public static void updateCompass(Player player) {
        if(StartGame.gameRunning != null) {
            Map.Entry findPlayer =  getClosedPlayerLocation(player);
            Location cp = player.getCompassTarget();

            if (findPlayer == null) {
                float angle = (float) (Math.random() * Math.PI * 2);
                float dx = (float) (Math.cos(angle) * 5);
                float dz = (float) (Math.sin(angle) * 5);

                for (ItemStack it : player.getInventory())
                    if (it != null)
                        if (it.getType().equals(Material.COMPASS))
                            it.setItemMeta(getCompassMeta(it, player.getLocation().add(new Vector(dx, 0, dz)), ChatColor.RED + "Players have disappeared"));
                if ((config.isCompassTracking() && player.getGameMode().equals(GameMode.SURVIVAL)) && !playerData.isFrozen(player))
                    player.sendActionBar(Component.text(ChatColor.RED + "Players have disappeared"));

            } else {
                Player Player = (Player) findPlayer.getKey();
                PlayerWorld playerWorld = (PlayerWorld) findPlayer.getValue();
                Location location = playerWorld.getPlayerLocationInWold(player.getWorld());
                if(location == null) return;

                if ((config.isCompassTracking() && player.getGameMode().equals(GameMode.SURVIVAL)) && !playerData.isFrozen(player)) {
                    player.sendActionBar(Component.text(ChatColor.GOLD + "Following : " + ChatColor.DARK_PURPLE + Player.getName()));
                }

                if (cp.getWorld() == location.getWorld() && !(cp.getBlockX() == location.getBlockX() && cp.getBlockY() == location.getBlockY() && cp.getBlockZ() == location.getBlockZ()) && config.isCompassTracking()) {
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
            if(Events.playerWorldMap.get(p) == null){
                Events.playerWorldMap.put(p, new PlayerWorld(p.getWorld(), p.getLocation()));
            } else {
                PlayerWorld playerWorld = Events.playerWorldMap.get(p);
                playerWorld.setWorldLocationHashMap(p.getWorld(), p.getLocation());
                Events.playerWorldMap.put(p, playerWorld);
            }
        }

        public static Map.Entry getClosedPlayerLocation(Player p){
            Map.Entry FindPlayer = Events.playerWorldMap.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals(p))
                    .filter(entry -> !entry.getKey().getGameMode().equals(GameMode.CREATIVE))
                    .filter(entry -> entry.getKey().getGameMode().equals(GameMode.SURVIVAL))
                    .filter(entry -> playerData.getRole(entry.getKey().getPlayer()) == ManHuntRole.Speedrunner)
                    .min(Comparator.comparing(entry -> entry.getValue().getPlayerLocationInWold(p.getWorld()).distance(p.getLocation())))
                    .orElse(null);
            return FindPlayer;
        }



    public static CompassMeta getCompassMeta(ItemStack compass,Location loc, String name){
        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        meta.displayName(Component.text(name));
        meta.setLodestone(loc);
        meta.setLodestoneTracked(false);
        return meta;

    }

}
