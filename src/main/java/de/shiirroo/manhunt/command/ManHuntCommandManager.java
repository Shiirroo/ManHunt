package de.shiirroo.manhunt.command;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.*;
import de.shiirroo.manhunt.command.subcommands.extra.Help;
import de.shiirroo.manhunt.command.subcommands.extra.Reload;
import de.shiirroo.manhunt.command.subcommands.extra.TPS;
import de.shiirroo.manhunt.command.subcommands.vote.VoteCommand;
import de.shiirroo.manhunt.utilis.Utilis;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ManHuntCommandManager implements TabExecutor {

    public static final List<String> tomanyargs = new ArrayList<>();
    public static final List<String> Commandnotfound = new ArrayList<>();
    private static ArrayList<SubCommand> subcommands;

    public ManHuntCommandManager() {
        subcommands = new ArrayList<>();
        getSubCommands().add(new Join());
        getSubCommands().add(new Leave());
        getSubCommands().add(new Show());
        getSubCommands().add(new Reload());
        getSubCommands().add(new StartGame());
        getSubCommands().add(new Reset());
        getSubCommands().add(new StopGame());
        getSubCommands().add(new Ready());
        getSubCommands().add(new VoteCommand());
        getSubCommands().add(new ConfigManHunt());
        getSubCommands().add(new TeamChat());
        getSubCommands().add(new TimerCommand());
        getSubCommands().add(new Save());
        getSubCommands().add(new TPS());

        getSubCommands().add(new Help(getSubCommands()));
        tomanyargs.add("❌❌❌");
        Commandnotfound.add("❌❌❌");

    }

    public static ArrayList<SubCommand> getSubCommands() {
        return subcommands;
    }

    public static List<String> getSubComanndsList(String[] args, CommandBuilder cmd, Boolean isOP) {
        List<String> commandList = new ArrayList<>();
        CommandBuilder cB = getSubCommandSearch(args, cmd, 0, isOP);
        if (cB != null) {
            if (cB.hasSubCommands())
                commandList = cB.getSubCommandListAsString(isOP);
            else
                commandList.add(cB.getCommandName());
        } else {
            commandList = tomanyargs;
        }
        return commandList;
    }

    public static CommandBuilder getSubCommandSearch(String[] args, CommandBuilder cmd, Integer run, Boolean isOP) {
        CommandBuilder command = null;
        if (args.length == 1) return cmd;
        else if (cmd.hasSubCommands()) {
            if (args.length == run) return null;
            CommandBuilder newMainCommand = cmd.getSubCommand(args[run], isOP);
            if (newMainCommand != null) {
                if (newMainCommand.isCustomInput()) return new CommandBuilder("customInput");
                if (args[run].equalsIgnoreCase(newMainCommand.getCommandName()))
                    command = getSubCommandSearch(args, newMainCommand, run + 1, isOP);
                else {
                    return null;
                }
            } else if (args[args.length - 2].equalsIgnoreCase(cmd.getCommandName()))
                command = cmd;
        }
        return command;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player p) {

            if (args.length == 0) {
                Help help = new Help(getSubCommands());
                help.perform(p, args);
            } else {
                for (SubCommand subCommand : getSubCommands()) {
                    if (args[0].equalsIgnoreCase(subCommand.getName())) {
                        subCommand.perform(p, args);
                        return true;
                    }
                }
                p.sendMessage(ManHuntPlugin.getprefix() + "Command not found!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        CommandBuilder manHunt = new CommandBuilder("ManHunt");

        for (SubCommand subCommand : getSubCommands()) {
            CommandBuilder sub = subCommand.getSubCommandsArgs(args);
            manHunt.addSubCommandBuilder(Objects.requireNonNullElseGet(sub, () -> new CommandBuilder(subCommand.getName(), subCommand.getNeedOp())));
        }

        List<String> list = getSubComanndsList(args, manHunt, sender.isOp());
        String input = args[args.length - 1].toLowerCase();
        List<String> completions = null;
        if (list == null) return Commandnotfound;
        if (list.size() == 1 && list.get(0).equalsIgnoreCase("customInput")) return new ArrayList<>();
        for (String s : list) {
            if (s.toLowerCase().startsWith(input)) {
                if (completions == null)
                    completions = new ArrayList<>();
                completions.add(s.substring(0, 1).toUpperCase() + s.substring(1));
            }
        }

        if (Utilis.isNumeric(input)) return null;
        if (completions == null) return Commandnotfound;
        Collections.sort(completions);
        return completions;
    }

}
