package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
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
        ArrayList description = new ArrayList<>();
        for(ManHuntRole m : ManHuntRole.values()){
            if(m.equals(ManHuntRole.Unassigned)) continue;
            description.add(m);
        }
        return description;
    }

    @Override
    public void perform(Player player, String[] args) {
        if (ManHuntPlugin.getGameData().getGameStatus().isGame()) {
            player.sendMessage(ManHuntPlugin.getprefix() + "Can´t change group while running match");
        } else {
            List<String> list = Stream.of(ManHuntRole.values()).map(ManHuntRole::toString).collect(Collectors.toList());
            if (args.length == 1) {
                player.sendMessage(ManHuntPlugin.getprefix() + getDescription());
            } else if (list.contains(args[1])) {
                joinGroup(player, ManHuntRole.valueOf(args[1]));
            } else {
                player.sendMessage(ManHuntPlugin.getprefix() + "This was not found: " + ChatColor.GOLD + args[1]);
            }
        }
    }



    public static boolean joinGroup(Player player, ManHuntRole manHuntRole){
        ManHuntRole mHR = ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(player.getUniqueId());
        if(mHR != null && !mHR.equals(ManHuntRole.Unassigned)) {
            if(!mHR.equals(manHuntRole)) {
                ManHuntPlugin.getGameData().getPlayerData().setRole(player, manHuntRole,ManHuntPlugin.getTeamManager());
                player.sendMessage(ManHuntPlugin.getprefix() + "You left the group " + ChatColor.GOLD + mHR + ChatColor.GRAY + " and joined the group: " + ChatColor.GOLD + manHuntRole);
                TeamChat.leaveChat(player);
                return true;
            } else {
                ManHuntPlugin.getGameData().getPlayerData().reset(player,ManHuntPlugin.getTeamManager());
                ManHuntPlugin.getGameData().getPlayerData().setRole(player, ManHuntRole.Unassigned,ManHuntPlugin.getTeamManager());
                player.sendMessage(ManHuntPlugin.getprefix() + "You left the group: " + ChatColor.GOLD + manHuntRole);
                TeamChat.leaveChat(player);
                return false;

            }

        } else {
            ManHuntPlugin.getGameData().getPlayerData().setRole(player, manHuntRole,ManHuntPlugin.getTeamManager());
            player.sendMessage(ManHuntPlugin.getprefix() + "You have joined the group: " + ChatColor.GOLD + manHuntRole);
            return true;

        }
    }
}



