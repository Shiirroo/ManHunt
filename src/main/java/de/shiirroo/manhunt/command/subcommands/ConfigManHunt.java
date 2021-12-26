package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.menus.setting.SettingsMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePresetMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.presets.Custom;
import de.shiirroo.manhunt.utilis.Utilis;
import de.shiirroo.manhunt.utilis.config.ConfigCreator;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.UUID;

public class ConfigManHunt extends SubCommand {


    @Override
    public String getName() {
        return "Config";
    }

    @Override
    public String getDescription() {
        return "Toggle config on or off";
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Config [name]";
    }

    @Override
    public Boolean getNeedOp() {
        return true;
    }

    public static CommandBuilder configCommand;

    @Override
    public CommandBuilder getSubCommandsArgs(String[] args)  {
        configCommand = new CommandBuilder("config", getNeedOp());
        for(ConfigCreator configCreator : ManHuntPlugin.getGameData().getGameConfig().getConfigCreatorsSett()) {
            if (!configCreator.getConfigName().equalsIgnoreCase("BossbarCompass")){
                CommandBuilder configSetting = new CommandBuilder(configCreator.getConfigName());
                if(configCreator.getConfigSetting() instanceof Boolean){
                configSetting.addSubCommandBuilder(new CommandBuilder("True"));
                configSetting.addSubCommandBuilder(new CommandBuilder("False"));
                }
                configCommand.addSubCommandBuilder(configSetting);
            }
        }
        return configCommand;
    }



    @Override
    public void perform(Player player, String[] args) {
        if(!player.isOp()){
            player.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED + "I´m sorry, but you don´t have permission to perform this command");
        } else if(args.length == 1){
            player.sendMessage(ManHuntPlugin.getprefix() + getDescription());
        } else if(args.length == 2 && !ManHuntPlugin.getGameData().getGameStatus().isGame()){
            ConfigCreator configCreator = ManHuntPlugin.getGameData().getGameConfig().getConfigCreators(args[1]);
            if(configCreator != null && configCreator.getConfigSetting() instanceof Integer){
                AnvilGUISetup(player,configCreator);
            }
            else {
                getConfigCommands(player, args[1]);
            }
        } else if(args.length > 3) {
            player.sendMessage(ManHuntPlugin.getprefix() + getDescription());
        } else if(!ManHuntPlugin.getGameData().getGameStatus().isGame()){
            changeBoolConfig(player, args);
        }
    }


    private void changeBoolConfig(Player player, String[] args){
        Optional<ConfigCreator> configCreator = ManHuntPlugin.getGameData().getGameConfig().getConfigCreatorsSett().stream().filter(c -> args[1].equalsIgnoreCase(c.getConfigName())).findFirst();
        if(configCreator.isPresent()){
            ConfigCreator creator = configCreator.get();
            if(creator.getConfigSetting() instanceof Boolean && args[2].equalsIgnoreCase("True") || args[2].equalsIgnoreCase("False")) {
                Boolean b = Boolean.valueOf(args[2].toLowerCase());
                Bukkit.getLogger().info(ManHuntPlugin.getprefix() +creator.getConfigName() + " " + b +" "+ args[2] );
                if(creator.getConfigSetting().equals(b)){
                    player.sendMessage(ManHuntPlugin.getprefix()+ChatColor.GOLD+ creator.getConfigName().substring(0, 1).toUpperCase() + creator.getConfigName().substring(1) + ChatColor.GRAY+" is already : " + (b? ChatColor.GREEN: ChatColor.RED) + args[2].substring(0, 1).toUpperCase() + args[2].substring(1));
                } else {
                    resetPreset(player);
                    creator.setConfigSetting(b, ManHuntPlugin.getPlugin());
                    if(GamePresetMenu.preset.presetName().equalsIgnoreCase(new Custom().presetName()) && GamePresetMenu.customHashMap != null){
                        GamePresetMenu.customHashMap.put(creator.getConfigName(),b);
                    }

                    if(creator.getConfigName().equalsIgnoreCase("ShowAdvancement")){
                        ShowAdvancement(b);
                    }

                    SettingsMenu.ConfigMenu.values().forEach(Menu::setMenuItems);

                    player.sendMessage(ManHuntPlugin.getprefix()+ChatColor.GOLD+ creator.getConfigName().substring(0, 1).toUpperCase() + creator.getConfigName().substring(1) + ChatColor.GRAY+" switched to : "+ (b? ChatColor.GREEN: ChatColor.RED) + args[2].substring(0, 1).toUpperCase() + args[2].substring(1));
                }
            }
        }
    }



    public static void resetPreset(Player player){
        if(!GamePresetMenu.preset.presetName().equalsIgnoreCase(new Custom().presetName())) {
            Bukkit.getLogger().info(ManHuntPlugin.getprefix() + "Game preset : Custom");
            player.sendMessage(Component.text(ManHuntPlugin.getprefix() + "You changed a configuration, the game preset was changed back to Custom"));
            GamePresetMenu.preset = new Custom();
            GamePresetMenu.setFooderPreset(player);
            for(Menu menu : SettingsMenu.GamePreset.values()) menu.setMenuItems();
        }
    }

    private void ShowAdvancement(Boolean bool){
        for(World w : Bukkit.getWorlds()){
            w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, bool);
        }
    }

    private void getConfigCommands(Player player, String args){
        for(ConfigCreator configCreator : ManHuntPlugin.getGameData().getGameConfig().getConfigCreatorsSett()) {
            if(args.equalsIgnoreCase(configCreator.getConfigName()) && configCreator.getConfigSetting() instanceof Boolean) {
                String s = String.valueOf(configCreator.getConfigSetting()).substring(0, 1).toUpperCase() + String.valueOf(configCreator.getConfigSetting()).substring(1).toLowerCase();

                if(configCreator.getConfigSetting().equals(true)){
                    player.sendMessage(ManHuntPlugin.getprefix()+ChatColor.GOLD+ configCreator.getConfigName() + ChatColor.GRAY+" : " +ChatColor.GREEN+ s);
                    return;
                } else if(configCreator.getConfigSetting().equals(false)){
                    player.sendMessage(ManHuntPlugin.getprefix()+ChatColor.GOLD+ configCreator.getConfigName() + ChatColor.GRAY+" : " +ChatColor.RED+ s);
                    return;

                }
            }
        }
    }

    public static void AnvilGUISetup(Player player, ConfigCreator configCreator){
        String DisplayText = "";
        String addon = "";
        switch (configCreator.getConfigName()) {
            case "ReadyStartTime" -> {
                DisplayText = "Ready Time:";
                addon = "s";
            }
            case "CompassTriggerTimer" -> {
                DisplayText = "TriggerTime:";
                addon = "s";
            }
            case "GameResetTime" -> {
                DisplayText = "Reset Time:";
                addon = "h";
            }
            case "HuntStartTime" -> {
                DisplayText = "Hunt time:";
                addon = "s";
            }
            case "SpeedrunnerOpportunity" -> {
                DisplayText = "Opportunity:";
                addon = "%";
            }
        }


        AnvilGUI(player, DisplayText, configCreator.getConfigName(), configCreator.getMin(), configCreator.getMax(), addon);
    }


    public static void AnvilGUI(Player player,String DisplayText, String ConfigValue, Integer lowestValue,Integer highestValue, String addon){
       new AnvilGUI.Builder()
                    .onComplete((p, text) -> {
                        text = text.replace(DisplayText + " ", "");
                        if(Utilis.isNumeric(text)){
                            Integer input = Integer.parseInt(text);
                            if(input >= lowestValue && input <= highestValue) {
                                ConfigCreator configCreator = ManHuntPlugin.getGameData().getGameConfig().getConfigCreators(ConfigValue);
                                if(configCreator != null) {
                                    configCreator.setConfigSetting(input, ManHuntPlugin.getPlugin());
                                    p.sendMessage(ManHuntPlugin.getprefix() + ChatColor.GOLD + ConfigValue + ChatColor.GRAY + " switched to" + " " + ChatColor.GREEN + input + " " + ChatColor.GRAY + addon);
                                    for (UUID uuid : SettingsMenu.ConfigMenu.keySet()) {
                                        SettingsMenu.ConfigMenu.get(uuid).setMenuItems();
                                    }
                                }
                                if(ConfigValue.equalsIgnoreCase("HuntStartTime")) StartGame.bossBarGameStart.setTime(input);
                                if(ConfigValue.equalsIgnoreCase("ReadyStartTime")) Ready.ready.getbossBarCreator().setTime(input);
                                if(!ConfigValue.equalsIgnoreCase("GameResetTime")) {
                                    if (GamePresetMenu.preset.presetName().equalsIgnoreCase(new Custom().presetName()) && GamePresetMenu.customHashMap != null) {
                                        GamePresetMenu.customHashMap.put(ConfigValue, input);
                                    }
                                    resetPreset(p);
                                }
                                if(SettingsMenu.ConfigMenu != null && SettingsMenu.ConfigMenu.get(p.getUniqueId()) != null) SettingsMenu.ConfigMenu.get(p.getUniqueId()).open();
                                return AnvilGUI.Response.close();
                            }
                            p.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED+"This is an invalid input." + ChatColor.GRAY + " Enter a number between " + ChatColor.GOLD + lowestValue + ChatColor.GRAY + " - " + ChatColor.GOLD + highestValue);
                        } else {
                            p.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED+"This is an invalid input");
                        }
                        return AnvilGUI.Response.text(ChatColor.GRAY +  DisplayText +" " +ChatColor.GREEN+ ManHuntPlugin.getGameData().getGameConfig().getConfigCreators(ConfigValue).getConfigSetting());
                    })
                    .text(ChatColor.GRAY + DisplayText +  " " +ChatColor.GREEN+ ManHuntPlugin.getGameData().getGameConfig().getConfigCreators(ConfigValue).getConfigSetting())
                    .itemLeft(new ItemStack(Material.CLOCK))
                    .title(ChatColor.DARK_GRAY + DisplayText+ " " +ChatColor.DARK_PURPLE+ lowestValue +ChatColor.DARK_GRAY+ " - "+ ChatColor.DARK_PURPLE + highestValue + " " +  addon)
                    .plugin(ManHuntPlugin.getPlugin())
                    .open(player);
    }
}



