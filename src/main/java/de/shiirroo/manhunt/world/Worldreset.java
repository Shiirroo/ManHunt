package de.shiirroo.manhunt.world;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.utilis.BossBarCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Worldreset {

    public static BossBarCreator worldReset;


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


    public static void resetBossBar(){
        if(worldReset == null){
            System.out.println(ManHuntPlugin.getprefix() + "World is Resetting");
            worldReset = new BossBarCreator(ManHuntPlugin.getPlugin(), ChatColor.GREEN + "World will reset in"+ChatColor.RED + " TIMER", 30);
            worldReset.onComplete(aBoolean -> {
                        worldReset = null;
                        Bukkit.setWhitelist(true);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.kick(Component.text(ManHuntPlugin.getprefix() + "World is Resetting.."));
                        }
                        ManHuntPlugin.getPlugin().getConfig().set("isReset", true);
                        ManHuntPlugin.getPlugin().saveConfig();
                        Bukkit.spigot().restart();
                    }
            );
            worldReset.setBossBarPlayers();
        } else {
            worldReset.setBossBarPlayers();
        }

    }

}
