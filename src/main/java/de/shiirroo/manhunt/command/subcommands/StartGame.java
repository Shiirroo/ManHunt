package de.shiirroo.manhunt.command.subcommands;

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

    private static Plugin plugin;
    private static TeamManager teamManager;
    private static PlayerData playerData;
    private static Config config;
    public static BossBarCreator gameRunning;
    public static Date gameStartTime;

    public StartGame(Plugin plugin, TeamManager teamManager, PlayerData playerData, Config config) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.playerData = playerData;
        this.config = config;
    }


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
            player.sendMessage(config.getprefix() + ChatColor.RED + "I´m sorry, but you don´t have permission to perform this command");
            return;
        }
        if (gameRunning == null) {
            Start();
        };
        if (gameRunning.isRunning()) {
            player.sendMessage(config.getprefix() + "Game is running");
            return;
        }
        if (setPlayer())
            Start();
    }

    public static boolean setPlayer() {
        if (playerData.getPlayersByRole(ManHuntRole.Unassigned).size() >= 1 && Bukkit.getOnlinePlayers().size() > 1) {
            while (playerData.getPlayersByRole(ManHuntRole.Speedrunner).size() <= getSpeedrunners()) {
                Integer speedrunnerPlayerID = Utilis.generateRandomInt(playerData.getPlayersByRole(ManHuntRole.Unassigned).size());
                Player SpeedrunnerPlayer = playerData.getPlayersByRole(ManHuntRole.Unassigned).get(speedrunnerPlayerID);
                playerData.setRole(SpeedrunnerPlayer, ManHuntRole.Speedrunner, teamManager);
            }
            while (playerData.getPlayersByRole(ManHuntRole.Unassigned).size() >= 1) {
                Integer speedrunnerPlayerID = Utilis.generateRandomInt(playerData.getPlayersByRole(ManHuntRole.Unassigned).size());
                Player SpeedrunnerPlayer = playerData.getPlayersByRole(ManHuntRole.Unassigned).get(speedrunnerPlayerID);
                playerData.setRole(SpeedrunnerPlayer, Utilis.generateRandomInt(2) == 0 ? ManHuntRole.Hunter : ManHuntRole.Assassin, teamManager);
            }
            return true;
        } else if (Bukkit.getOnlinePlayers().size() > 1 && playerData.getPlayersByRole(ManHuntRole.Speedrunner).size() >= 1) {
            return true;
        }
        return false;
    }


    public static void Start() {
        if (gameRunning == null) {
            if(Ready.ready != null){
                Ready.ready.cancelVote();
            }
            setGameWorld();
            gameRunning = new BossBarCreator(plugin, ChatColor.DARK_RED + "Hunt " + ChatColor.RED + "will start in " + ChatColor.GOLD + "TIMER", config.huntStartTime())
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
            p.sendActionBar(Component.text(ChatColor.DARK_PURPLE + "Speedrunners " + ChatColor.GRAY+"run!!"));
        }

        Bukkit.getServer().setWhitelist(true);

        for(World w : Bukkit.getWorlds()){
            w.setDifficulty(Difficulty.NORMAL);
            w.setGameRule(GameRule.DO_MOB_SPAWNING, true);
            w.setThundering(false);
            w.setTime(0);
        }
        for(Player speedrunner : playerData.getPlayersByRole(ManHuntRole.Speedrunner)){
            speedrunner.setGameMode(GameMode.SURVIVAL);
        }
    }

    private static void setGameStarting(){
        for(World w : Bukkit.getWorlds())
            w.setPVP(true);
        for(Player Gameplayer : Bukkit.getOnlinePlayers()){
            if(!playerData.getPlayerRole(Gameplayer).equals(ManHuntRole.Speedrunner)) {
                Gameplayer.sendActionBar(Component.text(ChatColor.RED + "Hunters" + ChatColor.GRAY + " go hunting!!"));
                Gameplayer.setGameMode(GameMode.SURVIVAL);
                getCompassTracker(Gameplayer);
            }
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
            if(Config.giveCompass() && Config.isCompassTracking() && Config.isCompassAutoUpdate()) {
                compass.setItemMeta(meta);
                player.getInventory().addItem(compass);
                Worker.updateCompass(player);
            } else if(Config.giveCompass())
            {
                compass.setItemMeta(meta);
                player.getInventory().addItem(compass);
            }

    }
}



