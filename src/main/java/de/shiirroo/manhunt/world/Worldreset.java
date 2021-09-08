package de.shiirroo.manhunt.world;

import de.shiirroo.manhunt.ManHuntPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Worldreset {

    public static int taskID;
    public static boolean running = false;
    public static BossBar bossBar;


    public static void reset() throws IOException {
        Bukkit.unloadWorld("world",false);
        Bukkit.unloadWorld("world_nether",false);
        Bukkit.unloadWorld("world_the_end",false);
        deleteRecursively(new File("world"));
        deleteRecursively(new File("world_nether"));
        deleteRecursively(new File("world_the_end"));
    }

    public static void deleteRecursively(File directory) {
        for(File file : Objects.requireNonNull(directory.listFiles())) {
            if(file.isDirectory()) {
                deleteRecursively(file);
            }
            else {
                file.delete();
            }
        }
    }

    private static int TastID(Plugin plugin){
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            double progess = 1.0;
            final double time = 1.0 / 30;
            int startGame = 30;
            @Override
            public void run() {
                bossBar.setProgress(progess);
                bossBar.setTitle(ChatColor.GREEN + "World will reset in " + ChatColor.GOLD+ startGame);

                progess = progess - time;
                startGame -= 1;
                if(startGame < 3){
                    Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f));
                }
                if(progess <= 0){
                    Bukkit.getScheduler().cancelTask(taskID);
                    bossBar.removeAll();
                    bossBar = null;
                    Bukkit.setWhitelist(true);
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.kick(Component.text(ManHuntPlugin.getprefix() + "World is Resetting.."));
                    }
                    plugin.getConfig().set("isReset", true);
                    plugin.saveConfig();
                    Bukkit.spigot().restart();
                }

            }

        }, 0, 20);
    };

    private static BossBar getBossBar(Plugin plugin){
        bossBar = Bukkit.createBossBar(ChatColor.GREEN + "World will reset in " + ChatColor.GOLD+ 30, BarColor.RED, BarStyle.SOLID);
        running = true;
        taskID = TastID(plugin);
        return bossBar;
    }

    public static void setBoosBar(Plugin plugin){
        bossBar = getBossBar(plugin);
        bossBar.setVisible(true);
        for(Player p : Bukkit.getOnlinePlayers()){
            bossBar.addPlayer(p);
        }
    }

    public static void cancelReady() {
        if (running) {
            running = false;
            if (bossBar != null) {
                bossBar.removeAll();
                bossBar = null;
            }
            if (taskID != 0) {
                Bukkit.getScheduler().cancelTask(taskID);
            }
        }
    }



}
