package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.player.onAsyncPlayerChatEvent;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
public class TeamChat extends SubCommand {

    @Override
    public String getName() {
        return "TeamChat";
    }

    @Override
    public String getDescription() {
        return "Switch chat to TeamChat or send message in TeamChat";
    }

    @Override
    public String getSyntax() {
        return "/MahHunt TeamChat or TeamChat [Message]";
    }

    @Override
    public Boolean getNeedOp() {
        return false;
    }

    @Override
    public CommandBuilder getSubCommandsArgs(String[] args) {
        return new CommandBuilder("TeamChat").setCustomInput();
    }

    @Override
    public void perform(Player player, String[] args) throws IOException, InterruptedException, MenuManagerException, MenuManagerNotSetupException {
        if (args.length == 1) {
            if(leaveChat(player)){
                player.sendMessage(ManHuntPlugin.getprefix() + "You have left the team chat");
            } else {
                ManHuntPlugin.getGameData().getGamePlayer().getTeamchat().add(player.getUniqueId());
                player.sendMessage(ManHuntPlugin.getprefix() + "You're joining the team chat");
            }
        } else if(args.length > 1 && args[0].equalsIgnoreCase("TeamChat") && !ManHuntPlugin.getGameData().getPlayerData().getPlayerRoleByUUID(player.getUniqueId()).equals(ManHuntRole.Unassigned)){
            String displayname = player.getName();
            StringBuilder messageString = null;
            for(String string : args){
                if(!string.equalsIgnoreCase("TeamChat")){
                    if(messageString == null)
                        messageString = new StringBuilder(string);
                    else
                        messageString.append(" ").append(string);
                }

            }
            assert messageString != null;
            String message = ChatColor.GRAY + messageString.toString();
            onAsyncPlayerChatEvent.sendTeamChatMessage(player, displayname, message);
        }
    }

    public static boolean leaveChat(Player player){
        if(ManHuntPlugin.getGameData().getGamePlayer().getTeamchat().contains(player.getUniqueId())){
            ManHuntPlugin.getGameData().getGamePlayer().getTeamchat().remove(player.getUniqueId());
            return true;
        }
        return false;
    }
}