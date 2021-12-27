package de.shiirroo.manhunt.event;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.event.player.onPlayerCommandPreprocessEvent;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.repeatingtask.GameTimes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Events implements Listener, Serializable {

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerCommandEvent(ServerCommandEvent event) {
        List<String> chars = new ArrayList<>(Arrays.asList(event.getCommand().split(" ")));
            boolean eventBool = UpdatePlayerInventory(chars, null);
            if (eventBool) {
                event.setCancelled(true);
        }

        if ((chars.size() == 1 && (chars.get(0).equalsIgnoreCase("/reload") || chars.get(0).equalsIgnoreCase("reload"))) ||
                (chars.size() == 2 && (chars.get(0).equalsIgnoreCase("/reload") || chars.get(0).equalsIgnoreCase("reload")) && chars.get(0).equalsIgnoreCase("confirm")))
                {
                    event.setCancelled(true);
                    onPlayerCommandPreprocessEvent.removeBossBar();
                    Bukkit.reload();
        }
    }

    public static boolean UpdatePlayerInventory(List<String> chars, String PlayerName) {
        if (chars.size() == 2 && (chars.get(0).equalsIgnoreCase("/op") || chars.get(0).equalsIgnoreCase("/deop") || chars.get(0).equalsIgnoreCase("op") || chars.get(0).equalsIgnoreCase("deop"))) {
            Player UpdatePlayer = Bukkit.getPlayer(chars.get(1));
            if (UpdatePlayer != null && UpdatePlayer.isOnline()) {
                if (UpdatePlayer.isOp()) {
                    UpdatePlayer.setOp(false);
                    ManHuntPlugin.getTeamManager().changePlayerName(UpdatePlayer, ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(UpdatePlayer.getUniqueId()));
                    if (!UpdatePlayer.getName().equalsIgnoreCase(PlayerName)) {
                        UpdatePlayer.sendMessage(ManHuntPlugin.getprefix() + "Your operator has been removed");
                    }
                } else {
                    UpdatePlayer.setOp(true);
                    ManHuntPlugin.getTeamManager().changePlayerName(UpdatePlayer, ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(UpdatePlayer.getUniqueId()));
                    if (!UpdatePlayer.getName().equalsIgnoreCase(PlayerName)) {
                        UpdatePlayer.sendMessage(ManHuntPlugin.getprefix() + "You became promoted to operator and can now execute ManHunt commands.");
                    }
                }
                if(!ManHuntPlugin.getGameData().getGameStatus().isGame()) ManHuntPlugin.playerMenu.get(UpdatePlayer.getUniqueId()).setMenuItems();
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerListPingEvent(ServerListPingEvent event) {
        Player player = null;
        if (ManHuntPlugin.getGameData().getGamePlayer().getPlayerIP() != null && ManHuntPlugin.getGameData().getGamePlayer().getPlayerIP() .get(event.getAddress().getHostAddress()) != null && event.getAddress().getHostAddress() != null) {
            player = Bukkit.getPlayer(ManHuntPlugin.getGameData().getGamePlayer().getPlayerIP().get(event.getAddress().getHostAddress()));
        }

        if (!ManHuntPlugin.getGameData().getGameStatus().isGameRunning())
            event.setMotd(ManHuntPlugin.getprefix() + "Game is not" + ChatColor.GREEN + " running.." + "\n" + ManHuntPlugin.getprefix() + ChatColor.GREEN + "You can join the server");
        else if (Ready.ready.getbossBarCreator().isRunning() && Ready.ready.getbossBarCreator().getTimer() > 3)
            event.setMotd(ManHuntPlugin.getprefix() + "Game is" + ChatColor.GREEN + " ready to start in " + ChatColor.GOLD + Ready.ready.getbossBarCreator().getTimer() + ChatColor.GREEN + " sec\n" + ManHuntPlugin.getprefix() + ChatColor.GREEN + "You can join the server");
        else if (Ready.ready.getbossBarCreator().isRunning() && Ready.ready.getbossBarCreator().getTimer() <= 3)
            event.setMotd(ManHuntPlugin.getprefix() + "Game is" + ChatColor.GREEN + " ready to start in " + ChatColor.GOLD + Ready.ready.getbossBarCreator().getTimer() + ChatColor.GREEN + " sec\n" + ManHuntPlugin.getprefix() + ChatColor.GREEN + "You can´t join the server");
        else if (ManHuntPlugin.getGameData().getGameStatus().isStarting())
            event.setMotd(ManHuntPlugin.getprefix() + "Game is" + ChatColor.YELLOW + " starting in " + ChatColor.GOLD + StartGame.bossBarGameStart.getTimer() + ChatColor.YELLOW + " sec\n" + ManHuntPlugin.getprefix() + ChatColor.YELLOW + (player != null && player.isWhitelisted() ? ChatColor.GOLD + player.getName() + ChatColor.GREEN + " you can join the server" : ChatColor.RED + "You can´t join the server"));
        else if (!ManHuntPlugin.getGameData().getGamePause().isPause()) {
            event.setMotd(ManHuntPlugin.getprefix() + "Game is" + ChatColor.RED + " running since: " + ChatColor.GRAY + getTimeString(true,  GameTimes.getStartTime(ManHuntPlugin.getGameData().getGameStatus().getGameStartTime(),ManHuntPlugin.getGameData().getGamePause().getPauseList(),ManHuntPlugin.getGameData().getGamePause().getUnPauseList())) +
                        "\n" + ManHuntPlugin.getprefix() + ChatColor.YELLOW + (player != null && player.isWhitelisted() ? ChatColor.GOLD + player.getName() + ChatColor.GREEN + " you can join the server" : ChatColor.RED + "You can´t join the server"));
        }
        else event.setMotd(ManHuntPlugin.getprefix() + "Game is" + ChatColor.AQUA + " paused since: " + ChatColor.GRAY + getTimeString(true, Calendar.getInstance().getTime().getTime() - ManHuntPlugin.getGameData().getGamePause().getPauseList().get((ManHuntPlugin.getGameData().getGamePause().getPauseList().size() - 1))) + "\n" + ManHuntPlugin.getprefix() + ChatColor.YELLOW + (player != null && player.isWhitelisted() ? ChatColor.GOLD + player.getName() + ChatColor.GREEN + " you can join the server" : ChatColor.RED + "You can´t join the server"));

        if (!ManHuntPlugin.getGameData().getGameStatus().isGame()) {
                event.setMaxPlayers(event.getNumPlayers());
            } else {
                event.setMaxPlayers((int) Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count());
          }

    }

        public static String getTimeString(Boolean space, Long time){
            String pauseString = ChatColor.GRAY + "";
                long diffSeconds = time / 1000 % 60;
                long diffMinutes = time / (60 * 1000) % 60;
                long diffHours = time / (60 * 60 * 1000);
                if (diffHours != 0) {
                    if (space) pauseString = "  ";
                    pauseString += "[ " + ChatColor.GREEN + "" + diffHours + ChatColor.GRAY + " h : " + ChatColor.GREEN + diffMinutes + ChatColor.GRAY + " m";
                } else if (diffMinutes != 0) {
                    if (space) pauseString = "     ";
                    pauseString += "[ " + ChatColor.GREEN + "" + diffMinutes + ChatColor.GRAY + " m : " + ChatColor.GREEN + diffSeconds + ChatColor.GRAY + " s";
                } else {
                    if (space) pauseString = "          ";
                    pauseString += "[ " + ChatColor.GREEN + "" + diffSeconds + ChatColor.GRAY + " s";
                }
            return pauseString + ChatColor.GRAY + " ]";
    }

    public static boolean cancelEvent(Player player){
        return (!player.getGameMode().equals(GameMode.CREATIVE) && ((ManHuntPlugin.getGameData().getGameStatus().isGame() && ManHuntPlugin.getGameData().getGamePause().isPause()) ||
                (ManHuntPlugin.getGameData().getGamePlayer().getIsFrozen().entrySet().stream().anyMatch(uuiduuidEntry -> uuiduuidEntry.getValue().equals(player.getUniqueId()))) ||
                (!ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(player.getUniqueId()).equals(ManHuntRole.Speedrunner)
                        && ManHuntPlugin.getGameData().getGameStatus().isStarting()) || (ManHuntPlugin.getGameData().getGameStatus().isReadyForVote() && !ManHuntPlugin.getGameData().getGameStatus().isGame())));
    }

}