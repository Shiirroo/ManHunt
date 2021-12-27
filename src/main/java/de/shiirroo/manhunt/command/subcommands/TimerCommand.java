package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TimerCommand extends SubCommand {

    @Override
    public String getName() {
        return "Timer";
    }

    @Override
    public String getDescription() {
        return "Displays the playing time.";
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Timer";
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
        if(args.length == 1){
            if(ManHuntPlugin.getGameData().getGamePlayer().getPlayerShowGameTimer().contains(player.getUniqueId())){
                ManHuntPlugin.getGameData().getGamePlayer().getPlayerShowGameTimer().remove(player.getUniqueId());
                player.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED +  "You are no longer showing any playing time.");
            } else {
                ManHuntPlugin.getGameData().getGamePlayer().getPlayerShowGameTimer().add(player.getUniqueId());
                player.sendMessage(ManHuntPlugin.getprefix() + ChatColor.GREEN + "You are now showing the playing time.");
            }
        } else {
            player.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED + "Command could not be executed like this");
        }
    }
}
