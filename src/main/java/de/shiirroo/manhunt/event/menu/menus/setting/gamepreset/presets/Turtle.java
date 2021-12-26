package de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.presets;

import com.google.common.collect.Lists;
import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePreset;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePresetMenu;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Utilis;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Turtle extends GamePreset implements Serializable {

    @Override
    public String getSpeedRunnersMaxSize() {
        return String.valueOf(getSpeedRunnerSize());
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
        ItemStack itemStack = new ItemStack(Material.TURTLE_EGG, 1);
        ItemMeta im = itemStack.getItemMeta();
        im.displayName(Component.text(ChatColor.AQUA +""+ ChatColor.BOLD + ChatColor.UNDERLINE+ "Turtle"));
        List<String> loreString = Lists.newArrayList("", ChatColor.GOLD +  "➤ "+ChatColor.GRAY +  "Play "+ ChatColor.AQUA + "Turtle"+ChatColor.GRAY +" ManHunt mode.",
                "",ChatColor.YELLOW +  "● " + ChatColor.GOLD + "min. "+ (Integer.parseInt(getSpeedRunnersMaxSize()) + 1) + " " + ManHuntRole.Speedrunner.getChatColor() + ManHuntRole.Speedrunner,
                ChatColor.YELLOW +  "● " + ChatColor.GOLD + getAssassinMaxSize()+"x " + ManHuntRole.Assassin.getChatColor() + ManHuntRole.Assassin,ChatColor.YELLOW
                        +  "● " + ChatColor.GOLD + getHunterMaxSize() +"x " + ManHuntRole.Hunter.getChatColor() + ManHuntRole.Hunter,
                "");

        makeConfig().forEach((s,o) ->  loreString.add(
                (ChatColor.YELLOW + "➢ " + ChatColor.GOLD + s + " : " + (o instanceof Boolean? ((Boolean) o? ChatColor.GREEN + o.toString().substring(0, 1).toUpperCase() + o.toString().substring(1):ChatColor.RED + o.toString().substring(0, 1).toUpperCase() + o.toString().substring(1)): ChatColor.GREEN + o.toString()))
        ));
        loreString.add(" ");
        loreString.add(GamePresetMenu.preset.presetName().equalsIgnoreCase(this.getClass().getName())? ChatColor.GREEN +""+ ChatColor.BOLD+ "⇨ Selected Preset": ChatColor.DARK_GRAY + "⇨ Select Preset");
        im.lore(Utilis.lore(loreString));
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(im);
        return itemStack;
    }

    @Override
    public int getSpeedRunnerSize() {
        long Opportunity = (((int) makeConfig().get("SpeedrunnerOpportunity")) /100)* Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count();
        if(Opportunity <= 1)
            return 0;
        return (int)Math.floor(Opportunity);
    }


    public boolean setPlayersGroup() {
        if (ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().anyMatch(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)) && Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1) {
            while (ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() <= getSpeedRunnerSize()) {
                int speedrunnerPlayerID = Utilis.generateRandomInt((int) ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)).count());
                Player SpeedrunnerPlayer = Bukkit.getPlayer(ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)).collect(Collectors.toList()).get(speedrunnerPlayerID));
                if(SpeedrunnerPlayer != null){
                    ManHuntPlugin.getGameData().getPlayerData().setRole(SpeedrunnerPlayer, ManHuntRole.Speedrunner,ManHuntPlugin.getTeamManager());
                }
            }
            while (ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().anyMatch(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR))) {
                int hunterPlayerID = Utilis.generateRandomInt((int) ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)).count());
                Player SpeedrunnerPlayer = Bukkit.getPlayer(ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)).collect(Collectors.toList()).get(hunterPlayerID));
                if(SpeedrunnerPlayer != null) {
                    ManHuntPlugin.getGameData().getPlayerData().setRole(SpeedrunnerPlayer, Utilis.generateRandomInt(2) == 0 ? ManHuntRole.Hunter : ManHuntRole.Assassin,ManHuntPlugin.getTeamManager());
                }
            }
            return true;
        }
        return Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1 && ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() >= getSpeedRunnerSize();
    }

    @Override
    public HashMap<String, Object> makeConfig(){
        HashMap<String, Object> defaultConfig = new LinkedHashMap<>();
        defaultConfig.put("HuntStartTime", 200);
        defaultConfig.put("AssassinsInstaKill", false);
        defaultConfig.put("CompassTracking", true);
        defaultConfig.put("GiveCompass", false);
        defaultConfig.put("CompassParticleToSpeedrunner", false);
        defaultConfig.put("FreezeAssassin", true);
        defaultConfig.put("ShowAdvancement", false);
        defaultConfig.put("CompassAutoUpdate", false);
        defaultConfig.put("CompassTriggerTimer", 30);
        defaultConfig.put("SpeedrunnerOpportunity", 60);
        defaultConfig.put("SpawnPlayerLeaveZombie", false);
        defaultConfig.put("ReadyStartTime", 30);
        return defaultConfig;
    }
}
