package de.shiirroo.manhunt.utilis;

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
    public static Boolean getCompassParticleInWorld(){
        return (Boolean) ManHuntPlugin.getConfigCreators("CompassParticleInWorld").getConfigSetting();
    }
    public static Boolean getCompassParticleInNether(){
        return (Boolean) ManHuntPlugin.getConfigCreators("CompassParticleInNether").getConfigSetting();
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
    public static Integer getVoteStartTime(){
        return (Integer) ManHuntPlugin.getConfigCreators("VoteStartTime").getConfigSetting();
    }
    public static Integer getGameResetTime(){
        return (Integer) ManHuntPlugin.getConfigCreators("GameResetTime").getConfigSetting();
    }
    public static void relodConfig(){
        ManHuntPlugin.getPlugin().reloadConfig();
        ManHuntPlugin.registerConfig(ManHuntPlugin.getPlugin());
    }
}
