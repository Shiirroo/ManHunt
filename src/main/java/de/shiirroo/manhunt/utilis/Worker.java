package de.shiirroo.manhunt.utilis;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.bossbar.BossBarUtilis;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
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
import org.bukkit.util.Vector;

import java.util.*;


public class Worker implements Runnable {

    public static Map<UUID, Long> compassclickdelay = new HashMap<>();
    public static Integer reminderTime = 1;
    

    @Override
    public void run() {
         if(!VoteCommand.pause) {
            Set<Player> frozeThisTick = new HashSet<>();
            if (StartGame.gameRunning == null) {
                for (Player player : ManHuntPlugin.getPlugin().getServer().getOnlinePlayers()) {
                    Long playerExit = Events.playerExit.get(player.getUniqueId());
                    if (playerExit == null && Ready.ready != null) {
                        player.sendActionBar(Component.text(ChatColor.GOLD + String.valueOf(Ready.ready.getPlayers().size()) + ChatColor.BLACK + " | " + ChatColor.GOLD + Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() + ChatColor.GREEN + " Ready"));
                    }
                }
            }

            if (StartGame.gameRunning != null) {
                if (Events.gameStartTime != null) {
                    Long pauseTime = 0L;
                    if(VoteCommand.pauseList.size() > 0 && VoteCommand.unPauseList.size() > 0){
                        for(int i=0;i!=VoteCommand.pauseList.size();i++){
                            pauseTime = pauseTime + (VoteCommand.unPauseList.get(i) - VoteCommand.pauseList.get(i))/1000L;
                        }
                    }


                    Long l = (Calendar.getInstance().getTime().getTime() - Events.gameStartTime.getTime()) / 1000;
                    l = l - pauseTime;
                    Long gameReset = l - (Config.getGameResetTime() * 60 * 60) ;
                    if (gameReset >= 0) {
                        Events.gameStartTime = null;
                        Bukkit.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix() + ChatColor.RED + "The game time has expired, the map resets itself"));
                        Worldreset.resetBossBar();
                    } else if (reminderTime == l / (60 * 60)) {
                        long diffHours = Config.getGameResetTime() - l / (60 * 60 );
                        Bukkit.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix() + "Game time has elapsed " + ChatColor.GOLD + reminderTime + ChatColor.GRAY + (reminderTime > 1 ? " hour" : " hours") + ". There are still " + ChatColor.GOLD + diffHours + ChatColor.GRAY + (diffHours > 1 ? " hour" : " hours") + " left"));
                        reminderTime = reminderTime + 1;
                    }
                }


                if (Config.getFreezeAssassin()) {
                    for (Player player : ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner)) {
                        Entity target = Utilis.getTarget(player);
                        if (target == null || target.getType() != EntityType.PLAYER) continue;
                        Player targetPlayer = (Player) target;
                        if (targetPlayer.getGameMode() != GameMode.SURVIVAL || targetPlayer.isFlying())
                            return;
                        if (ManHuntPlugin.getPlayerData().getPlayerRole(targetPlayer) != ManHuntRole.Assassin) continue;
                        if (targetPlayer.getVehicle() != null) return;
                        ManHuntPlugin.getPlayerData().setFrozen(targetPlayer, true);
                        targetPlayer.sendActionBar(Component.text(ChatColor.DARK_AQUA + "Frozen " + ChatColor.GRAY + "by " + ChatColor.GOLD).append(player.displayName()));
                        frozeThisTick.add(targetPlayer);
                        Utilis.drawLine(player.getEyeLocation(), targetPlayer.getEyeLocation(), 1);
                    }
                }
            }

            if (StartGame.gameRunning != null) {
                for (Player player : ManHuntPlugin.getPlugin().getServer().getOnlinePlayers()) {

                    if (Config.getBossbarCompass() && ManHuntPlugin.debug) {
                        if (!BossBarCoordinates.hasCoordinatesBossbar(player))
                            BossBarCoordinates.addPlayerCoordinatesBossbar(player);
                        if (BossBarCoordinates.hasCoordinatesBossbar(player) && !Objects.requireNonNull(BossBarCoordinates.getCoordinatesBossbarTitle(player)).equalsIgnoreCase(BossBarUtilis.setBossBarLoc(player)))
                            BossBarCoordinates.editPlayerCoordinatesBossbar(player, BossBarUtilis.setBossBarLoc(player));
                    } else if (BossBarCoordinates.hasCoordinatesBossbar(player)) {
                        BossBarCoordinates.deletePlayerCoordinatesBossbar(player);
                    }


                    if (ManHuntPlugin.getPlayerData().isFrozen(player) && !frozeThisTick.contains(player))
                        ManHuntPlugin.getPlayerData().setFrozen(player, false);


                    if (Config.getCompassTracking() && Config.getCompassAutoUpdate()) {
                        ManHuntRole mht = ManHuntPlugin.getPlayerData().getPlayerRole(player);
                        if (mht != null) {
                            if (!mht.equals(ManHuntRole.Speedrunner)) {
                                updateCompass(player);
                            }
                        }
                    }
                    if (Config.getCompassParticleInWorld() || Config.getCompassParticleInNether()) {
                        ManHuntRole mht = ManHuntPlugin.getPlayerData().getPlayerRole(player);
                        if (mht != null) {
                            updateParticle(player);
                        }
                    }

                }
            }
        } else {
            for(Player player: Bukkit.getOnlinePlayers()){
                player.sendActionBar(Component.text(ChatColor.GOLD + "Game is Paused" + ChatColor.RED+ " ..."));
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
            if(Config.getCompassParticleInWorld() && player.getWorld().getEnvironment().equals(World.Environment.NORMAL)
                || Config.getCompassParticleInNether() && player.getWorld().getEnvironment().equals(World.Environment.NETHER))
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
                if ((Config.getCompassTracking() && player.getGameMode().equals(GameMode.SURVIVAL)) && !ManHuntPlugin.getPlayerData().isFrozen(player))
                    player.sendActionBar(Component.text(ChatColor.RED + "Players have disappeared"));

            } else {
                Player Player = (Player) findPlayer.getKey();
                PlayerWorld playerWorld = (PlayerWorld) findPlayer.getValue();
                Location location = playerWorld.getPlayerLocationInWold(player.getWorld());
                if(location == null) return;

                if ((Config.getCompassTracking() && player.getGameMode().equals(GameMode.SURVIVAL)) && !ManHuntPlugin.getPlayerData().isFrozen(player)) {
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
                    .filter(entry -> ManHuntPlugin.getPlayerData().getPlayerRole(entry.getKey().getPlayer()) == ManHuntRole.Speedrunner)
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
