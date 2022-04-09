package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Reset extends SubCommand {

    @Override
    public String getName() {
        return "Reset";
    }

    @Override
    public String getDescription() {
        return "Reset the World";
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Reset";
    }

    @Override
    public Boolean getNeedOp() {
        return true;
    }


    @Override
    public CommandBuilder getSubCommandsArgs(String[] args) {
        return null;
    }


    @Override
    public void perform(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED + "I´m sorry, but you don´t have permission to perform this command");
            return;
        }
        Bukkit.setWhitelist(true);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer(ManHuntPlugin.getprefix() + "World is Resetting..");
        }
        ManHuntPlugin.getPlugin().getConfig().set("isReset", true);
        ManHuntPlugin.getPlugin().saveConfig();
        Bukkit.spigot().restart();
    }
}



