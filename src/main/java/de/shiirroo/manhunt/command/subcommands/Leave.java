package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Leave extends SubCommand {

    private final TeamManager teamManager;
    private final PlayerData playerData;
    private final Config config;

    public Leave(TeamManager teamManager, PlayerData playerData, Config config) {
        this.teamManager = teamManager;
        this.playerData = playerData;
        this.config = config;
    }

    @Override
    public String getName() {
        return "Leave";
    }

    @Override
    public String getDescription() {
        return "Leave Group";
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Leave";
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
        if(playerData.getPlayerRole(player) == null){
            player.sendMessage(config.getprefix() + "You are not in a group");
        } else if(StartGame.gameRunning == null){
            if (!playerData.getPlayerRole(player).equals(ManHuntRole.Unassigned)) {
                player.sendMessage(config.getprefix() + "You left the group: " + ChatColor.GOLD + playerData.getPlayerRole(player));
                playerData.setRole(player, ManHuntRole.Unassigned, teamManager);
            }
            else {
                player.sendMessage(config.getprefix() + "You can´t leave this group");
            }
        } else {
            player.sendMessage(config.getprefix() + "You cannot leave the group during a game");
        }
    }

}



