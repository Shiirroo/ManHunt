package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class Show extends SubCommand {

    @Override
    public String getName() {
        return "Show";
    }

    @Override
    public String getDescription() {
        return "Show Players in Group " + new ArrayList<>(Arrays.asList(ManHuntRole.values()));
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Show or Show [Groupname]";
    }

    @Override
    public Boolean getNeedOp() {
        return false;
    }


    @Override
    public CommandBuilder getSubCommandsArgs(String[] args) {
        CommandBuilder show = new CommandBuilder(getName());
        for (ManHuntRole m : ManHuntRole.values()) {
            show.addSubCommandBuilder(new CommandBuilder(m.toString()));
        }
        return show;
    }


    @Override
    public void perform(Player player, String[] args) {
        if (args.length == 1) {
            for (ManHuntRole manHuntRole : ManHuntRole.values()) {
                StringBuilder players = new StringBuilder();
                List<UUID> groupplayers = ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(manHuntRole);
                sendShowMessage(player, manHuntRole, players, groupplayers);
            }
        } else if (args.length == 2) {
            {
                ManHuntRole role = ManHuntRole.valueOf(args[1]);
                StringBuilder players = new StringBuilder();
                List<UUID> groupplayers = ManHuntPlugin.getGameData().getPlayerData().getPlayersByRole(role);
                sendShowMessage(player, role, players, groupplayers);
            }
        }
    }

    public void sendShowMessage(Player player, ManHuntRole manHuntRole, StringBuilder players, List<UUID> groupplayers) {
        if (groupplayers.size() != 0) {
            for (UUID uuid : groupplayers) {
                if (uuid != null) {
                    players.append(Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName()).append(" ");
                }
            }

            player.sendMessage(ManHuntPlugin.getprefix() + ChatColor.GOLD + manHuntRole + ChatColor.GRAY + " [" + ChatColor.GREEN + groupplayers.size() + ChatColor.GRAY + "] | " + ChatColor.GRAY + players);
        } else {
            player.sendMessage(ManHuntPlugin.getprefix() + ChatColor.GOLD + manHuntRole + ChatColor.GRAY + " [" + ChatColor.GREEN + 0 + ChatColor.GRAY + "] | " + ChatColor.GRAY + "Emtpy");
        }

    }
}



