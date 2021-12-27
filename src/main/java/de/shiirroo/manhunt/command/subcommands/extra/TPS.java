package de.shiirroo.manhunt.command.subcommands.extra;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TPS extends SubCommand {

    @Override
    public String getName() {
        return "TPS";
    }

    @Override
    public String getDescription() {
        return "Show minecraft TPS";
    }

    @Override
    public String getSyntax() {
        return "/ManHunt TPS";
    }

    @Override
    public Boolean getNeedOp() {
        return false;
    }


    @Override
    public CommandBuilder getSubCommandsArgs(String[] args)  {
        return null;
    }


    @Override
    public void perform(Player player, String[] args) {
        player.sendMessage(ManHuntPlugin.getprefix() + ChatColor.GRAY + "TPS from last : [ "+ChatColor.GOLD+"1m"+ChatColor.GRAY+" | "+ChatColor.GOLD+"5m"+ChatColor.GRAY+" | "+ChatColor.GOLD+"15m"+ChatColor.GRAY+" ] : " + goodValue(Bukkit.getTPS()[0])  + ChatColor.GRAY+ " | " + goodValue(Bukkit.getTPS()[1]) + ChatColor.GRAY  + " | " + goodValue(Bukkit.getTPS()[2]));
    }


    public String goodValue(double value){
        if(value >=15) return ""+ChatColor.GREEN + (int) value;
        else if(value >=8) return ""+ChatColor.YELLOW + (int) value;
        else return ""+ChatColor.RED + (int) value;
    }
}
