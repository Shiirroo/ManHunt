package de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.presets;

import com.google.common.collect.Lists;
import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePreset;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePresetMenu;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Utilis;
import de.shiirroo.manhunt.utilis.config.Config;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.C;

import java.util.*;
import java.util.stream.Collectors;

public class Custom extends GamePreset {


    @Override
    public String getSpeedRunnersMaxSize() {
        return "ထ";
    }

    @Override
    public String getAssassinMaxSize() {
        return "ထ";
    }

    @Override
    public String getHunterMaxSize() {
        return "ထ";
    }

    @Override
    public ItemStack displayItem() {
        ItemStack itemStack = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta im = itemStack.getItemMeta();
        im.displayName(Component.text(ChatColor.YELLOW +""+ ChatColor.BOLD + ChatColor.UNDERLINE+ "Custom"));
        List<String> loreString = Lists.newArrayList("", ChatColor.GOLD +  "➤ "+ChatColor.GRAY +  "Play "+ ChatColor.GRAY + "Your"+ChatColor.GRAY +" ManHunt mode.",
                "",ChatColor.YELLOW +  "● " + ChatColor.GOLD + getSpeedRunnersMaxSize() + "x " +ManHuntRole.Speedrunner.getChatColor() + ManHuntRole.Speedrunner,
                ChatColor.YELLOW +  "● " + ChatColor.GOLD + getAssassinMaxSize()+"x " + ManHuntRole.Assassin.getChatColor() + ManHuntRole.Assassin,ChatColor.YELLOW
                        +  "● " + ChatColor.GOLD + getHunterMaxSize() +"x " + ManHuntRole.Hunter.getChatColor() + ManHuntRole.Hunter,
                "");
        String s = GamePresetMenu.checkCustom()? ChatColor.BLUE +""+ ChatColor.BOLD+ "⇨ Reload Preset" :ChatColor.GREEN +""+ ChatColor.BOLD+ "⇨ Selected Preset";

        loreString.add(GamePresetMenu.preset.presetName().equalsIgnoreCase(this.getClass().getName())? s : ChatColor.DARK_GRAY + "⇨ Select Preset");

        im.lore(Utilis.lore(loreString));
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(im);
        return itemStack;
    }

    @Override
    public int getSpeedRunnerSize() {
        long Opportunity = (Config.getSpeedrunnerOpportunity()/100)* Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count();
        if(Opportunity <= 1)
            return 0;
        return (int)Math.floor(Opportunity);
    }


    public boolean setPlayersGroup() {
        if (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().anyMatch(p -> !p.getGameMode().equals(GameMode.SPECTATOR)) && Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1) {
            while (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() <= getSpeedRunnerSize()) {
                int speedrunnerPlayerID = Utilis.generateRandomInt((int) ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).count());
                Player SpeedrunnerPlayer = ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).collect(Collectors.toList()).get(speedrunnerPlayerID);
                ManHuntPlugin.getPlayerData().setRole(SpeedrunnerPlayer, ManHuntRole.Speedrunner, ManHuntPlugin.getTeamManager());
            }
            while (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().anyMatch(p -> !p.getGameMode().equals(GameMode.SPECTATOR))) {
                int speedrunnerPlayerID = Utilis.generateRandomInt((int) ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).count());
                Player SpeedrunnerPlayer = ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).collect(Collectors.toList()).get(speedrunnerPlayerID);
                ManHuntPlugin.getPlayerData().setRole(SpeedrunnerPlayer, Utilis.generateRandomInt(2) == 0 ? ManHuntRole.Hunter : ManHuntRole.Assassin, ManHuntPlugin.getTeamManager());
            }
            return true;
        }
        if (Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1 && ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() >= 1) {
            return true;
        }
        return false;
    }

    @Override
    public HashMap<String, Object> makeConfig(){
        if(GamePresetMenu.customHashMap == null){
            GamePresetMenu.customHashMap = new LinkedHashMap<>();
            GamePresetMenu.customHashMap.put("HuntStartTime", Config.getHuntStartTime());
            GamePresetMenu.customHashMap.put("AssassinsInstaKill", Config.getAssassinsInstaKill());
            GamePresetMenu.customHashMap.put("CompassTracking", Config.getCompassTracking());
            GamePresetMenu.customHashMap.put("GiveCompass", Config.getGiveCompass());
            GamePresetMenu.customHashMap.put("CompassParticleToSpeedrunner", Config.getCompassParticleToSpeedrunner());
            GamePresetMenu.customHashMap.put("FreezeAssassin", Config.getFreezeAssassin());
            GamePresetMenu.customHashMap.put("ShowAdvancement", Config.getShowAdvancement());
            GamePresetMenu.customHashMap.put("CompassAutoUpdate", Config.getCompassAutoUpdate());
            GamePresetMenu.customHashMap.put("CompassTriggerTimer", Config.getCompassTriggerTimer());
            GamePresetMenu.customHashMap.put("SpeedrunnerOpportunity", Config.getSpeedrunnerOpportunity());
            GamePresetMenu.customHashMap.put("SpawnPlayerLeaveZombie", Config.getSpawnPlayerLeaveZombie());
            GamePresetMenu.customHashMap.put("ReadyStartTime", Config.getReadyStartTime());
        }
        return GamePresetMenu.customHashMap;
    }
}


