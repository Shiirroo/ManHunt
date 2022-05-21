package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.player.onAsyncPlayerChatEvent;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class TeamChat extends SubCommand {

    @Override
    public String getName() {
        return "teamchat";
    }

    @Override
    public String getDescription() {
        return "Switch chat to TeamChat or send message in TeamChat";
    }

    @Override
    public String getSyntax() {
        return "/manhunt teamchat or teamchat [Message]";
    }

    @Override
    public Boolean getNeedOp() {
        return false;
    }

    @Override
    public CommandBuilder getSubCommandsArgs(String[] args) {
        return new CommandBuilder("teamchat").setCustomInput();
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length == 1) {
            if (leaveChat(player)) {
                player.sendMessage(ManHuntPlugin.getprefix() + "You have left the team chat");
            } else {
                ManHuntPlugin.getGameData().getGamePlayer().getTeamchat().add(player.getUniqueId());
                player.sendMessage(ManHuntPlugin.getprefix() + "You're joining the team chat");
            }
        } else if (args.length > 1 && args[0].equalsIgnoreCase("Teamchat")) {
                String displayname = player.getDisplayName();
                StringBuilder messageString = null;
                for (String string : args) {
                    if (!string.equalsIgnoreCase("Teamchat")) {
                        if (messageString == null)
                            messageString = new StringBuilder(string);
                        else
                            messageString.append(" ").append(string);
                    }

                }
                assert messageString != null;
                String message = ChatColor.GRAY + messageString.toString();
                if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                    for(Player onlineplayer : Bukkit.getOnlinePlayers().stream().filter(p -> p.getGameMode().equals(GameMode.SPECTATOR)).toList()){
                        onlineplayer.sendMessage(displayname + ChatColor.GRAY + " [" + ChatColor.AQUA + "SC" + ChatColor.GRAY + "]" + ChatColor.GOLD + " >>> " + message);
                    }
                } else {
                    if(!ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(player.getUniqueId()).equals(ManHuntRole.Unassigned)) {
                        onAsyncPlayerChatEvent.sendTeamChatMessage(player, displayname, message);
                    } else {
                        player.sendMessage(ManHuntPlugin.getprefix() + "You need a group to send a message in TeamChat.");
                    }
            }
        }
    }

    public static boolean leaveChat(Player player) {
        if (ManHuntPlugin.getGameData().getGamePlayer().getTeamchat().contains(player.getUniqueId())) {
            ManHuntPlugin.getGameData().getGamePlayer().getTeamchat().remove(player.getUniqueId());
            return true;
        }
        return false;
    }

}