package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Show extends SubCommand {

    private final PlayerData playerData;
    private final Config config;

    public Show(PlayerData playerData, Config config) {
        this.playerData = playerData;
        this.config = config;
    }


    @Override
    public String getName() {
        return "Show";
    }

    @Override
    public String getDescription() {
        return "Show Players in Group "  + new ArrayList<>(Arrays.asList(ManHuntRole.values()));
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Show or /ManHunt Show [Groupname]";
    }

    @Override
    public Boolean getNeedOp() {
        return false;
    }


    @Override
    public CommandBuilder getSubCommandsArgs(String[] args) {
        CommandBuilder show = new CommandBuilder(getName());
        for(ManHuntRole m : ManHuntRole.values()){
            if(m.equals(ManHuntRole.Unassigned)) continue;
            show.addSubCommandBuilder(new CommandBuilder(m.toString()));
        }
        return show;
    }


    @Override
    public void perform(Player player, String[] args) throws MenuManagerException, MenuManagerNotSetupException {
        List<String> list = Stream.of(ManHuntRole.values()).map(ManHuntRole::toString).collect(Collectors.toList());
        if(args.length == 1){
            for (String s : list) {
                StringBuilder players = new StringBuilder();
                List<Player> groupplayers2 = playerData.getPlayersByRole(ManHuntRole.valueOf(s));
                if (groupplayers2.size() != 0) {
                    for (Player value : groupplayers2) {
                        players.append(value.getName()).append(" ");
                    }

                    player.sendMessage(config.getprefix() + ChatColor.GOLD + ManHuntRole.valueOf(s) + ChatColor.GRAY + " [" + ChatColor.GREEN + groupplayers2.size() + ChatColor.GRAY + "] | " + ChatColor.GRAY + players);
                } else {
                    player.sendMessage(config.getprefix() + ChatColor.GOLD + ManHuntRole.valueOf(s) + ChatColor.GRAY + " [" + ChatColor.GREEN + groupplayers2.size() + ChatColor.GRAY + "] | " + ChatColor.GRAY + "Emtpy");
                }
            }
        }
    }

}



