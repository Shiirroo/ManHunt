package de.shiirroo.manhunt.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarCoordinates {

    private static final Map<UUID, BossBar> bossbarCoordinatesPlayers = new HashMap<>();


    public static boolean hasCoordinatesBossbar(Player p) {
        return bossbarCoordinatesPlayers.get(p.getUniqueId()) != null;
    }

    public static String getCoordinatesBossbarTitle(Player p) {
        BossBar bb = getPlayerCoordinatesBossbar(p);
        if (bb != null) {
            return bb.getTitle();
        }
        return null;
    }


    public static BossBar getPlayerCoordinatesBossbar(Player p) {
        if (hasCoordinatesBossbar(p))
            return bossbarCoordinatesPlayers.get(p.getUniqueId());
        return null;
    }

    public static void addPlayerCoordinatesBossbar(Player p) {
        if (getPlayerCoordinatesBossbar(p) == null) {
            BossBar Bossbar = Bukkit.createBossBar(BossBarUtilis.setBossBarLoc(p), BarColor.BLUE, BarStyle.SOLID);
            Bossbar.addPlayer(p);
            bossbarCoordinatesPlayers.put(p.getUniqueId(), Bossbar);
        }

    }

    public static void editPlayerCoordinatesBossbar(Player p, String s) {
        BossBar bb = getPlayerCoordinatesBossbar(p);
        if (bb != null) {
            bb.setTitle(s);
        }
    }


    public static void deletePlayerCoordinatesBossbar(Player p) {
        BossBar bb = getPlayerCoordinatesBossbar(p);
        if (bb != null) {
            bb.removeAll();
            bb.setVisible(false);
            bossbarCoordinatesPlayers.remove(p.getUniqueId());
        }
    }
}
