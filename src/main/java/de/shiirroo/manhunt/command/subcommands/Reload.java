package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Reload extends SubCommand {


    @Override
    public String getName() {
        return "Reload";
    }

    @Override
    public String getDescription() {
        return "Reload config files";
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Reload";
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
    public void perform(Player p, String[] args) {
        if (!p.isOp()) {
            p.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED + "I´m sorry, but you don´t have permission to perform this command");
            return;
        }
        Config.relodConfig();
        p.sendMessage(ManHuntPlugin.getprefix() + "Config reloaded");
        System.out.println(ManHuntPlugin.getprefix() + ChatColor.GRAY + "Config was reloaded.");


    }
}



