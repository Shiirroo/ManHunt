package de.shiirroo.manhunt.utilis.vote;

import de.shiirroo.manhunt.ManHuntPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class BossBarCreator {


    private Set<UUID> votePlayers = new HashSet<>();
    private Integer voteTime;
    private Integer taskID;
    private String title = "";
    private BossBar bossBar;
    private boolean bossbarForVote;
    private Integer howManyPlayersinPercent = 50;
    private Consumer<Boolean> completeFunction;
    private Consumer<Boolean> shortlyFunction;
    private Plugin plugin;
    private double progess = 1.0;
    private double time;
    private int timer;


    public BossBarCreator(Plugin plugin, String title, Integer voteTime, Boolean bossbarForVote, Set<UUID> votePlayers){
        this.votePlayers = votePlayers;
        this.bossbarForVote = bossbarForVote;
        this.plugin = plugin;
        this.title = title;
        this.bossBar = Bukkit.createBossBar(this.updateBossBarTitle(), BarColor.RED, BarStyle.SOLID);
        this.voteTime = voteTime;
        this.timer = voteTime;
        this.time = 1.0 / voteTime;

    }

    public BossBarCreator(Plugin plugin, String title, Integer time){
        this.plugin = plugin;
        this.title = title;
        this.bossBar = Bukkit.createBossBar(title, BarColor.RED, BarStyle.SOLID);
        this.voteTime = time;
        this.timer = time;
        this.time = 1.0 / time;

    }

    public void setTime(Integer time){
        this.voteTime = time;
        this.timer = time;
        this.time = 1.0 / time;
    }

    public void setBossBarPlayer(Player player){
        this.bossBar.addPlayer(player);
    }

    public void setHowManyPlayersinPercent(Integer howManyPlayersinPercent) {
        this.howManyPlayersinPercent = howManyPlayersinPercent;
    }

    public void setBossBarPlayers(){
        for(Player player: Bukkit.getOnlinePlayers()){
                bossBar.addPlayer(player);
        }
        this.taskID = TastID();
        ManHuntPlugin.getPlayerData().updatePlayers(ManHuntPlugin.getTeamManager());
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public String updateBossBarTitle(){
        String bossBarTitle = (this.title).replace("TIMER",String.valueOf(this.timer));
        bossBarTitle = bossBarTitle.replace("VOTEPLAYERS", String.valueOf(this.votePlayers.size()));
        bossBarTitle = bossBarTitle.replace("ONLINEPLAYERS", String.valueOf(Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count()));
        bossBarTitle = bossBarTitle.replace("NOVOTEPLAYERS", String.valueOf((Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() - this.votePlayers.size())));
        return bossBarTitle;
    }

    public BossBarCreator onComplete(Consumer<Boolean> completeFunction) {
        Validate.notNull(completeFunction, "onComplete function cannot be null");
        this.completeFunction = completeFunction;
        return this;
    }

    public BossBarCreator onShortlyComplete(Consumer<Boolean> shortlyFunction) {
        Validate.notNull(shortlyFunction, "onShortlyComplete function cannot be null");
        this.shortlyFunction = shortlyFunction;
        return this;
    }

    public Consumer<Boolean> getCompleteFunction() {
        return completeFunction;
    }

    public Consumer<Boolean> getShortlyFunction() {
        return shortlyFunction;
    }

    public boolean isRunning(){
        if(taskID != null) {
            return true;
        }
        return false;
    }

    public int getTimer() {
        return this.timer;
    }

    private int TastID(){
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            bossBar.setProgress(this.progess);
            bossBar.setTitle(updateBossBarTitle());
            timer = timer - 1;
            progess = this.progess - this.time;
            if(timer < 2){
                if(shortlyFunction != null)
                    shortlyFunction.accept(false);
            }
            if(progess < 0) {
                cancel();
                if(completeFunction != null) {
                    if (bossbarForVote) {
                       if ((votePlayers.size() * 100) / Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() >= this.howManyPlayersinPercent){
                           this.completeFunction.accept(true);
                        } else {
                            completeFunction.accept(false);
                        }
                    } else
                        completeFunction.accept(true);
                }
            }
            if (bossbarForVote && Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1 && (Bukkit.getOnlinePlayers().stream().filter(e -> e.getGameMode().equals(GameMode.SURVIVAL)).count() == votePlayers.size())){
                cancel();
                completeFunction.accept(true);
            }
        }, 0, 20);
    };

    public void cancel() {
        if (this.taskID != null) {
            Bukkit.getScheduler().cancelTask(this.taskID);
            this.taskID = null;
            if (bossBar != null) {
                bossBar.removeAll();
            }
            this.progess = 1.0;
            this.bossBar.setProgress(this.progess);
            this.timer = this.voteTime;
            this.time = 1.0 / this.voteTime;
            this.bossBar.setTitle(this.updateBossBarTitle());
        }
    }
}
