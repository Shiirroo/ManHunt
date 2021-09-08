package de.shiirroo.manhunt.event;

import de.shiirroo.manhunt.*;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.world.PlayerWorld;
import de.shiirroo.manhunt.world.Worldreset;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import java.io.IOException;
import java.util.*;


public class Events implements Listener {

    public static Map<UUID, ManHuntRole> players = new HashMap<>();
    public static Map<UUID, Menu> playerMenu = new HashMap<>();
    public static Map<UUID, Long> playerExit = new HashMap<>();
    public static Map<Player, PlayerWorld> playerWorldMap = new HashMap<>();
    public static Date gameStartTime;

    public Events() {
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
                    ManHuntPlugin.getTeamManager().changePlayerName(UpdatePlayer, ManHuntPlugin.getPlayerData().getRole(UpdatePlayer));
                    if(!UpdatePlayer.getName().equalsIgnoreCase(PlayerName)) {
                        UpdatePlayer.sendMessage(ManHuntPlugin.getprefix() + "Your operator has been removed");
                    }
                } else {
                    UpdatePlayer.setOp(true);
                    ManHuntPlugin.getTeamManager().changePlayerName(UpdatePlayer, ManHuntPlugin.getPlayerData().getRole(UpdatePlayer));
                    if(!UpdatePlayer.getName().equalsIgnoreCase(PlayerName)) {
                        UpdatePlayer.sendMessage(ManHuntPlugin.getprefix() + "You became promoted to operator and can now execute ManHunt commands.");
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
            event.motd(Component.text(ManHuntPlugin.getprefix() +"Game is not"+ ChatColor.GREEN +" running.." + "\n" +ManHuntPlugin.getprefix() +ChatColor.GREEN+"You can join the server" ));
        else if(Ready.ready != null && Ready.ready.getbossBarCreator().isRunning() == true)
            event.motd(Component.text(ManHuntPlugin.getprefix() +"Game is"+ ChatColor.GREEN +" ready to start.." + "\n" +ManHuntPlugin.getprefix() +ChatColor.GREEN+"You can join the server" ));
        else if(StartGame.gameRunning != null && StartGame.gameRunning.isRunning() && StartGame.gameRunning.getBossBar() != null)
            event.motd(Component.text(ManHuntPlugin.getprefix() +"Game is"+ ChatColor.YELLOW +" starting.." + "\n" +ManHuntPlugin.getprefix() +ChatColor.YELLOW+"You can´t join the server" ));
        else if(StartGame.gameRunning != null){
            event.motd(Component.text(ManHuntPlugin.getprefix() +"Game is"+ ChatColor.RED +" running since " +ChatColor.GRAY + getStartTimeFormat()+ " ]\n" +ManHuntPlugin.getprefix() +ChatColor.RED+"You can´t join the server" ));
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
