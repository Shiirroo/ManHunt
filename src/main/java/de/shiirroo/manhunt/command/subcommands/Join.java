package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Join extends SubCommand {

    private static TeamManager teamManager;
    private static PlayerData playerData;
    private static Config config;

    public Join(TeamManager teamManager, PlayerData playerData, Config config) {
        this.teamManager = teamManager;
        this.playerData = playerData;
        this.config = config;
    }


    @Override
    public String getName() {
        return "Join";
    }

    @Override
    public String getDescription() {
        return "You can join Group "  + setDescription();
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Join [Group name]";
    }

    @Override
    public Boolean getNeedOp() {
        return true;
    }


    @Override
    public CommandBuilder getSubCommandsArgs(String[] args) {
        CommandBuilder join = new CommandBuilder(getName());
        for(ManHuntRole m : ManHuntRole.values()){
            if(m.equals(ManHuntRole.Unassigned)) continue;
            join.addSubCommandBuilder(new CommandBuilder(m.toString()));
        }
        return join;
    }


    public ArrayList<String> setDescription(){
        ArrayList tes = new ArrayList<>();
        for(ManHuntRole m : ManHuntRole.values()){
            if(m.equals(ManHuntRole.Unassigned)) continue;
            tes.add(m);
        }
        return tes;
    };

    @Override
    public void perform(Player player, String[] args) {
        if(StartGame.gameRunning != null) { player.sendMessage(config.getprefix() + "Can´t change group while running match");return;}
        List<String> list = Stream.of(ManHuntRole.values()).map(ManHuntRole::toString).collect(Collectors.toList());
        if(args.length == 1){
            player.sendMessage(config.getprefix() + getDescription());
        } else if(list.contains(args[1]) && ManHuntRole.valueOf(args[1]) != null) {
            joinGroup(player,ManHuntRole.valueOf(args[1]));
        } else {
            player.sendMessage(config.getprefix() + "This was not found: " + ChatColor.GOLD + args[1]);
        }
    }



    public static boolean joinGroup(Player player, ManHuntRole manHuntRole){
        ManHuntRole mHR = playerData.getRole(player);
        if(mHR != null && !mHR.equals(ManHuntRole.Unassigned)) {
            if(!mHR.equals(manHuntRole)) {
                playerData.setRole(player, manHuntRole, teamManager);
                player.sendMessage(config.getprefix() + "You left the group " + ChatColor.GOLD + mHR + ChatColor.GRAY + " and joined the group: " + ChatColor.GOLD + manHuntRole);
                return true;
            } else {
                playerData.reset(player, teamManager);
                playerData.setRole(player, ManHuntRole.Unassigned, teamManager);
                player.sendMessage(config.getprefix() + "You left the group: " + ChatColor.GOLD + manHuntRole);
                return false;

            }

        } else {
            playerData.setRole(player, manHuntRole, teamManager);
            player.sendMessage(config.getprefix() + "You have joined the group: " + ChatColor.GOLD + manHuntRole);
            return true;

        }
    }
}



