package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.utilis.anvilgui.AnvilGUI;
import de.shiirroo.manhunt.event.menu.*;
import de.shiirroo.manhunt.event.menu.menus.ConfigMenu;
import de.shiirroo.manhunt.event.menu.menus.PlayerMenu;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ConfigManHunt extends SubCommand {

    private static Plugin plugin;
    private static Config config;

    public ConfigManHunt(Plugin plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }


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
        return "/ManHunt config [config name] True | False";
    }

    @Override
    public Boolean getNeedOp() {
        return true;
    }

    public static CommandBuilder configCommand;

    @Override
    public CommandBuilder getSubCommandsArgs(String[] args)  {
        configCommand = new CommandBuilder("config", getNeedOp());
        for(String entry : config.getConfig().keySet()) {
            if (!entry.equalsIgnoreCase("BossbarCompass")){
                CommandBuilder configSetting = new CommandBuilder(entry);
                configSetting.addSubCommandBuilder(new CommandBuilder("True"));
                configSetting.addSubCommandBuilder(new CommandBuilder("False"));
                configCommand.addSubCommandBuilder(configSetting);
            }
        }
        configCommand.addSubCommandBuilder(new CommandBuilder("HuntStartTime"));
        configCommand.addSubCommandBuilder(new CommandBuilder("SpeedrunnerOpportunity"));
        configCommand.addSubCommandBuilder(new CommandBuilder("GameResetTime"));
        configCommand.addSubCommandBuilder(new CommandBuilder("CompassTriggerTimer"));
        configCommand.addSubCommandBuilder(new CommandBuilder("VoteStartTime"));
        return configCommand;
    }



    @Override
    public void perform(Player player, String[] args) {
        if(!player.isOp()){ player.sendMessage(config.getprefix() + ChatColor.RED + "I´m sorry, but you don´t have permission to perform this command");return;}
        if(args.length == 1){
            player.sendMessage(config.getprefix() + getDescription());
        }
        else if(args.length == 2){
            if(args[1].equalsIgnoreCase("huntStartTime")) {
                huntStartTimeGUI(player);
            } else if(args[1].equalsIgnoreCase("GameResetTime")) {
                gameResetTime(player);
            } else if(args[1].equalsIgnoreCase("CompassTriggerTimer")) {
                compassTriggerTimer(player);
            } else if(args[1].equalsIgnoreCase("SpeedrunnerOpportunity")) {
                speedrunnerOpportunityGUI(player);
            } else if(args[1].equalsIgnoreCase("VoteStartTime")) {
                voteStartTime(player);
            } else {
                getConfigCommands(player, args[1]);
            }
        }
        else if(args.length > 3) {
            player.sendMessage(config.getprefix() + getDescription());
        }
         else{
            changeBoolConfig(player, args);
        }
    }


    private void changeBoolConfig(Player player, String[] args){
        for(String entry : config.getConfig().keySet()){
            if(args[1].equalsIgnoreCase(entry)) {
                if(plugin.getConfig().getBoolean(entry)){
                    if(args[2].equalsIgnoreCase("False")){
                        plugin.getConfig().set(entry, false);
                        plugin.saveConfig();
                        if(entry.equalsIgnoreCase("ShowAdvancement")){
                            ShowAdvancement(false);
                        }
                        config.reload();
                        player.sendMessage(config.getprefix()+ChatColor.GOLD+ entry.substring(0, 1).toUpperCase() + entry.substring(1) + ChatColor.GRAY+" switched to" +ChatColor.RED+ " False");
                    } else{
                        player.sendMessage(config.getprefix()+ChatColor.GOLD+ entry.substring(0, 1).toUpperCase() + entry.substring(1) + ChatColor.GRAY+" is already" +ChatColor.GREEN+ " True");
                    }
                    return;
                } else if(!plugin.getConfig().getBoolean(entry)){
                    if(args[2].equalsIgnoreCase("True")){
                        plugin.getConfig().set(entry, true);
                        plugin.saveConfig();
                        if(entry.equalsIgnoreCase("ShowAdvancement")){
                            ShowAdvancement(true);
                        }
                        config.reload();
                        player.sendMessage(config.getprefix()+ChatColor.GOLD+ entry.substring(0, 1).toUpperCase() + entry.substring(1) + ChatColor.GRAY+" switched to" +ChatColor.GREEN+ " True");

                    } else{
                        player.sendMessage(config.getprefix()+ChatColor.GOLD+ entry.substring(0, 1).toUpperCase() + entry.substring(1) + ChatColor.GRAY+" is already"+ChatColor.RED+ " False");
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
        for(String entry : config.getConfig().keySet()){
            if(args.equalsIgnoreCase(entry)) {
                String s = Objects.requireNonNull(plugin.getConfig().getString(entry)).substring(0, 1).toUpperCase() + Objects.requireNonNull(plugin.getConfig().getString(entry)).substring(1);

                if(plugin.getConfig().getBoolean(entry)){
                    player.sendMessage(config.getprefix()+ChatColor.GOLD+ entry.substring(0, 1).toUpperCase() + entry.substring(1) + ChatColor.GRAY+" : " +ChatColor.GREEN+ s);
                    return;
                } else if(!plugin.getConfig().getBoolean(entry)){
                    player.sendMessage(config.getprefix()+ChatColor.GOLD+ entry.substring(0, 1).toUpperCase() + entry.substring(1) + ChatColor.GRAY+" : " +ChatColor.RED+ s);
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


    public static void ConfigMenu(Player p){
        try {
            MenuManager.openMenu(ConfigMenu.class, p, null);
        } catch (MenuManagerException e) {
            e.printStackTrace();
        } catch (MenuManagerNotSetupException e) {
            e.printStackTrace();
        }
    }
    public static void voteStartTime(Player player){
        AnvilGUI(player, "Vote Time:", "VoteStartTime", 3, 120, "s");
    }

    public static void compassTriggerTimer(Player player){
        AnvilGUI(player, "TriggerTime:", "CompassTriggerTimer", 3, 300, "s");
    }

    public static void gameResetTime(Player player){
        AnvilGUI(player, "Reset Time:", "GameResetTime", 2, 100, "h");
    }

    public static void huntStartTimeGUI(Player player){
        AnvilGUI(player, "Hunt time:", "HuntStartTime", 3, 999, "s");
    }

    public static void speedrunnerOpportunityGUI(Player player) {
        AnvilGUI(player, "Opportunity:", "SpeedrunnerOpportunity", 1, 99, "%");
    }

    public static void AnvilGUI(Player player,String DisplayText, String ConfigValue, Integer lowestValue,Integer highestValue, String addon){
            new AnvilGUI.Builder()
                    .onComplete((p, text) -> {
                        text = text.replace(DisplayText + " ", "");
                        if(isNumeric(text)){
                            Integer input = Integer.parseInt(text);
                            if(input >= lowestValue && input <= highestValue) {
                                plugin.getConfig().set(ConfigValue, input);
                                plugin.saveConfig();
                                config.reload();
                                p.sendMessage(config.getprefix() + ChatColor.GOLD + ConfigValue + ChatColor.GRAY + " switched to" + " "  + ChatColor.GREEN + input +" "+ ChatColor.GRAY + addon);
                                ConfigMenu(p);
                                for(UUID uuid : PlayerMenu.ConfigMenu.keySet()){
                                    PlayerMenu.ConfigMenu.get(uuid).setMenuItems();
                                }
                                return AnvilGUI.Response.openInventory(PlayerMenu.ConfigMenu.get(p.getUniqueId()).getInventory());
                            }
                            p.sendMessage(config.getprefix() + ChatColor.RED+"This is an invalid input." + ChatColor.GRAY + " Enter a number between " + ChatColor.GOLD + lowestValue + ChatColor.GRAY + " - " + ChatColor.GOLD + highestValue);
                        } else {
                            p.sendMessage(config.getprefix() + ChatColor.RED+"This is an invalid input");
                        }
                        return AnvilGUI.Response.text(ChatColor.GRAY +  DisplayText +" " +ChatColor.GREEN+ plugin.getConfig().getInt(ConfigValue));
                    })
                    .text(ChatColor.GRAY + DisplayText +  " " +ChatColor.GREEN+ plugin.getConfig().getInt(ConfigValue))
                    .itemLeft(new ItemStack(Material.CLOCK))
                    .onLeftInputClick((p) -> ConfigMenu(p))
                    .title(ChatColor.DARK_GRAY + DisplayText+ " " +ChatColor.DARK_PURPLE+ lowestValue +ChatColor.DARK_GRAY+ " - "+ ChatColor.DARK_PURPLE + highestValue + " " +  addon)
                    .plugin(plugin)
                    .open(player);
    }
}



