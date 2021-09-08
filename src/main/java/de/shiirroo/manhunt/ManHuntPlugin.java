package de.shiirroo.manhunt;

import de.shiirroo.manhunt.bossbar.BossBarUtilis;
import de.shiirroo.manhunt.command.ManHuntCommandManager;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.block.onBlockBreak;
import de.shiirroo.manhunt.event.block.onBlockPlace;
import de.shiirroo.manhunt.event.entity.onEntityDamageByEntityEvent;
import de.shiirroo.manhunt.event.entity.onEntityDamageEvent;
import de.shiirroo.manhunt.event.entity.onEntityDeathEvent;
import de.shiirroo.manhunt.event.entity.onEntityMountEvent;
import de.shiirroo.manhunt.event.menu.MenuManager;
import de.shiirroo.manhunt.event.player.*;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.utilis.Worker;
import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.world.Worldreset;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public final class ManHuntPlugin extends JavaPlugin {

    private static Plugin plugin;
    private static Config config;
    public static boolean debug = false;


    @Override
    public void onEnable() {
        PlayerData playerData = new PlayerData();
        TeamManager manager = new TeamManager(this, playerData);
        Config config = new Config(this);
        this.config = config;

        for (Player p : getServer().getOnlinePlayers()) {
            if(p.isWhitelisted())
                p.setWhitelisted(false);
            p.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setGameMode(GameMode.ADVENTURE);
        }

        for (OfflinePlayer p : getServer().getOfflinePlayers()) {
            if(p.isWhitelisted())
                p.setWhitelisted(false);
        }

        getServer().setWhitelist(false);
        getServer().setDefaultGameMode(GameMode.ADVENTURE);
        for (World w : Bukkit.getWorlds()) {
            w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, this.getConfig().getBoolean("showAdvancement"));
            w.setPVP(false);
            w.setTime(0);
            w.setDifficulty(Difficulty.PEACEFUL);
            w.setGameRule(GameRule.DO_MOB_SPAWNING, false);

        }


        getServer().getPluginManager().registerEvents(new Events(playerData, config, manager), this);

        //---------------------BLOCK------------------------
        getServer().getPluginManager().registerEvents(new onBlockPlace(playerData), this);
        getServer().getPluginManager().registerEvents(new onBlockBreak(playerData), this);
        //---------------------Entity-----------------------

        getServer().getPluginManager().registerEvents(new onEntityDamageByEntityEvent(playerData, config), this);
        getServer().getPluginManager().registerEvents(new onEntityDamageEvent(), this);
        getServer().getPluginManager().registerEvents(new onEntityDeathEvent(playerData,config,this), this);
        getServer().getPluginManager().registerEvents(new onEntityMountEvent(config), this);
        //---------------------Player-------------------------
        getServer().getPluginManager().registerEvents(new onAsyncPlayerChatEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerAttemptPickupItemEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerCommandPreprocessEvent(playerData,config,manager), this);
        getServer().getPluginManager().registerEvents(new onPlayerDeathEvent(playerData,config, this), this);
        getServer().getPluginManager().registerEvents(new onPlayerInteractEvent(playerData,config), this);
        getServer().getPluginManager().registerEvents(new onPlayerJoin(playerData, config, manager), this);
        getServer().getPluginManager().registerEvents(new onPlayerLeave(playerData, config, manager), this);
        getServer().getPluginManager().registerEvents(new onPlayerMove(playerData, config), this);
        getServer().getPluginManager().registerEvents(new onPlayerRespawnEvent(playerData, config), this);
        getServer().getPluginManager().registerEvents(new onPlayerSwapHandItemsEvent(), this);
        //----------------------------------------------------


        Objects.requireNonNull(getCommand("ManHunt")).setExecutor(new ManHuntCommandManager(this, manager, playerData, config));
        Objects.requireNonNull(getCommand("ManHunt")).setTabCompleter(new ManHuntCommandManager(this, manager, playerData, config));


        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Worker(this, playerData, config), 1, 1);

        MenuManager.setup(this.getServer(), this, playerData);
        this.plugin = this;
        if(Ready.ready == null){
            Ready.setReadyVote();
        }


        getLogger().info("ManHunt plugin started.");
    }

    @Override
    public void onDisable() {
        getLogger().info("ManHunt plugin stopped.");
    }

    public static Plugin getPlugin(){
        return plugin;
    }

    public static Config getConfigSetting(){
        return config;
    }




    @Override
    public void onLoad() {
        getLogger().info("ManHunt plugin is loading.");
        saveConfig();
        if(!(getConfig().getInt("SpeedrunnerOpportunity") >= 1 && getConfig().getInt("SpeedrunnerOpportunity") <= 99)){
            getConfig().set("SpeedrunnerOpportunity", 30);
            saveConfig();
        }

        if(!(getConfig().getInt("GameResetTime") >= 2 && getConfig().getInt("GameResetTime") <= 100)){
            getConfig().set("GameResetTime", 8);
            saveConfig();
        }
        if(!(getConfig().getInt("VoteStartTime") >= 3 && getConfig().getInt("VoteStartTime") <= 120)){
            getConfig().set("VoteStartTime", 15);
            saveConfig();
        }
        if(!(getConfig().getInt("CompassTiggerTimer") >= 3 && getConfig().getInt("CompassTiggerTimer") <= 300)){
            getConfig().set("CompassTiggerTimer", 30);
            saveConfig();
        }

        if(!(getConfig().getInt("HuntStartTime") >= 3 && getConfig().getInt("HuntStartTime") <= 999)){
            getConfig().set("HuntStartTime", 30);
            saveConfig();
        }

        if(!getConfig().getBoolean("isReset")){
            getConfig().set("isReset", false);
            saveConfig();
            return;
        }
        if(getConfig().getBoolean("isReset")) {
            try {
                Worldreset.reset();
            } catch (IOException e) {
                System.out.println("World resetting is not working as intended");
            }

            getConfig().set("isReset", false);
            saveConfig();
        }
    }
}
