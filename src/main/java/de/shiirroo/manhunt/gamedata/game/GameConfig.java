package de.shiirroo.manhunt.gamedata.game;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.utilis.config.ConfigCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class GameConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private LinkedHashSet<ConfigCreator> configCreatorsSett = new LinkedHashSet<>();

    public GameConfig(GameConfig gameConfig){
        configCreatorsSett.addAll(gameConfig.getConfigCreatorsSett());
    }

    public GameConfig(Plugin plugin){
        LinkedHashSet<ConfigCreator> newConfigCreatorsSett = new LinkedHashSet<>();
        Bukkit.getLogger().info(ManHuntPlugin.getprefix() +"Config is loaded.");
        newConfigCreatorsSett.add(new ConfigCreator("HuntStartTime", 5, 999 ,120).configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Start time when", ChatColor.GRAY +"the hunters can hunt"))));
        newConfigCreatorsSett.add(new ConfigCreator("AssassinsInstaKill").configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Assassins can Instakill Speedrunners", ChatColor.GRAY + "or remove one piece of armor each hit."))));
        newConfigCreatorsSett.add(new ConfigCreator("CompassTracking" ).configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Point Speedrunners at your",ChatColor.GRAY +"compass in the overworld"))));
        newConfigCreatorsSett.add(new ConfigCreator("GiveCompass").configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Give Hunters a compass at", ChatColor.GRAY + "the start of the game"))));
        newConfigCreatorsSett.add(new ConfigCreator("CompassParticleToSpeedrunner").configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Show Particle where the Speedrunners are.", ChatColor.GRAY + "Only works with compass in hand"))));
        newConfigCreatorsSett.add(new ConfigCreator("FreezeAssassin").configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Freeze Assassins of",ChatColor.GRAY +"the Gears of Speedrunners."))));
        newConfigCreatorsSett.add(new ConfigCreator("BossbarCompass").configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Give Hunters a compass in the",ChatColor.GRAY  + "boss bar where the Speedrunners ","", ChatColor.DARK_RED + "! ALPHA TEST !"))));
        newConfigCreatorsSett.add(new ConfigCreator("ShowAdvancement").configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Show whether a player has", ChatColor.GRAY + "received an advancement"))));
        newConfigCreatorsSett.add(new ConfigCreator("CompassAutoUpdate").configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Compass updates automatically"))));
        newConfigCreatorsSett.add(new ConfigCreator("CompassTriggerTimer", 5, 300,15).configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Timer when Hunters can ",ChatColor.GRAY +"Update there Compass."))));
        newConfigCreatorsSett.add(new ConfigCreator("SpeedrunnerOpportunity", 1, 99,40 ).configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Percentage chance to ",ChatColor.GRAY  + "become a speedrunner."))));
        newConfigCreatorsSett.add(new ConfigCreator("SpawnPlayerLeaveZombie").configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Spawn a Player Zombie when ", ChatColor.GRAY + "a player leaves the game."))));
        newConfigCreatorsSett.add(new ConfigCreator("ReadyStartTime", 5, 120,15).configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Ready time until the game starts."))));
        newConfigCreatorsSett.add(new ConfigCreator("GameResetTime", 2, 100, 8 ).configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Time in hours when the game ",ChatColor.GRAY +"will reset itself."))));
        newConfigCreatorsSett.add(new ConfigCreator("MaxPlayerSize", 2, 100, 10).configCreator(plugin)
                .setLore(new ArrayList<>( Arrays.asList("", ChatColor.GRAY + "Max Player Size",ChatColor.GRAY +"for this game"))));
        Bukkit.getLogger().info(ManHuntPlugin.getprefix() +"Config was loaded successfully");
        plugin.saveConfig();
        configCreatorsSett = newConfigCreatorsSett.stream().sorted(Comparator.comparing(ConfigCreator::getConfigName)).collect(Collectors.toCollection(LinkedHashSet::new));
    }


    public LinkedHashSet<ConfigCreator> getConfigCreatorsSett() {
        return configCreatorsSett;
    }

    public ConfigCreator getConfigCreators(String ConfigName){
        Optional<ConfigCreator> configCreator = getConfigCreatorsSett().stream().filter(config -> config.getConfigName().equalsIgnoreCase(ConfigName)).findFirst();
        return configCreator.orElse(null);
    }

}
