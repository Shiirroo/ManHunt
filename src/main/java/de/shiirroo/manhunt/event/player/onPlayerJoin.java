package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.vote.VoteCommand;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManager;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.menus.PlayerMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.SettingsMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePresetMenu;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.utilis.repeatingtask.ZombieSpawner;
import de.shiirroo.manhunt.world.PlayerWorld;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class onPlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        setUpPlayer(p);
        if (p.getGameMode().equals(GameMode.SPECTATOR)){
            event.joinMessage(Component.text(""));
        } else {
            event.joinMessage(Component.text(ChatColor.GRAY+ "["+ChatColor.GREEN +"+"+ ChatColor.GRAY + "] ").append(p.displayName().color(p.displayName().color())));
        }
    }

    private static ManHuntRole getRoleOfflinePlayer(Player player){
        return ManHuntPlugin.getGameData().getGamePlayer().getPlayerOfflineRole().get(player.getUniqueId());
    }

    public static void setUpPlayer(Player p){
        ManHuntPlugin.getGameData().getGamePlayer().getPlayerIP().put(Objects.requireNonNull(p.getAddress()).getAddress().getHostAddress(), p.getUniqueId());
        ManHuntRole mhr = getRoleOfflinePlayer(p);
        if(mhr != null) ManHuntPlugin.getGameData().getPlayerData().setRole(Objects.requireNonNull(p.getPlayer()), getRoleOfflinePlayer(p),ManHuntPlugin.getTeamManager());
        else ManHuntPlugin.getGameData().getPlayerData().setRole(Objects.requireNonNull(p.getPlayer()), ManHuntRole.Unassigned,ManHuntPlugin.getTeamManager());
        if(ManHuntPlugin.getGameData().getGameStatus().isGame()) {
            deleteZombiePlayer(p);
            if(!ManHuntPlugin.getGameData().getGameStatus().getLivePlayerList().contains(p.getUniqueId())){
                p.setGameMode(GameMode.SPECTATOR);
                ManHuntPlugin.getGameData().getPlayerData().setRole(p.getPlayer(), ManHuntRole.Unassigned,ManHuntPlugin.getTeamManager());
            }
        } else {
            p.getInventory().clear();
            p.setHealth(20);
            p.setFoodLevel(20);
            if(SettingsMenu.GamePreset != null) SettingsMenu.GamePreset.values().forEach(Menu::setMenuItems);
            p.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
            p.setGameMode(GameMode.ADVENTURE);
            try {
                ManHuntPlugin.playerMenu.put(p.getUniqueId(), MenuManager.getMenu(PlayerMenu.class, p.getUniqueId()).open());
            } catch (MenuManagerException | MenuManagerNotSetupException e) {
                Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.RED + "Something went wrong while player join");
                e.printStackTrace();
            }
            if(Ready.ready.getbossBarCreator().isRunning()) {
                if(Ready.ready.getbossBarCreator().getTimer() > 3){
                    Ready.ready.getbossBarCreator().cancel();
                } else if(Ready.ready.getbossBarCreator().getTimer() <= 3) {
                    if (ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(p.getUniqueId()).equals(ManHuntRole.Unassigned) || !ManHuntPlugin.getGameData().getGameStatus().getLivePlayerList().contains(p.getUniqueId())) {
                        p.setGameMode(GameMode.SPECTATOR);
                        p.getInventory().clear();
                    }
                }
            }
        }



        if(ManHuntPlugin.getGameData().getGameStatus().isStarting()) {
            StartGame.bossBarGameStart.setBossBarPlayer(p);
        } else if(VoteCommand.getVote() != null && VoteCommand.getVote().getVoteCreator().getbossBarCreator().isRunning()) {
            VoteCommand.getVote().getVoteCreator().getbossBarCreator().setBossBarPlayer(p);
        } else if(ManHuntPlugin.getWorldreset().getWorldReset().isRunning()){
            ManHuntPlugin.getWorldreset().getWorldReset().setBossBarPlayer(p);
        }

        if(Config.getBossbarCompass() && !BossBarCoordinates.hasCoordinatesBossbar(p)){
            BossBarCoordinates.addPlayerCoordinatesBossbar(p);
        }
        if(ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(p.getUniqueId()).equals(ManHuntRole.Unassigned)) {
            if (ManHuntPlugin.getGameData().getGamePlayer().getPlayerWorldMap().get(p.getUniqueId()) == null) {
                ManHuntPlugin.getGameData().getGamePlayer().getPlayerWorldMap().put(p.getUniqueId(), new PlayerWorld(p.getWorld(), p));
            } else {
                PlayerWorld playerWorld = ManHuntPlugin.getGameData().getGamePlayer().getPlayerWorldMap().get(p.getUniqueId());
                playerWorld.setWorldLocationHashMap(p.getWorld(), p.getLocation());
            }
        }

        p.sendPlayerListHeader(Component.text("\n" + ManHuntPlugin.getprefix()));
        GamePresetMenu.setFooderPreset(p);
    }

    private static void deleteZombiePlayer(Player p){
        if (ManHuntPlugin.getGameData().getGamePlayer().getZombieHashMap().get(p.getUniqueId()) != null) {
            ZombieSpawner zombieSpawner = ManHuntPlugin.getGameData().getGamePlayer().getZombieHashMap().get(p.getUniqueId());
            if(zombieSpawner != null) {
                Zombie zombie = (Zombie) Bukkit.getEntity(zombieSpawner.getZombieUUID());
                if (zombie == null || zombie.isDead()) {
                    p.getInventory().clear();
                    if (ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(p.getUniqueId()).equals(ManHuntRole.Speedrunner)) {
                        p.setHealth(0);
                    } else {
                        p.setExp(0);
                        p.setLevel(0);
                        p.setFoodLevel(20);
                        p.setHealth(20);
                        if (p.getBedSpawnLocation() != null) {
                            p.teleport(p.getBedSpawnLocation());
                        } else {
                            p.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
                        }
                        StartGame.getCompassTracker(p);
                    }
                } else {
                    if(zombie.getLocation() != p.getLocation()){
                        p.teleport(zombie.getLocation());
                    }
                    p.setHealth(zombie.getHealth());
                    zombie.remove();
                }
            }
            ManHuntPlugin.getGameData().getGamePlayer().getZombieHashMap().remove(p.getUniqueId());
        }
    }
}
