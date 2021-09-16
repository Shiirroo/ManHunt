package de.shiirroo.manhunt;

import de.shiirroo.manhunt.command.ManHuntCommandManager;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.block.onBlockBreak;
import de.shiirroo.manhunt.event.block.onBlockPlace;
import de.shiirroo.manhunt.event.entity.*;
import de.shiirroo.manhunt.event.menu.MenuManager;
import de.shiirroo.manhunt.event.player.*;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.utilis.repeatingtask.CompassTracker;
import de.shiirroo.manhunt.utilis.config.ConfigCreator;
import de.shiirroo.manhunt.utilis.repeatingtask.GameTimes;
import de.shiirroo.manhunt.world.Worldreset;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.*;
import java.util.*;

public final class ManHuntPlugin extends JavaPlugin implements Serializable {

    private static Plugin plugin;
    public static boolean debug = false;
    private static Set<ConfigCreator> configCreatorsSett;
    private static PlayerData playerData;
    private static TeamManager teamManager;
    public static Integer GameTimesTimer = 1;


    public static String getprefix() {
        return ChatColor.DARK_GRAY +"["+ ChatColor.GOLD + "Man" + ChatColor.RED + "Hunt"+ChatColor.DARK_GRAY +"] "+ ChatColor.GRAY ;
    }

    @Override
    public void onEnable() {
        this.plugin = this;
        FileConfiguration fileConfiguration = this.getConfig();
        fileConfiguration.options().copyDefaults(true);
        ConfigCreator isGameRunning =  new ConfigCreator("isGameRunning").configCreator(fileConfiguration).Plugin(this);

        this.playerData = new PlayerData();
        this.teamManager = new TeamManager(this);


        registerConfig(this);

        registerEvents();
        Objects.requireNonNull(getCommand("ManHunt")).setExecutor(new ManHuntCommandManager());
        Objects.requireNonNull(getCommand("ManHunt")).setTabCompleter(new ManHuntCommandManager());

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new CompassTracker(), 1, 1);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new GameTimes(), 0, GameTimesTimer);

        MenuManager.setup(this.getServer(), this);


        if(isGameRunning.getConfigSetting().equals(false)){
            setUPWorld();
        }

        getLogger().info("ManHunt plugin started.");
    }

    @Override
    public void onDisable() {

        /*FileOutputStream fos = null;
        try {
            //File myObj = new File("GameFile.tmp");
            fos = new FileOutputStream("plugins\\ManHunt\\GameFile.tmp");
            BukkitObjectOutputStream oos = new BukkitObjectOutputStream(fos);

        oos.writeObject(getPlayerData());
        oos.close();

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
            e.printStackTrace();
        }*/

        getLogger().info("ManHunt plugin stopped.");




    }

    public static Plugin getPlugin(){
        return plugin;
    }


    public static void registerConfig(Plugin plugin){
        configCreatorsSett = new LinkedHashSet<>();
        FileConfiguration fileConfiguration = plugin.getConfig();
        fileConfiguration.options().copyDefaults(true);
        System.out.println(ManHuntPlugin.getprefix() +"Config is loaded.");
        configCreatorsSett.add(new ConfigCreator("HuntStartTime", 5, 999 ,120).configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("AssassinsInstaKill").configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("CompassTracking" ).configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("GiveCompass").configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("CompassParticleToSpeedrunner").configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("FreezeAssassin").configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("BossbarCompass").configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("ShowAdvancement").configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("CompassAutoUpdate").configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("CompassTriggerTimer", 5, 300,15).configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("SpeedrunnerOpportunity", 1, 99,40 ).configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("SpawnPlayerLeaveZombie").configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("ReadyStartTime", 5, 120,15).configCreator(fileConfiguration).Plugin(plugin));
        configCreatorsSett.add(new ConfigCreator("GameResetTime", 2, 100, 8 ).configCreator(fileConfiguration).Plugin(plugin));
        System.out.println(ManHuntPlugin.getprefix() +"Config was loaded successfully");
    }

    public static Set<ConfigCreator> getConfigCreatorsSett(){
        return configCreatorsSett;
    }

    public static ConfigCreator getConfigCreators(String ConfigName){
        Optional<ConfigCreator> configCreator = ManHuntPlugin.getConfigCreatorsSett().stream().filter(config -> config.getConfigName().equalsIgnoreCase(ConfigName)).findFirst();
        if(configCreator != null && configCreator.isPresent())
            return configCreator.get();
        return null;
    }


    private void registerEvents(){

        getServer().getPluginManager().registerEvents(new Events(), this);

        //---------------------BLOCK------------------------
        getServer().getPluginManager().registerEvents(new onBlockPlace(), this);
        getServer().getPluginManager().registerEvents(new onBlockBreak(), this);
        //---------------------Entity-----------------------

        getServer().getPluginManager().registerEvents(new onEntityDamageByEntityEvent(), this);
        getServer().getPluginManager().registerEvents(new onEntityDamageEvent(), this);
        getServer().getPluginManager().registerEvents(new onEntityDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new onEntityMountEvent(), this);
        getServer().getPluginManager().registerEvents(new onEntityMoveEvent(), this);
        //---------------------Player-------------------------
        getServer().getPluginManager().registerEvents(new onAsyncPlayerChatEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerAttemptPickupItemEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerCommandPreprocessEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerInteractEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new onPlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new onPlayerMove(), this);
        getServer().getPluginManager().registerEvents(new onPlayerRespawnEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerSwapHandItemsEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerGameModeChangeEvent(), this);
        //----------------------------------------------------

    }

    public void setUPWorld(){
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
            w.getWorldBorder().setCenter(w.getSpawnLocation());
            w.getWorldBorder().setSize(20);
        }

        if(Ready.ready == null){
            Ready.setReadyVote();
        }


    }

    public static PlayerData getPlayerData() {
        return playerData;
    }

    public static TeamManager getTeamManager() {
        return teamManager;
    }

    @Override
    public void onLoad() {

        /*try {
            File f = new File("plugins\\ManHunt\\GameFile.tmp");
            if(f.exists()) {
                FileInputStream fis = new FileInputStream("plugins\\ManHunt\\GameFile.tmp");
                if (fis != null) {

                    BukkitObjectInputStream ois = new BukkitObjectInputStream(fis);
                    PlayerData playerData = (PlayerData) ois.readObject();
                    System.out.println(playerData.getPlayerRole(Bukkit.getPlayer("Shiirroo")));
                    ois.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/

        getLogger().info("ManHunt plugin is loading.");
        saveConfig();



        if(!(getConfig().getInt("SpeedrunnerOpportunity") >= 1 && getConfig().getInt("SpeedrunnerOpportunity") <= 99)){
            getConfig().set("SpeedrunnerOpportunity", 40);
            saveConfig();
        }

        if(!(getConfig().getInt("GameResetTime") >= 2 && getConfig().getInt("GameResetTime") <= 100)){
            getConfig().set("GameResetTime", 8);
            saveConfig();
        }
        if(!(getConfig().getInt("ReadyStartTime") >= 5 && getConfig().getInt("ReadyStartTime") <= 120)){
            getConfig().set("ReadyStartTime", 15);
            saveConfig();
        }
        if(!(getConfig().getInt("CompassTiggerTimer") >= 5 && getConfig().getInt("CompassTiggerTimer") <= 300)){
            getConfig().set("CompassTiggerTimer", 30);
            saveConfig();
        }

        if(!(getConfig().getInt("HuntStartTime") >= 5 && getConfig().getInt("HuntStartTime") <= 999)){
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
                System.out.println(ManHuntPlugin.getprefix() + "World resetting is not working as intended");
            }

            getConfig().set("isReset", false);
            saveConfig();
        }
    }
}
