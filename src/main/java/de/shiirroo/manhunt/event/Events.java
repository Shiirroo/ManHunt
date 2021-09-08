package de.shiirroo.manhunt.event;

import de.shiirroo.manhunt.*;
import de.shiirroo.manhunt.bossbar.BossBarCoordinates;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManager;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.menus.PlayerMenu;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.utilis.Worker;
import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.utilis.Utilis;
import de.shiirroo.manhunt.world.PlayerWorld;
import de.shiirroo.manhunt.world.Worldreset;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.spigotmc.event.entity.EntityMountEvent;

import java.io.IOException;
import java.util.*;


public class Events implements Listener {

    private static PlayerData playerData;
    private static TeamManager teamManager;
    private static Config config ;


    public static Map<UUID, ManHuntRole> players = new HashMap<>();
    public static Map<UUID, Menu> playerMenu = new HashMap<>();
    public static Map<UUID, Long> playerExit = new HashMap<>();
    public static Map<Player, PlayerWorld> playerWorldMap = new HashMap<>();
    public static Date gameStartTime;

    public Events(PlayerData playerData, Config config, TeamManager teamManager) {
        this.playerData = playerData;
        this.config = config;
        this.teamManager = teamManager;

    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onServerCommandEvent (ServerCommandEvent event) throws IOException {
        List<String> chars = new ArrayList<>(Arrays.asList(event.getCommand().split(" ")));
        if(StartGame.gameRunning == null){
            boolean eventBool =  UpdatePlayerInventory(chars, null);
            if(eventBool) {
                event.setCancelled(eventBool);
            }
        }
        if(chars.size() >= 2){
            if((chars.get(0).equalsIgnoreCase("/reload") || chars.get(0).equalsIgnoreCase("reload")) && chars.get(1).equalsIgnoreCase("confirm")){
                Worldreset.reset();
            }
        }
    }

    public static boolean UpdatePlayerInventory(List<String> chars, String PlayerName){
        if(chars.size() == 2 && (chars.get(0).equalsIgnoreCase("/op") || chars.get(0).equalsIgnoreCase("/deop")  || chars.get(0).equalsIgnoreCase("op") || chars.get(0).equalsIgnoreCase("deop"))) {
            Player UpdatePlayer = Bukkit.getPlayer(chars.get(1));
            if(UpdatePlayer != null && UpdatePlayer.isOnline()){
                if(UpdatePlayer.isOp()) {
                    UpdatePlayer.setOp(false);
                    teamManager.changePlayerName(UpdatePlayer, playerData.getRole(UpdatePlayer));
                    if(!UpdatePlayer.getName().equalsIgnoreCase(PlayerName)) {
                        UpdatePlayer.sendMessage(config.getprefix() + "Your operator has been removed");
                    }
                } else {
                    UpdatePlayer.setOp(true);
                    teamManager.changePlayerName(UpdatePlayer, playerData.getRole(UpdatePlayer));
                    if(!UpdatePlayer.getName().equalsIgnoreCase(PlayerName)) {
                        UpdatePlayer.sendMessage(config.getprefix() + "You became promoted to operator and can now execute ManHunt commands.");
                    }
                }
                playerMenu.get(UpdatePlayer.getUniqueId()).setMenuItems();
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerListPingEvent (ServerListPingEvent event) {
        if(StartGame.gameRunning == null && Ready.ready != null && Ready.ready.getbossBarCreator().isRunning() == false)
            event.motd(Component.text(config.getprefix() +"Game is not"+ ChatColor.GREEN +" running.." + "\n" +config.getprefix() +ChatColor.GREEN+"You can join the server" ));
        else if(Ready.ready != null && Ready.ready.getbossBarCreator().isRunning() == true)
            event.motd(Component.text(config.getprefix() +"Game is"+ ChatColor.GREEN +" ready to start.." + "\n" +config.getprefix() +ChatColor.GREEN+"You can join the server" ));
        else if(StartGame.gameRunning != null && StartGame.gameRunning.isRunning() && StartGame.gameRunning.getBossBar() != null)
            event.motd(Component.text(config.getprefix() +"Game is"+ ChatColor.YELLOW +" starting.." + "\n" +config.getprefix() +ChatColor.YELLOW+"You can´t join the server" ));
        else if(StartGame.gameRunning != null){
            event.motd(Component.text(config.getprefix() +"Game is"+ ChatColor.RED +" running since " +ChatColor.GRAY + getStartTimeFormat()+ " ]\n" +config.getprefix() +ChatColor.RED+"You can´t join the server" ));
        }
        if(StartGame.gameRunning == null) {
            event.setMaxPlayers(event.getNumPlayers());
        } else {
            event.setMaxPlayers((int) Bukkit.getOnlinePlayers().stream().filter(e -> e.getGameMode().equals(GameMode.SURVIVAL)).count());
        }



    }

    public String getStartTimeFormat(){
        if(gameStartTime != null){
            long diff = Calendar.getInstance().getTime().getTime() - gameStartTime.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);
            if(diffHours != 0){
                return "  [ "+ChatColor.GREEN + "" + diffHours + ChatColor.GRAY+ " h : "  +ChatColor.GREEN + diffMinutes + ChatColor.GRAY+ " m";
            } else if(diffMinutes != 0){
                return "     [ "+ChatColor.GREEN + "" + diffMinutes + ChatColor.GRAY+ " m : "  +ChatColor.GREEN +  diffSeconds  + ChatColor.GRAY+ " s";
            } else {
                return "          [ "+ChatColor.GREEN +""+  diffSeconds + ChatColor.GRAY +" s";
            }


        }
        return "[ ERROR";
    }

}
