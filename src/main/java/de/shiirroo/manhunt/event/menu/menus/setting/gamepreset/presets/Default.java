package de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.presets;

import com.google.common.collect.Lists;
import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePreset;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePresetMenu;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import de.shiirroo.manhunt.utilis.Utilis;
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

public class Default extends GamePreset implements Serializable {


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
        ItemStack itemStack = new ItemStack(Material.BEDROCK, 1);
        ItemMeta im = itemStack.getItemMeta();
        im.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Default");
        List<String> loreString = Lists.newArrayList("", ChatColor.GOLD + "➤ " + ChatColor.GRAY + "Play " + ChatColor.DARK_GRAY + "Default" + ChatColor.GRAY + " classic ManHunt mode.",
                "", ChatColor.YELLOW + "● " + ChatColor.GOLD + (Integer.parseInt(getSpeedRunnersMaxSize())) + "x " + ManHuntRole.Speedrunner.getChatColor() + ManHuntRole.Speedrunner,
                ChatColor.YELLOW + "● " + ChatColor.GOLD + getAssassinMaxSize() + "x " + ManHuntRole.Assassin.getChatColor() + ManHuntRole.Assassin, ChatColor.YELLOW
                        + "● " + ChatColor.GOLD + getHunterMaxSize() + "x " + ManHuntRole.Hunter.getChatColor() + ManHuntRole.Hunter,
                "");

        makeConfig().forEach((s, o) -> loreString.add(
                (ChatColor.YELLOW + "➢ " + ChatColor.GOLD + s + " : " + (o instanceof Boolean ? ((Boolean) o ? ChatColor.GREEN + o.toString().substring(0, 1).toUpperCase() + o.toString().substring(1) : ChatColor.RED + o.toString().substring(0, 1).toUpperCase() + o.toString().substring(1)) : ChatColor.GREEN + o.toString()))
        ));
        loreString.add(" ");
        loreString.add(GamePresetMenu.preset.presetName().equalsIgnoreCase(this.getClass().getName()) ? ChatColor.GREEN + "" + ChatColor.BOLD + "⇨ Selected Preset" : ChatColor.DARK_GRAY + "⇨ Select Preset");
        im.setLore(loreString);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(im);
        return itemStack;
    }

    @Override
    public int getSpeedRunnerSize() {
        long Opportunity = ((((int) makeConfig().get("SpeedrunnerOpportunity")) / 100L) * Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count());
        if (Opportunity <= 1)
            return 1;
        return Math.round(Opportunity) + 1;
    }


    public boolean setPlayersGroup() {
        if (ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().anyMatch(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)) && Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1) {
            while (ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() <= getSpeedRunnerSize()) {
                int speedrunnerPlayerID = Utilis.generateRandomInt((int) ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)).count());
                Player SpeedrunnerPlayer = Bukkit.getPlayer(ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)).toList().get(speedrunnerPlayerID));
                if (SpeedrunnerPlayer != null)
                    ManHuntPlugin.getGameData().getPlayerData().setRole(SpeedrunnerPlayer, ManHuntRole.Speedrunner, ManHuntPlugin.getTeamManager());
            }
            while (ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().anyMatch(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR))) {
                int hunterPlayerID = Utilis.generateRandomInt((int) ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)).count());
                Player hunterPlayer = Bukkit.getPlayer(ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)).toList().get(hunterPlayerID));
                if (hunterPlayer != null)
                    ManHuntPlugin.getGameData().getPlayerData().setRole(hunterPlayer, Utilis.generateRandomInt(2) == 0 ? ManHuntRole.Hunter : ManHuntRole.Assassin, ManHuntPlugin.getTeamManager());
            }
            return true;
        }
        return Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1 && ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() >= 1;
    }

    @Override
    public HashMap<String, Object> makeConfig() {
        HashMap<String, Object> defaultConfig = new LinkedHashMap<>();
        defaultConfig.put("HuntStartTime", 120);
        defaultConfig.put("AssassinsInstaKill", false);
        defaultConfig.put("CompassTracking", true);
        defaultConfig.put("GiveCompass", true);
        defaultConfig.put("CompassParticleToSpeedrunner", false);
        defaultConfig.put("FreezeAssassin", true);
        defaultConfig.put("ShowAdvancement", true);
        defaultConfig.put("CompassAutoUpdate", false);
        defaultConfig.put("CompassTriggerTimer", 15);
        defaultConfig.put("SpeedrunnerOpportunity", 40);
        defaultConfig.put("SpawnPlayerLeaveZombie", false);
        defaultConfig.put("ReadyStartTime", 15);
        return defaultConfig;
    }
}
