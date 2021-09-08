package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Reload extends SubCommand {

    private final Plugin plugin;
    private final TeamManager teamManager;
    private final PlayerData playerData;
    private final Config config;

    public Reload(Plugin plugin, TeamManager teamManager, PlayerData playerData, Config config) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.playerData = playerData;
        this.config = config;
    }


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
        return "/ManHunt reload";
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
            p.sendMessage(config.getprefix() + ChatColor.RED + "I´m sorry, but you don´t have permission to perform this command");
            return;
        }
        config.reload();
        p.sendMessage(config.getprefix() + "Config reloaded");


    }
}



