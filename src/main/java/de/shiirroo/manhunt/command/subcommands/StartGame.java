package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.player.onPlayerLeave;
import de.shiirroo.manhunt.utilis.*;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.utilis.repeatingtask.CompassTracker;
import de.shiirroo.manhunt.utilis.vote.BossBarCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.*;
import java.util.stream.Collectors;

public class StartGame extends SubCommand {

    public static BossBarCreator gameRunning;
    public static Date gameStartTime;
    public static HashSet<UUID> playersonStart;

    @Override
    public String getName() {
        return "Start";
    }

    @Override
    public String getDescription() {
        return "Start the ManHunt game";
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Start";
    }

    @Override
    public Boolean getNeedOp() {
        return true;
    }


    @Override
    public CommandBuilder getSubCommandsArgs(String[] args) {
        return null;
    }


    @Override
    public void perform(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED + "I´m sorry, but you don´t have permission to perform this command");
            return;
        }
        if (gameRunning != null && gameRunning.isRunning()) {
            player.sendMessage(ManHuntPlugin.getprefix() + "Game is running");
            return;
        }
        if (setPlayer()) {
            Start();
        }
    }

    public static boolean setPlayer() {
        if (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).count() >= 1 && Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1) {
            while (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() != getSpeedrunners()) {
                Integer speedrunnerPlayerID = Utilis.generateRandomInt((int) ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).count());
                Player SpeedrunnerPlayer = ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).collect(Collectors.toList()).get(speedrunnerPlayerID);
                ManHuntPlugin.getPlayerData().setRole(SpeedrunnerPlayer, ManHuntRole.Speedrunner, ManHuntPlugin.getTeamManager());
            }
            while (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).count() >= 1) {
                Integer speedrunnerPlayerID = Utilis.generateRandomInt((int) ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).count());
                Player SpeedrunnerPlayer = ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).collect(Collectors.toList()).get(speedrunnerPlayerID);
                ManHuntPlugin.getPlayerData().setRole(SpeedrunnerPlayer, Utilis.generateRandomInt(2) == 0 ? ManHuntRole.Hunter : ManHuntRole.Assassin, ManHuntPlugin.getTeamManager());
            }
            return true;
        } else if (Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1 && ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() >= 1) {
            return true;
        }
        return false;
    }


    public static void Start() {
        if (gameRunning == null) {
            if(Ready.ready != null){
                Ready.ready.cancelVote();
                Ready.ready = null;
            }
            playersonStart = new HashSet<>();
            System.out.println(ManHuntPlugin.getprefix() + ChatColor.GRAY + "Game will start soon.");
            ManHuntPlugin.GameTimesTimer = 20;
            setGameWorld();
            gameRunning = new BossBarCreator(ManHuntPlugin.getPlugin(), ChatColor.DARK_RED + "Hunt " + ChatColor.RED + "will start in " + ChatColor.GOLD + "TIMER", Config.getHuntStartTime())
                    .onComplete(vote -> {
                        setGameStarting();
                        Events.gameStartTime = Calendar.getInstance().getTime();
                        System.out.println(ManHuntPlugin.getprefix() + ChatColor.GRAY + "Hunters can hunt.");
                    })
                    .onShortlyComplete(vote -> {
                        Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f));
                    });

            gameRunning.setBossBarPlayers();
            System.out.println(ManHuntPlugin.getprefix() + ChatColor.GRAY + "Speedrunner can run.");
        }
    }


    private static void setGameWorld(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(!p.getGameMode().equals(GameMode.SPECTATOR)) {
                p.getInventory().clear();
                p.setWhitelisted(true);
                p.setExp(0);
                p.setLevel(0);
                p.setHealth(20);
                p.setFoodLevel(20);
                p.setTotalExperience(0);
                p.setBedSpawnLocation(p.getWorld().getSpawnLocation());
                p.sendActionBar(Component.text(ChatColor.DARK_PURPLE + "Speedrunners " + ChatColor.GRAY + "run!!"));
                playersonStart.add(p.getUniqueId());
            }
        }

        Bukkit.getServer().setWhitelist(true);

        for(World w : Bukkit.getWorlds()){
            w.setDifficulty(Difficulty.NORMAL);
            w.setGameRule(GameRule.DO_MOB_SPAWNING, true);
            w.setThundering(false);
            w.setTime(0);
            w.getWorldBorder().setCenter(w.getSpawnLocation());
            w.getWorldBorder().setSize(59999968);
            w.getWorldBorder().reset();
        }
        for(Player speedrunner : ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner)){
            if(!speedrunner.getGameMode().equals(GameMode.SPECTATOR)) {
                speedrunner.setGameMode(GameMode.SURVIVAL);
            }
        }
        onPlayerLeave.zombieHashMap = new HashMap<>();
    }

    private static void setGameStarting(){
        for(World w : Bukkit.getWorlds())
            w.setPVP(true);
        for(Player player : ManHuntPlugin.getPlayerData().getPlayersWithOutSpeedrunner()){
            if(!player.getGameMode().equals(GameMode.SPECTATOR)) {
                player.sendActionBar(Component.text(ChatColor.RED + "Hunters" + ChatColor.GRAY + " go hunting!!"));
                player.setGameMode(GameMode.SURVIVAL);
                getCompassTracker(player);
            }

        }
    }


    private static int getSpeedrunners(){
       double Opportunity = Config.getSpeedrunnerOpportunity()/100*Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count();
       if(Opportunity <= 1)
           return 1;
       return (int)Math.floor(Opportunity);
    }



    public static void getCompassTracker(Player player){
            ItemStack compass = new ItemStack(Material.COMPASS);
            CompassMeta meta = (CompassMeta) compass.getItemMeta();
            meta.setLodestoneTracked(false);
            if(Config.getGiveCompass() && Config.getCompassTracking() && Config.getCompassAutoUpdate()) {
                compass.setItemMeta(meta);
                player.getInventory().addItem(compass);
                CompassTracker.updateCompass(player);
            } else if(Config.getGiveCompass())
            {
                compass.setItemMeta(meta);
                player.getInventory().addItem(compass);
            }
    }
}



