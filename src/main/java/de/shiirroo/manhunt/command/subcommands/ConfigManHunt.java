package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.event.menu.MenuManager;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.menus.ConfigMenu;
import de.shiirroo.manhunt.event.menu.menus.SettingsMenu;
import de.shiirroo.manhunt.utilis.config.ConfigCreator;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
        for(ConfigCreator configCreator : ManHuntPlugin.getConfigCreatorsSett()) {
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
        if(!player.isOp()){ player.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED + "I´m sorry, but you don´t have permission to perform this command");return;}
        if(args.length == 1){
            player.sendMessage(ManHuntPlugin.getprefix() + getDescription());
        }
        else if(args.length == 2){
            ConfigCreator configCreator = ManHuntPlugin.getConfigCreators(args[1]);

            if(configCreator != null && configCreator.getConfigSetting() instanceof Integer){
                AnvilGUISetup(player,configCreator);
            }
            else {
                getConfigCommands(player, args[1]);
            }
        }
        else if(args.length > 3) {
            player.sendMessage(ManHuntPlugin.getprefix() + getDescription());
        }
         else{
            changeBoolConfig(player, args);
        }
    }


    private void changeBoolConfig(Player player, String[] args){
        for(ConfigCreator configCreator : ManHuntPlugin.getConfigCreatorsSett()){
            if(args[1].equalsIgnoreCase(configCreator.getConfigName()) && configCreator.getConfigSetting() instanceof Boolean ) {
                if(configCreator.getConfigSetting().equals(true)){
                    if(args[2].equalsIgnoreCase("False")){
                        configCreator.setConfigSetting(false);
                        if(configCreator.getConfigName().equalsIgnoreCase("ShowAdvancement")){
                            ShowAdvancement(false);
                        }
                        player.sendMessage(ManHuntPlugin.getprefix()+ChatColor.GOLD+ configCreator.getConfigName().substring(0, 1).toUpperCase() + configCreator.getConfigName().substring(1) + ChatColor.GRAY+" switched to" +ChatColor.RED+ " False");
                    } else{
                        player.sendMessage(ManHuntPlugin.getprefix()+ChatColor.GOLD+ configCreator.getConfigName().substring(0, 1).toUpperCase() + configCreator.getConfigName().substring(1) + ChatColor.GRAY+" is already" +ChatColor.GREEN+ " True");
                    }
                    return;
                } else
                    if(configCreator.getConfigSetting().equals(false)){
                        if(args[2].equalsIgnoreCase("True")){
                            configCreator.setConfigSetting(true);
                            if(configCreator.getConfigName().equalsIgnoreCase("ShowAdvancement")){
                                ShowAdvancement(true);
                            }
                            player.sendMessage(ManHuntPlugin.getprefix()+ChatColor.GOLD+ configCreator.getConfigName().substring(0, 1).toUpperCase() + configCreator.getConfigName().substring(1) + ChatColor.GRAY+" switched to" +ChatColor.GREEN+ " True");
                    } else{
                        player.sendMessage(ManHuntPlugin.getprefix()+ChatColor.GOLD+ configCreator.getConfigName().substring(0, 1).toUpperCase() + configCreator.getConfigName().substring(1) + ChatColor.GRAY+" is already"+ChatColor.RED+ " False");
                    }
                    return;
                }
            }
        }




    }

    private void ShowAdvancement(Boolean bool){
        for(World w : Bukkit.getWorlds()){
            w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, bool);
        }
    }

    private void getConfigCommands(Player player, String args){
        for(ConfigCreator configCreator : ManHuntPlugin.getConfigCreatorsSett()) {
            if(args.equalsIgnoreCase(configCreator.getConfigName()) && configCreator.getConfigSetting() instanceof Boolean) {
                String s = Objects.requireNonNull(String.valueOf(configCreator.getConfigSetting()).substring(0, 1).toUpperCase() + String.valueOf(configCreator.getConfigSetting()).substring(1).toLowerCase());

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

    public static boolean isNumeric(String strNum) {
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }


    public static void ConfigMenu(Player p) {
        try {
            MenuManager.openMenu(ConfigMenu.class, p, null);
        } catch (MenuManagerException e) {
        } catch (MenuManagerNotSetupException e) {
        }
    }


    public static void AnvilGUISetup(Player player, ConfigCreator configCreator){
        String DisplayText = new String();
        String addon = new String();
        switch (configCreator.getConfigName()){
            case "ReadyStartTime":
                DisplayText = "Ready Time:";
                addon = "s";
                break;
            case "CompassTriggerTimer":
                DisplayText = "TriggerTime:";
                addon = "s";
                break;
            case "GameResetTime":
                DisplayText = "Reset Time:";
                addon = "h";
                break;
            case "HuntStartTime":
                DisplayText = "Hunt time:";
                addon = "s";
                break;
            case "SpeedrunnerOpportunity":
                DisplayText = "Opportunity:";
                addon = "%";
                break;

        }


        AnvilGUI(player, DisplayText, configCreator.getConfigName(), configCreator.getMin(), configCreator.getMax(), addon);
    }


    public static void AnvilGUI(Player player,String DisplayText, String ConfigValue, Integer lowestValue,Integer highestValue, String addon){
       new AnvilGUI.Builder()
                    .onComplete((p, text) -> {
                        text = text.replace(DisplayText + " ", "");
                        if(isNumeric(text)){
                            Integer input = Integer.parseInt(text);
                            if(input >= lowestValue && input <= highestValue) {
                                ConfigCreator configCreator = ManHuntPlugin.getConfigCreators(ConfigValue);
                                if(configCreator != null) {
                                    configCreator.setConfigSetting(input);
                                    p.sendMessage(ManHuntPlugin.getprefix() + ChatColor.GOLD + ConfigValue + ChatColor.GRAY + " switched to" + " " + ChatColor.GREEN + input + " " + ChatColor.GRAY + addon);
                                    for (UUID uuid : SettingsMenu.ConfigMenu.keySet()) {
                                        SettingsMenu.ConfigMenu.get(uuid).setMenuItems();
                                    }
                                }
                                if(Ready.ready != null && ConfigValue.equalsIgnoreCase("ReadyStartTime")){
                                    Ready.ready.getbossBarCreator().setTime(input);
                                }

                                if(SettingsMenu.ConfigMenu != null && SettingsMenu.ConfigMenu.get(p.getUniqueId()) != null){
                                    SettingsMenu.ConfigMenu.get(p.getUniqueId()).open("");
                                }
                                return AnvilGUI.Response.close();
                            }
                            p.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED+"This is an invalid input." + ChatColor.GRAY + " Enter a number between " + ChatColor.GOLD + lowestValue + ChatColor.GRAY + " - " + ChatColor.GOLD + highestValue);
                        } else {
                            p.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED+"This is an invalid input");
                        }
                        return AnvilGUI.Response.text(ChatColor.GRAY +  DisplayText +" " +ChatColor.GREEN+ ManHuntPlugin.getConfigCreators(ConfigValue).getConfigSetting());
                    })
                    .text(ChatColor.GRAY + DisplayText +  " " +ChatColor.GREEN+ ManHuntPlugin.getConfigCreators(ConfigValue).getConfigSetting())
                    .itemLeft(new ItemStack(Material.CLOCK))
                    .title(ChatColor.DARK_GRAY + DisplayText+ " " +ChatColor.DARK_PURPLE+ lowestValue +ChatColor.DARK_GRAY+ " - "+ ChatColor.DARK_PURPLE + highestValue + " " +  addon)
                    .plugin(ManHuntPlugin.getPlugin())
                    .open(player);


    }
}



