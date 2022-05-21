package de.shiirroo.manhunt.command.subcommands.extra;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.utilis.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Reload extends SubCommand {


    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "reload config files";
    }

    @Override
    public String getSyntax() {
        return "/manhunt reload";
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
        Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.GRAY + "Config was reloaded.");
    }
}



