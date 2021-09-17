package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.player.onAsyncPlayerChatEvent;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeamChat extends SubCommand {

    public static Set<UUID> teamchat = new HashSet<>();


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
        CommandBuilder cm = new CommandBuilder("TeamChat");
        cm.addSubCommandBuilder(new CommandBuilder("Custom"));
        return cm;

    }

    @Override
    public void perform(Player player, String[] args) throws IOException, InterruptedException, MenuManagerException, MenuManagerNotSetupException {
        if (args.length == 1 && !ManHuntPlugin.getPlayerData().getPlayerRole(player).equals(ManHuntRole.Unassigned)) {
            if(leaveChat(player)){
                player.sendMessage(Component.text(ManHuntPlugin.getprefix() + "You have left the team chat"));
            } else {
                teamchat.add(player.getUniqueId());
                player.sendMessage(Component.text(ManHuntPlugin.getprefix() + "You're joining the team chat"));
            }
        } else if(args.length > 1 && args[0].equalsIgnoreCase("TeamChat") && !ManHuntPlugin.getPlayerData().getPlayerRole(player).equals(ManHuntRole.Unassigned)){
            Component displayname = player.displayName();
            String messageString = null;
            for(String string : args){
                if(!string.equalsIgnoreCase("TeamChat")){
                    if(messageString == null)
                        messageString = string;
                    else
                        messageString =  messageString + " "+ string;
                }

            }
            assert messageString != null;
            Component message = Component.text(messageString).color(TextColor.fromHexString("#AAAAAA"));
            onAsyncPlayerChatEvent.sendTeamChatMessage(player, displayname, message);
        }
    }

    public static boolean leaveChat(Player player){
        if(TeamChat.teamchat.contains(player.getUniqueId())){
            TeamChat.teamchat.remove(player.getUniqueId());
            return true;
        }
        return false;
    }
}