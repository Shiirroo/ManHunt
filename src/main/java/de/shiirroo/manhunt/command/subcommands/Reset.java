package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Reset extends SubCommand {

    private final Plugin plugin;
    private final TeamManager teamManager;
    private final PlayerData playerData;
    private final Config config;

    public Reset(Plugin plugin, TeamManager teamManager, PlayerData playerData, Config config) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.playerData = playerData;
        this.config = config;
    }


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
    public CommandBuilder getSubCommandsArgs(String[] args)  {
        return null;
    }


    @Override
    public void perform(Player player, String[] args) {
        if(!player.isOp()){ player.sendMessage(config.getprefix() + ChatColor.RED + "I´m sorry, but you don´t have permission to perform this command");return;}
        Bukkit.setWhitelist(true);
        for(Player p : Bukkit.getOnlinePlayers()){
            p.kick(Component.text(config.getprefix() + "World is Resetting.."));
        }
        plugin.getConfig().set("isReset", true);
        plugin.saveConfig();
        Bukkit.spigot().restart();
        return;

    }

}



