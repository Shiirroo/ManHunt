package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.utilis.BossBarCreator;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.utilis.Worker;
import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.utilis.Utilis;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.Plugin;

import java.util.Calendar;
import java.util.Date;

public class StartGame extends SubCommand {

    public static BossBarCreator gameRunning;
    public static Date gameStartTime;

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
        if (gameRunning == null) {
            Start();
        };
        if (gameRunning.isRunning()) {
            player.sendMessage(ManHuntPlugin.getprefix() + "Game is running");
            return;
        }
        if (setPlayer())
            Start();
    }

    public static boolean setPlayer() {
        if (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).size() >= 1 && Bukkit.getOnlinePlayers().size() > 1) {
            while (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() != getSpeedrunners()) {
                Integer speedrunnerPlayerID = Utilis.generateRandomInt(ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).size());
                Player SpeedrunnerPlayer = ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).get(speedrunnerPlayerID);
                ManHuntPlugin.getPlayerData().setRole(SpeedrunnerPlayer, ManHuntRole.Speedrunner, ManHuntPlugin.getTeamManager());
            }
            while (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).size() >= 1) {
                Integer speedrunnerPlayerID = Utilis.generateRandomInt(ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).size());
                Player SpeedrunnerPlayer = ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).get(speedrunnerPlayerID);
                ManHuntPlugin.getPlayerData().setRole(SpeedrunnerPlayer, Utilis.generateRandomInt(2) == 0 ? ManHuntRole.Hunter : ManHuntRole.Assassin, ManHuntPlugin.getTeamManager());
            }
            return true;
        } else if (Bukkit.getOnlinePlayers().size() > 1 && ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() >= 1) {
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

            setGameWorld();
            gameRunning = new BossBarCreator(ManHuntPlugin.getPlugin(), ChatColor.DARK_RED + "Hunt " + ChatColor.RED + "will start in " + ChatColor.GOLD + "TIMER", Config.getHuntStartTime())
                    .onComplete(vote -> {
                        setGameStarting();
                        Events.gameStartTime = Calendar.getInstance().getTime();
                    })
                    .onShortlyComplete(vote -> {
                        Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f));
                    });

            gameRunning.setBossBarPlayers();
        }
    }


    private static void setGameWorld(){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.getInventory().clear();
            p.setWhitelisted(true);
            p.setExp(0);
            p.setBedSpawnLocation(p.getWorld().getSpawnLocation());
            p.sendActionBar(Component.text(ChatColor.DARK_PURPLE + "Speedrunners " + ChatColor.GRAY+"run!!"));
        }

        Bukkit.getServer().setWhitelist(true);

        for(World w : Bukkit.getWorlds()){
            w.setDifficulty(Difficulty.NORMAL);
            w.setGameRule(GameRule.DO_MOB_SPAWNING, true);
            w.setThundering(false);
            w.setTime(0);
        }
        for(Player speedrunner : ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner)){
            speedrunner.setGameMode(GameMode.SURVIVAL);
        }
    }

    private static void setGameStarting(){
        for(World w : Bukkit.getWorlds())
            w.setPVP(true);
        for(Player player : ManHuntPlugin.getPlayerData().getPlayersWithOutSpeedrunner()){
            player.sendActionBar(Component.text(ChatColor.RED + "Hunters" + ChatColor.GRAY + " go hunting!!"));
            player.setGameMode(GameMode.SURVIVAL);
            getCompassTracker(player);

        }
    }


    private static int getSpeedrunners(){
       double Opportunity = Config.getSpeedrunnerOpportunity()/100*Bukkit.getServer().getOnlinePlayers().size();
       if(Opportunity <= 1)
           return 1;
       return (int)Math.floor(Opportunity);
    }



    private static void getCompassTracker(Player player){
            ItemStack compass = new ItemStack(Material.COMPASS);
            CompassMeta meta = (CompassMeta) compass.getItemMeta();
            meta.setLodestoneTracked(false);
            if(Config.getGiveCompass() && Config.getCompassTracking() && Config.getCompassAutoUpdate()) {
                compass.setItemMeta(meta);
                player.getInventory().addItem(compass);
                Worker.updateCompass(player);
            } else if(Config.getGiveCompass())
            {
                compass.setItemMeta(meta);
                player.getInventory().addItem(compass);
            }

    }
}



