package de.shiirroo.manhunt.utilis.repeatingtask;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.utilis.config.Config;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Calendar;
import java.util.List;

public class GameTimes implements Runnable{


    @Override
    public void run() {

        if (!ManHuntPlugin.getGameData().getGamePause().isPause()) {
            if (!ManHuntPlugin.getGameData().getGameStatus().isGame()) {
                for (Player player : ManHuntPlugin.getPlugin().getServer().getOnlinePlayers()) {
                    Long playerExitAreaTimer = ManHuntPlugin.getGameData().getGamePlayer().getPlayerExitGameAreaTimer().get(player.getUniqueId());
                    if(player.getGameMode().equals(GameMode.SPECTATOR)){
                        PotionEffect potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, 40, 255);
                        potionEffect.withParticles(false);
                        potionEffect.withIcon(false);
                        player.addPotionEffect(potionEffect);
                        player.sendActionBar(Component.text(ChatColor.GOLD +
                                "You're in the Spectator, wait for the game to start."));

                    }
                    else if (playerExitAreaTimer == null && Ready.ready != null) {
                        player.sendActionBar(Component.text(ChatColor.GOLD + String.valueOf(Ready.ready.getPlayers().size()) + ChatColor.BLACK + " | " + ChatColor.GOLD + Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() + ChatColor.GREEN + " Ready"));
                    }
                }
            }  else if (ManHuntPlugin.getGameData().getGameStatus().getGameStartTime() != 0 ) {
                    long pauseTime = getPauseTime(ManHuntPlugin.getGameData().getGamePause().getPauseList(),ManHuntPlugin.getGameData().getGamePause().getUnPauseList());
                    long timeCalc = (Calendar.getInstance().getTime().getTime() - ManHuntPlugin.getGameData().getGameStatus().getGameStartTime() - pauseTime) / 1000;
                    if(ManHuntPlugin.getGameData().getGameStatus().getGameElapsedTime() != timeCalc){
                        ManHuntPlugin.getGameData().getGameStatus().setGameElapsedTime(timeCalc);
                        if ((ManHuntPlugin.getGameData().getGameStatus().getGameElapsedTime() - ((long) Config.getGameResetTime() * 60 * 60)) >= 0) {
                            ManHuntPlugin.getGameData().getGameStatus().setGameElapsedTime(0);
                            Bukkit.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix() + ChatColor.RED + "The game time has expired, the map resets itself"));
                            ManHuntPlugin.getWorldreset().resetBossBar();
                        } else if (ManHuntPlugin.getGameData().getGameStatus().getElapsedTime() == ManHuntPlugin.getGameData().getGameStatus().getGameElapsedTime() / (60 * 60)) {
                            long diffHours = Config.getGameResetTime() - ManHuntPlugin.getGameData().getGameStatus().getGameElapsedTime() / (60 * 60);
                            Bukkit.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix() + "Game time has elapsed " + ChatColor.GOLD + ManHuntPlugin.getGameData().getGameStatus().getElapsedTime() + ChatColor.GRAY + (ManHuntPlugin.getGameData().getGameStatus().getElapsedTime() > 1 ? " hour" : " hours") + ". There are still " + ChatColor.GOLD + diffHours + ChatColor.GRAY + (diffHours > 1 ? " hour" : " hours") + " left"));
                            ManHuntPlugin.getGameData().getGameStatus().setElapsedTime(ManHuntPlugin.getGameData().getGameStatus().getElapsedTime() + 1);
                        }
                    for(Player player: Bukkit.getOnlinePlayers()){
                        if(((ManHuntPlugin.getGameData().getGamePlayer().getPlayerFrozenTime().get(player.getUniqueId()) != null && ManHuntPlugin.getGameData().getGamePlayer().getPlayerFrozenTime().get(player.getUniqueId()) <= Calendar.getInstance().getTime().getTime()) || (ManHuntPlugin.getGameData().getGamePlayer().getPlayerFrozenTime().get(player.getUniqueId()) == null)) && ManHuntPlugin.getGameData().getGamePlayer().getPlayerShowGameTimer().contains(player.getUniqueId())) {
                            player.sendActionBar(Component.text(Events.getTimeString(false, getStartTime(ManHuntPlugin.getGameData().getGameStatus().getGameStartTime(),ManHuntPlugin.getGameData().getGamePause().getPauseList(),ManHuntPlugin.getGameData().getGamePause().getUnPauseList()))));
                        }
                    }
                    if((boolean) ManHuntPlugin.getGameData().getGameMode().getRandomEffects().value && (ManHuntPlugin.getGameData().getGameStatus().getGameElapsedTime() % 60 == 0) && ManHuntPlugin.getGameData().getGameStatus().getGameElapsedTime() > 0){
                        ManHuntPlugin.getGameData().getGameMode().getRandomEffects().execute();
                    }
                    if((ManHuntPlugin.getGameData().getGameStatus().getGameElapsedTime() % 300 == 0) && ManHuntPlugin.getGameData().getGameStatus().getGameElapsedTime() > 0){
                        if(ManHuntPlugin.getGameData().getGameStatus().getAutoSave() != null)
                            ManHuntPlugin.getGameData().getGameStatus().getAutoSave().saveGame(true, ManHuntPlugin.getGameData());
                    }
                }
            }
        } else {
            for(Player player: Bukkit.getOnlinePlayers()){
                player.sendActionBar(Component.text(ChatColor.GOLD + "Game is Paused" + ChatColor.RED+ " ...   " + Events.getTimeString(false,Calendar.getInstance().getTime().getTime() - ManHuntPlugin.getGameData().getGamePause().getPauseList().get((ManHuntPlugin.getGameData().getGamePause().getPauseList().size() - 1)))));
            }
        }
    }

    public static Long getStartTime(Long gameStartTime, List<Long> pauseList, List<Long> unPauseList) {
        return ((Calendar.getInstance().getTime().getTime() - gameStartTime)) - getPauseTime(pauseList, unPauseList);
    }

    public static Long getPauseTime(List<Long> pauseList, List<Long> unPauseList){
        long pauseTime = 0L;
        if (pauseList.size() >= 1) {
            for (int i = 0; i != pauseList.size(); i++) {
                if (i < unPauseList.size()) {
                    pauseTime += (unPauseList.get(i) - (pauseList.get(i)));
                } else {
                    pauseTime += (Calendar.getInstance().getTime().getTime() - (pauseList.get(i)));
                }
            }
        }
        return pauseTime;
    }
}
