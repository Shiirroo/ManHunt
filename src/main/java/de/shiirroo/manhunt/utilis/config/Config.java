package de.shiirroo.manhunt.utilis.config;

import de.shiirroo.manhunt.ManHuntPlugin;

public class Config {


    public static Integer getHuntStartTime(){
        return (Integer) ManHuntPlugin.getConfigCreators("HuntStartTime").getConfigSetting();
    }
    public static Boolean getAssassinsInstaKill(){
        return (Boolean) ManHuntPlugin.getConfigCreators("AssassinsInstaKill").getConfigSetting();
    }
    public static Boolean getCompassTracking(){
        return (Boolean) ManHuntPlugin.getConfigCreators("CompassTracking").getConfigSetting();
    }
    public static Boolean getGiveCompass(){
        return (Boolean) ManHuntPlugin.getConfigCreators("GiveCompass").getConfigSetting();
    }
    public static Boolean getCompassParticleToSpeedrunner(){
        return (Boolean) ManHuntPlugin.getConfigCreators("CompassParticleToSpeedrunner").getConfigSetting();
    }
    public static Boolean getFreezeAssassin(){
        return (Boolean) ManHuntPlugin.getConfigCreators("FreezeAssassin").getConfigSetting();
    }
    public static Boolean getBossbarCompass(){
        return (Boolean) ManHuntPlugin.getConfigCreators("BossbarCompass").getConfigSetting();
    }
    public static Boolean getShowAdvancement(){
        return (Boolean) ManHuntPlugin.getConfigCreators("ShowAdvancement").getConfigSetting();
    }
    public static Boolean getCompassAutoUpdate(){
        return (Boolean) ManHuntPlugin.getConfigCreators("CompassAutoUpdate").getConfigSetting();
    }
    public static Integer getCompassTriggerTimer(){
        return (Integer) ManHuntPlugin.getConfigCreators("CompassTriggerTimer").getConfigSetting();
    }
    public static Integer getSpeedrunnerOpportunity(){
        return (Integer) ManHuntPlugin.getConfigCreators("SpeedrunnerOpportunity").getConfigSetting();
    }
    public static Integer getReadyStartTime(){
        return (Integer) ManHuntPlugin.getConfigCreators("ReadyStartTime").getConfigSetting();
    }
    public static Integer getGameResetTime(){
        return (Integer) ManHuntPlugin.getConfigCreators("GameResetTime").getConfigSetting();
    }
    public static Boolean getSpawnPlayerLeaveZombie(){
        return (Boolean) ManHuntPlugin.getConfigCreators("SpawnPlayerLeaveZombie").getConfigSetting();
    }
    public static void relodConfig(){
        ManHuntPlugin.getPlugin().reloadConfig();
        ManHuntPlugin.registerConfig(ManHuntPlugin.getPlugin());
    }
}
