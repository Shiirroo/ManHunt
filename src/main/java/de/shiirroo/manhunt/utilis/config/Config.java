package de.shiirroo.manhunt.utilis.config;

import de.shiirroo.manhunt.ManHuntPlugin;

import java.io.Serializable;

public class Config implements Serializable {

    public static Integer getHuntStartTime(){
        return (Integer) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("HuntStartTime").getConfigSetting();
    }
    public static Boolean getAssassinsInstaKill(){
        return (Boolean) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("AssassinsInstaKill").getConfigSetting();
    }
    public static Boolean getCompassTracking(){
        return (Boolean) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("CompassTracking").getConfigSetting();
    }
    public static Boolean getGiveCompass(){
        return (Boolean) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("GiveCompass").getConfigSetting();
    }
    public static Boolean getCompassParticleToSpeedrunner(){
        return (Boolean) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("CompassParticleToSpeedrunner").getConfigSetting();
    }
    public static Boolean getFreezeAssassin(){
        return (Boolean) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("FreezeAssassin").getConfigSetting();
    }
    public static Boolean getBossbarCompass(){
        return (Boolean) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("BossbarCompass").getConfigSetting();
    }
    public static Boolean getShowAdvancement(){
        return (Boolean) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("ShowAdvancement").getConfigSetting();
    }
    public static Boolean getCompassAutoUpdate(){
        return (Boolean) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("CompassAutoUpdate").getConfigSetting();
    }
    public static Integer getCompassTriggerTimer(){
        return (Integer) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("CompassTriggerTimer").getConfigSetting();
    }
    public static Integer getSpeedrunnerOpportunity(){
        return (Integer) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("SpeedrunnerOpportunity").getConfigSetting();
    }
    public static Integer getReadyStartTime(){
        return (Integer) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("ReadyStartTime").getConfigSetting();
    }
    public static Integer getGameResetTime(){
        return (Integer) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("GameResetTime").getConfigSetting();
    }
    public static Boolean getSpawnPlayerLeaveZombie(){
        return (Boolean) ManHuntPlugin.getGameData().getGameConfig().getConfigCreators("SpawnPlayerLeaveZombie").getConfigSetting();
    }
    public static void relodConfig(){
        ManHuntPlugin.getPlugin().reloadConfig();
        ManHuntPlugin.getGameData().reloadGameConfig(ManHuntPlugin.getPlugin());
    }
}
