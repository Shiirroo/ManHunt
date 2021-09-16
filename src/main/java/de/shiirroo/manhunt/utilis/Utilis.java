package de.shiirroo.manhunt.utilis;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.world.Worldreset;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utilis {

    public static void drawLine(Location point1, Location point2, double space) {
        World world = point1.getWorld();
        Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        for (double length = 0; length < distance; length += space) {
            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 0.3f);
            if (p1.distance(point1.toVector()) > 1 && p1.distance(p2) > 1)
                world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 0, 0, 0, 0, dust);
            p1.add(vector);
        }
    }

    public static void drawDirection(Location point1, Location point2, double space) {
        World world = point1.getWorld();
        Validate.isTrue(point2.getWorld().equals(world), "You have to be in same worlds!");
        Vector p1 = point1.toVector();
        Vector dir = point2.toVector().clone().subtract(p1).setY(0).normalize().multiply(space);
        Vector p = p1.add(dir);
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0, 255, 0), 0.6f);
    }

    public static void drawWorldBorder(Player p, double dX, double dZ) {
        Location loc = p.getLocation();
        Vector vec = p.getWorld().getSpawnLocation().toVector();

        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 3);
        if (StartGame.gameRunning != null && StartGame.gameRunning.isRunning())
            dust = new Particle.DustOptions(Color.fromRGB(0, 250, 0), 3);
        if (dX > 8d) {
            p.getWorld().spawnParticle(Particle.REDSTONE, vec.getX() + 10.25, loc.getY() + 1.7, vec.getZ() + dZ, 0, 0, 0, 0, dust);
            p.getWorld().spawnParticle(Particle.REDSTONE, vec.getX() + 10.25, loc.getY() + 1, vec.getZ() + dZ, 0, 0, 0, 0, dust);
        }
        if (dZ > 8d) {
            p.getWorld().spawnParticle(Particle.REDSTONE, vec.getX() + dX, loc.getY() + 1.7, vec.getZ() + 10.25, 0, 0, 0, 0, dust);
            p.getWorld().spawnParticle(Particle.REDSTONE, vec.getX() + dX, loc.getY() + 1, vec.getZ() + 10.25, 0, 0, 0, 0, dust);
        }
        if (dX < -8d) {
            p.getWorld().spawnParticle(Particle.REDSTONE, vec.getX() - 10.25, loc.getY() + 1.7, vec.getZ() + dZ, 0, 0, 0, 0, dust);
            p.getWorld().spawnParticle(Particle.REDSTONE, vec.getX() - 10.25, loc.getY() + 1, vec.getZ() + dZ, 0, 0, 0, 0, dust);
        }
        if (dZ < -8d) {
            p.getWorld().spawnParticle(Particle.REDSTONE, vec.getX() + dX, loc.getY() + 1.7, vec.getZ() - 10.25, 0, 0, 0, 0, dust);
            p.getWorld().spawnParticle(Particle.REDSTONE, vec.getX() + dX, loc.getY() + 1, vec.getZ() - 10.25, 0, 0, 0, 0, dust);
        }
    }

    public static LivingEntity getTarget(Player player) {
        int range = 60;
        List<Entity> nearbyEntities = player.getNearbyEntities(range, range, range);
        ArrayList<LivingEntity> entities = new ArrayList<>();

        for (Entity e : nearbyEntities) {
            if (e instanceof LivingEntity) {
                entities.add((LivingEntity) e);
            }
        }

        LivingEntity target = null;
        BlockIterator bItr = new BlockIterator(player, range);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        while (bItr.hasNext()) {
            block = bItr.next();
            if (block.getType() != Material.AIR && block.getType() != Material.WATER) break;
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            for (LivingEntity e : entities) {
                if (e.getType().equals(EntityType.PLAYER)) {
                    loc = e.getLocation();
                    ex = loc.getX();
                    ey = loc.getY();
                    ez = loc.getZ();
                    if ((bx - .15 <= ex && ex <= bx + 1.15)
                            && (bz - .15 <= ez && ez <= bz + 1.15)
                            && (by - 1 <= ey && ey <= by + 1)) {
                        target = e;
                        break;
                    }
                }
            }
        }
        return target;
    }

    public static int generateRandomInt(int upperRange) {
        Random random = new Random();
        return random.nextInt(upperRange);
    }


    public static void allSpeedrunnersDead() {
        boolean allSpeedrunnerdead = true;
        for (Player player : ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner))  {
                if (!player.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
                    allSpeedrunnerdead = false;
            }
        }
        if (allSpeedrunnerdead) {
            Bukkit.getServer().sendMessage(Component.text(ManHuntPlugin.getprefix() + "All " + ChatColor.DARK_PURPLE + "Speedrunners" + ChatColor.GRAY + " are dead. " + ChatColor.RED + "Hunters " + ChatColor.GRAY + "win!!"));
            if (!ManHuntPlugin.debug) {
                StartGame.gameStartTime = null;
                Worldreset.resetBossBar();
            }
        }
    }

}
