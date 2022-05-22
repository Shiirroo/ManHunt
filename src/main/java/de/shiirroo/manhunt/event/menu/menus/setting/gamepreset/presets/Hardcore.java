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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class Hardcore extends GamePreset implements Serializable {
    @Override
    public String getSpeedRunnersMaxSize() {
        return "1";
    }

    @Override
    public String getAssassinMaxSize() {
        return "ထ";
    }

    @Override
    public String getHunterMaxSize() {
        return "0";
    }

    @Override
    public ItemStack displayItem() {
        ItemStack playHead = new ItemStack(Material.WITHER_SKELETON_SKULL);
        SkullMeta im = (SkullMeta) playHead.getItemMeta();
        im.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Hardcore");
        List<String> loreString = Lists.newArrayList("", ChatColor.GOLD + "➤ " + ChatColor.GRAY + "Play " + ChatColor.DARK_RED + "Hardcore" + ChatColor.GRAY + " ManHunt mode.",
                "", ChatColor.YELLOW + "● " + ChatColor.GOLD + getSpeedRunnersMaxSize() + "x " + ManHuntRole.Speedrunner.getChatColor() + ManHuntRole.Speedrunner,
                ChatColor.YELLOW + "● " + ChatColor.GOLD + getAssassinMaxSize() + "x " + ManHuntRole.Assassin.getChatColor() + ManHuntRole.Assassin, ChatColor.YELLOW
                        + "● " + ChatColor.GOLD + getHunterMaxSize() + "x " + ManHuntRole.Hunter.getChatColor() + ManHuntRole.Hunter,
                "");
        makeConfig().forEach((s, o) -> loreString.add(
                (ChatColor.YELLOW + "➢ " + ChatColor.GOLD + s + " : " + (o instanceof Boolean ? ((Boolean) o ? ChatColor.GREEN + o.toString().substring(0, 1).toUpperCase() + o.toString().substring(1) : ChatColor.RED + o.toString().substring(0, 1).toUpperCase() + o.toString().substring(1)) : ChatColor.GREEN + o.toString()))
        ));
        loreString.add(" ");
        loreString.add(GamePresetMenu.preset.presetName().equalsIgnoreCase(this.getClass().getName()) ? ChatColor.GREEN + "" + ChatColor.BOLD + "⇨ Selected Preset" : ChatColor.DARK_GRAY + "⇨ Select Preset");
        im.setLore(loreString);
        playHead.setItemMeta(im);
        return playHead;
    }

    @Override
    public int getSpeedRunnerSize() {
        return 1;
    }

    @Override
    public boolean setPlayersGroup() {
        if (ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().anyMatch(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)) && Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1
        ) {
            if (ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Hunter).size() > Integer.parseInt(getHunterMaxSize()))
                return false;
            while (ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() < getSpeedRunnerSize()) {
                int speedrunnerPlayerID = Utilis.generateRandomInt((int) ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)).count());
                Player SpeedrunnerPlayer = Bukkit.getPlayer(ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)).toList().get(speedrunnerPlayerID));
                if (SpeedrunnerPlayer != null)
                    ManHuntPlugin.getGameData().getPlayerData().setRole(SpeedrunnerPlayer, ManHuntRole.Speedrunner, ManHuntPlugin.getTeamManager());
            }
            if (ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().anyMatch(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)))
                ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(uuid -> !Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)).forEach(uuid -> ManHuntPlugin.getGameData().getPlayerData().setRole(Objects.requireNonNull(Bukkit.getPlayer(uuid)), ManHuntRole.Assassin, ManHuntPlugin.getTeamManager()));
            return true;
        }
        return Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1 && ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() == 1 && ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(ManHuntRole.Hunter).stream().noneMatch(uuid -> (!Objects.requireNonNull(Bukkit.getPlayer(uuid)).getGameMode().equals(GameMode.SPECTATOR)));
    }

    @Override
    public HashMap<String, Object> makeConfig() {
        HashMap<String, Object> defaultConfig = new LinkedHashMap<>();
        defaultConfig.put("HuntStartTime", 30);
        defaultConfig.put("AssassinsInstaKill", true);
        defaultConfig.put("CompassTracking", true);
        defaultConfig.put("GiveCompass", true);
        defaultConfig.put("CompassParticleToSpeedrunner", true);
        defaultConfig.put("FreezeAssassin", false);
        defaultConfig.put("ShowAdvancement", true);
        defaultConfig.put("CompassAutoUpdate", true);
        defaultConfig.put("SpawnPlayerLeaveZombie", true);
        defaultConfig.put("ReadyStartTime", 15);
        return defaultConfig;
    }
}
