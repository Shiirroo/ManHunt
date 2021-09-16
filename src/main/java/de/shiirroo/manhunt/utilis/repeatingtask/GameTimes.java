package de.shiirroo.manhunt.utilis.repeatingtask;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.TimerCommand;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.world.Worldreset;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class GameTimes implements Runnable {

    public static HashMap<UUID, Long> playerBossBar  = new HashMap<>();
    public static Integer elapsedTime = 1;

    @Override
    public void run() {
        if (!VoteCommand.pause) {
            if (StartGame.gameRunning == null) {
                for (Player player : ManHuntPlugin.getPlugin().getServer().getOnlinePlayers()) {
                    Long playerExit = Events.playerExit.get(player.getUniqueId());
                    if (playerExit == null && Ready.ready != null) {
                        player.sendActionBar(Component.text(ChatColor.GOLD + String.valueOf(Ready.ready.getPlayers().size()) + ChatColor.BLACK + " | " + ChatColor.GOLD + Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() + ChatColor.GREEN + " Ready"));
                    }
                }
            }  else if (StartGame.gameRunning != null && Events.gameStartTime != null) {
                    Long pauseTime = getPauseTime();

                    Long gameElapsedTime = (Calendar.getInstance().getTime().getTime() - Events.gameStartTime.getTime() - pauseTime) / 1000;
                    if ((gameElapsedTime - (Config.getGameResetTime() * 60 * 60)) >= 0) {
                        Events.gameStartTime = null;
                        Bukkit.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix() + ChatColor.RED + "The game time has expired, the map resets itself"));
                        Worldreset.resetBossBar();
                    } else if (elapsedTime == gameElapsedTime / (60 * 60)) {
                        long diffHours = Config.getGameResetTime() - gameElapsedTime / (60 * 60);
                        Bukkit.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix() + "Game time has elapsed " + ChatColor.GOLD + elapsedTime + ChatColor.GRAY + (elapsedTime > 1 ? " hour" : " hours") + ". There are still " + ChatColor.GOLD + diffHours + ChatColor.GRAY + (diffHours > 1 ? " hour" : " hours") + " left"));
                        elapsedTime = elapsedTime + 1;
                    }
                for(Player player: Bukkit.getOnlinePlayers()){
                    if(((playerBossBar.get(player.getUniqueId()) != null && playerBossBar.get(player.getUniqueId()).longValue() <= Calendar.getInstance().getTime().getTime()) || (playerBossBar.get(player.getUniqueId()) == null)) && TimerCommand.playerShowTimers.contains(player.getUniqueId())){
                      player.sendActionBar(Component.text(Events.getTimeString(false,getStartTime())));
                    }
                }
            }
        } else {
            for(Player player: Bukkit.getOnlinePlayers()){
                player.sendActionBar(Component.text(ChatColor.GOLD + "Game is Paused" + ChatColor.RED+ " ...   " + Events.getTimeString(false,Calendar.getInstance().getTime().getTime() - VoteCommand.pauseList.get((VoteCommand.pauseList.size() - 1)))));
            }


        }
    }

    public static Long getStartTime() {
        return ((Calendar.getInstance().getTime().getTime() - Events.gameStartTime.getTime())) - getPauseTime();
    }

    public static Long getPauseTime(){
        Long pauseTime = 0L;
        if (VoteCommand.pauseList.size() >= 1 && VoteCommand.unPauseList.size() >= 1)
            for (int i = 0; i != VoteCommand.pauseList.size(); i++)
                pauseTime = pauseTime + (VoteCommand.unPauseList.get(i) - VoteCommand.pauseList.get(i));
        return pauseTime;
    }
}
