package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Help extends SubCommand {

    private static ArrayList<SubCommand> getSubCommands;

    public Help(ArrayList<SubCommand> getSubCommands) {
        Help.getSubCommands = getSubCommands;
    }


    @Override
    public String getName() {
        return "Help";
    }

    @Override
    public String getDescription() {
        return "It will show your information about the plugin";
    }

    @Override
    public String getSyntax() {
        return "/ManHunt";
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
    public void perform(Player p, String[] args) {
        p.sendMessage(ChatColor.DARK_GRAY +"---- Show Help Information about ManHunt ----");
        for (SubCommand getSubCommand : getSubCommands) {
            p.sendMessage(ChatColor.GOLD + getSubCommand.getSyntax() + " " + ChatColor.GRAY + getSubCommand.getDescription());
        }

    }


}
