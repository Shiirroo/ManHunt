package de.shiirroo.manhunt.event.player;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.menu.MenuManager;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.menus.PlayerMenu;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.repeatingtask.CompassTracker;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.world.Worldreset;
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

import java.util.*;

public class onPlayerJoin implements Listener {

    public static HashMap<String, Player> playerIP = new HashMap<>();


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) throws MenuManagerException, MenuManagerNotSetupException {
        Player p = event.getPlayer();
        if(p.getPlayer() == null ) return;
        playerIP.put(p.getAddress().getHostString(), p);

        ManHuntRole mhr = GetRoleOfflinePlayer(p.getPlayer());
        if(mhr != null) ManHuntPlugin.getPlayerData().setRole(p.getPlayer(), GetRoleOfflinePlayer(p), ManHuntPlugin.getTeamManager());
        else ManHuntPlugin.getPlayerData().setRole(p.getPlayer(), ManHuntRole.Unassigned, ManHuntPlugin.getTeamManager());

        Component displayname = event.getPlayer().displayName();
        if(StartGame.gameRunning != null) {
            if (onPlayerLeave.zombieHashMap.get(event.getPlayer().getUniqueId()) != null) {
                Zombie zombie = onPlayerLeave.zombieHashMap.get(event.getPlayer().getUniqueId()).getZombie();
                if (zombie.isDead()) {
                    p.getInventory().clear();;
                    if (ManHuntPlugin.getPlayerData().getPlayerRole(p).equals(ManHuntRole.Speedrunner)) {
                        p.setHealth(0);
                    } else {
                        p.setExp(0);
                        p.setLevel(0);
                        p.setFoodLevel(20);
                        p.setHealth(20);
                        if(p.getBedSpawnLocation() != null){
                            p.teleport(p.getBedSpawnLocation());
                        } else {
                            p.teleport(Bukkit.getWorld("world").getSpawnLocation());
                        }
                        StartGame.getCompassTracker(p);


                        }
                    } else {
                    p.setHealth(zombie.getHealth());
                    zombie.remove();
                }
                onPlayerLeave.zombieHashMap.remove(event.getPlayer().getUniqueId());
            }
        }

        if(StartGame.gameRunning == null){
            p.getInventory().clear();
            p.setHealth(20);
            p.setFoodLevel(20);
            p.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
            Events.playerMenu.put(event.getPlayer().getUniqueId(), MenuManager.openMenu(PlayerMenu.class, event.getPlayer(), null));
            if(Ready.ready != null && Ready.ready.getbossBarCreator().isRunning()) {
                if(Ready.ready.getbossBarCreator().getTimer() > 3){
                    Ready.ready.getbossBarCreator().cancel();
                } else if(Ready.ready.getbossBarCreator().getTimer() <= 3) {
                    if (ManHuntPlugin.getPlayerData().getPlayerRole(p).equals(ManHuntRole.Unassigned) || !StartGame.playersonStart.contains(event.getPlayer().getUniqueId())) {
                        event.getPlayer().setGameMode(GameMode.SPECTATOR);
                        event.getPlayer().getInventory().clear();
                    }
                }
            }
        }

        if(Ready.ready != null && StartGame.gameRunning == null){
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        }


        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
            event.joinMessage(Component.text(""));
        } else {
            event.joinMessage(Component.text(ChatColor.GRAY+ "["+ChatColor.GREEN +"+"+ ChatColor.GRAY + "] ").append(displayname.color(displayname.color())));
        }

        if(StartGame.gameRunning != null && StartGame.gameRunning.isRunning()) {
            StartGame.gameRunning.setBossBarPlayer(event.getPlayer());
        } else if(VoteCommand.vote != null && VoteCommand.vote.getbossBarCreator().isRunning()) {
            VoteCommand.vote.getbossBarCreator().setBossBarPlayer(event.getPlayer());
        } else if(Worldreset.worldReset != null && Worldreset.worldReset.isRunning()){
            Worldreset.worldReset.setBossBarPlayer(p);
        }



        if(Config.getBossbarCompass() && !BossBarCoordinates.hasCoordinatesBossbar(event.getPlayer())){
            BossBarCoordinates.addPlayerCoordinatesBossbar(event.getPlayer());
        }
        CompassTracker.setPlayerlast(p);
    }

    private ManHuntRole GetRoleOfflinePlayer(Player player){
        return Events.players.get(player.getUniqueId());
    }
}
