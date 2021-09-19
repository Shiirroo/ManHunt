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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Dream extends GamePreset {

    @Override
    public String getSpeedRunnersMaxSize() {
        return "1";
    }

    @Override
    public String getAssassinMaxSize() {
        return "1";
    }
    @Override
    public String getHunterMaxSize() {
        return "ထ";
    }


    @Override
    public ItemStack displayItem() {
        ItemStack playHead = Utilis.getPlayHead();
        SkullMeta im = (SkullMeta) playHead.getItemMeta();
        im.displayName(Component.text(ChatColor.GREEN +""+ ChatColor.BOLD + ChatColor.UNDERLINE+ "DREAM"));
        im.setOwner("Dream");
        List<String> loreString = Lists.newArrayList("", ChatColor.GOLD +  "➤ "+ChatColor.GRAY +  "Play "+ ChatColor.GREEN + "Dream's"+ChatColor.GRAY +" classic ManHunt mode.",
                "",ChatColor.YELLOW +  "● " + ChatColor.GOLD + getSpeedRunnersMaxSize() +"x " + ManHuntRole.Speedrunner.getChatColor() + ManHuntRole.Speedrunner,
                ChatColor.YELLOW +  "● " + ChatColor.GOLD + getAssassinMaxSize()  +"x " + ManHuntRole.Assassin.getChatColor() + ManHuntRole.Assassin,ChatColor.YELLOW
                        +  "● " + ChatColor.GOLD + getHunterMaxSize()  +"x " + ManHuntRole.Hunter.getChatColor() + ManHuntRole.Hunter,
                "");
        makeConfig().forEach((s,o) ->  loreString.add(
                (ChatColor.YELLOW + "➢ " + ChatColor.GOLD + s + " : " + (o instanceof Boolean? ((Boolean) o? ChatColor.GREEN + o.toString().substring(0, 1).toUpperCase() + o.toString().substring(1):ChatColor.RED + o.toString().substring(0, 1).toUpperCase() + o.toString().substring(1)): ChatColor.GREEN + o.toString()))
        ));
        loreString.add(" ");
        loreString.add(GamePresetMenu.preset.presetName().equalsIgnoreCase(this.getClass().getName())? ChatColor.GREEN +""+ ChatColor.BOLD+ "⇨ Selected Preset": ChatColor.DARK_GRAY + "⇨ Select Preset");
        im.lore(Utilis.lore(loreString));
        playHead.setItemMeta(im);
        return playHead;
    }

    @Override
    public int getSpeedRunnerSize() {
        return 0;
    }

    @Override
    public boolean setPlayersGroup() {
            if (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().anyMatch(p -> !p.getGameMode().equals(GameMode.SPECTATOR)) && Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1)
            {
                while (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() <= getSpeedRunnerSize()) {
                    int speedrunnerPlayerID = Utilis.generateRandomInt((int) ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).count());
                    Player SpeedrunnerPlayer = ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).collect(Collectors.toList()).get(speedrunnerPlayerID);
                    ManHuntPlugin.getPlayerData().setRole(SpeedrunnerPlayer, ManHuntRole.Speedrunner, ManHuntPlugin.getTeamManager());
                }
                if(Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 2 && ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().anyMatch(p -> !p.getGameMode().equals(GameMode.SPECTATOR))){
                        int AssassinPlayerID = Utilis.generateRandomInt((int) ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).count());
                        Player AssassinPlayer = ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).collect(Collectors.toList()).get(AssassinPlayerID);
                        ManHuntPlugin.getPlayerData().setRole(AssassinPlayer, ManHuntRole.Assassin, ManHuntPlugin.getTeamManager());
                }
                if(ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().anyMatch(p -> !p.getGameMode().equals(GameMode.SPECTATOR)))
                    ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR)).forEach(player -> ManHuntPlugin.getPlayerData().setRole(player, ManHuntRole.Hunter, ManHuntPlugin.getTeamManager()));
                return true;
            }
        return Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() > 1 && ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() == 1 && ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Assassin).size() == 1;
    }

    @Override
    public HashMap<String, Object> makeConfig(){
        HashMap<String, Object> defaultConfig = new LinkedHashMap<>();
        defaultConfig.put("HuntStartTime", 120);
        defaultConfig.put("AssassinsInstaKill", true);
        defaultConfig.put("CompassTracking", true);
        defaultConfig.put("GiveCompass", true);
        defaultConfig.put("CompassParticleToSpeedrunner", false);
        defaultConfig.put("FreezeAssassin", true);
        defaultConfig.put("ShowAdvancement", true);
        defaultConfig.put("CompassAutoUpdate", false);
        defaultConfig.put("CompassTriggerTimer", 15);
        defaultConfig.put("SpawnPlayerLeaveZombie", false);
        defaultConfig.put("ReadyStartTime", 15);
        return defaultConfig;
    }
}
