package de.shiirroo.manhunt.utilis;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Config {

    private static Plugin plugin;

    private static boolean assassinsInstaKill;
    private static boolean freezeAssassin;
    private static boolean compassTracking;
    private static boolean giveCompass;
    private static boolean compassParticleInWorld;
    private static boolean compassParticleInNether;
    private static boolean showAdvancement;
    private static boolean bossbarCompass;
    private static boolean compassAutoUpdate;
    private static int huntStartTime;
    private static int compassTriggerTimer;
    private static int VoteStartTime;
    private static int SpeedrunnerOpportunity;
    private static int gameResetTime;

    public Config(Plugin plugin) {
        this.plugin = plugin;
        reload();
    }

    /**
     * Reload config from config file located in plugins directory
     */
    public void reload() {
        this.plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveConfig();

        this.huntStartTime = config.getInt("HuntStartTime");
        this.assassinsInstaKill = config.getBoolean("AssassinsInstaKill");
        this.compassTracking = config.getBoolean("CompassTracking");
        this.giveCompass = config.getBoolean("GiveCompass");
        this.compassParticleInWorld = config.getBoolean("CompassParticleInWorld");
        this.compassParticleInNether = config.getBoolean("CompassParticleInNether");
        this.freezeAssassin = config.getBoolean("FreezeAssassin");
        this.showAdvancement = config.getBoolean("ShowAdvancement");
        //this.bossbarCompass = config.getBoolean("BossbarCompass");
        this.compassAutoUpdate = config.getBoolean("CompassAutoUpdate");
        this.compassTriggerTimer = config.getInt("CompassTriggerTimer");
        this.SpeedrunnerOpportunity = config.getInt("SpeedrunnerOpportunity");
        this.VoteStartTime = config.getInt("VoteStartTime");
        this.gameResetTime = config.getInt("GameResetTime");
    }

    /**
     * @return true if the ManHunt can one shot the speedrunner
     */
     public LinkedHashMap<String, Boolean> getConfig(){
         LinkedHashMap<String, Boolean> getConfig = new LinkedHashMap<>();
        getConfig.put("AssassinsInstaKill", this.assassinsInstaKill);
        getConfig.put("BossbarCompass", this.bossbarCompass);
        getConfig.put("CompassTracking", this.compassTracking);
        getConfig.put("CompassAutoUpdate", this.compassAutoUpdate);
        getConfig.put("CompassParticleInWorld", this.compassParticleInWorld);
        getConfig.put("CompassParticleInNether", this.compassParticleInNether);
        getConfig.put("FreezeAssassin", this.freezeAssassin);
        getConfig.put("GiveCompass", this.giveCompass);
        getConfig.put("ShowAdvancement", this.showAdvancement);

    return getConfig;
    }

    public static void setConfig(LinkedHashMap<String, Boolean> hashMap){
         for(String linkedHashMap : hashMap.keySet()){
             plugin.getConfig().set(linkedHashMap, hashMap.get(linkedHashMap));
         }
         plugin.saveConfig();
        assassinsInstaKill = hashMap.get("AssassinsInstaKill");
        bossbarCompass = hashMap.get("BossbarCompass");
        compassTracking = hashMap.get("CompassTracking");
        compassAutoUpdate = hashMap.get("CompassAutoUpdate");
        compassParticleInWorld = hashMap.get("CompassParticleInWorld");
        compassParticleInNether = hashMap.get("CompassParticleInNether");
        freezeAssassin = hashMap.get("FreezeAssassin");
        giveCompass = hashMap.get("GiveCompass");
        showAdvancement = hashMap.get("ShowAdvancement");
    }


    public String getprefix() {
        return ChatColor.DARK_GRAY +"["+ ChatColor.GOLD + "Man" + ChatColor.RED + "Hunt"+ChatColor.DARK_GRAY +"] "+ ChatColor.GRAY ;
    }

    /**
     * @return true if the ManHunt will be frozen in place if the speedrunner puts their aim over the ManHunt.
     */

    public static int getGameResetTime() {
        return gameResetTime;
    }

    public boolean freeze() {
        return freezeAssassin;
    }

    public static int getVoteStartTime() {
        return VoteStartTime;
    }


    public static boolean isCompassAutoUpdate() {
        return compassAutoUpdate;
    }

    public boolean isBossbarCompass() {
        return bossbarCompass;
    }

    public static int getSpeedrunnerOpportunity() {
        return SpeedrunnerOpportunity;
    }

    /**
     * @return true if the assassins compass (if they have one) points to the closest speedrunner
     */
    public static boolean isCompassTracking() {
        return compassTracking;
    }

    /**
     * @return if the assassins compass (if they have one) points to the closest speedrunner
     */
    public static boolean giveCompass() {
        return giveCompass;
    }

    public static int huntStartTime() {
        return huntStartTime;
    }

    /**
     * @return true if it's needed to draw a yellow particle near ManHunt (if he holds a compass in main hand)in the direction of closest speedrunner
     */
    public boolean isCompassParticleInWorld() {
        return compassParticleInWorld;
    }

    public boolean isShowAdvancement() {
        return showAdvancement;
    }

    public static int compassTriggerTimer() { return compassTriggerTimer;}

    /**
     * @return true if it's needed to draw particles in the nether
     */
    public boolean isCompassParticleInNether() {
        return compassParticleInNether;
    }

    public boolean isAssassinsInstaKill() {
        return assassinsInstaKill;
    }

    /**
     * @return true, if compass will go crazy if there is no speedrunners in the same world
     */
}
