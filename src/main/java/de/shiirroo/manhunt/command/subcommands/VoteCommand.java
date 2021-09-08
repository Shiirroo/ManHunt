package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class VoteCommand extends SubCommand {

    private final TeamManager teamManager;
    private final PlayerData playerData;
    private final Config config;
    private final Plugin plugin;

    public VoteCommand(TeamManager teamManager, PlayerData playerData, Config config, Plugin plugin) {
        this.teamManager = teamManager;
        this.playerData = playerData;
        this.config = config;
        this.plugin = plugin;
    }


    @Override
    public String getName() {
        return "Vote";
    }

    @Override
    public String getDescription() {
        return "Vote for the available vote";
    }

    @Override
    public String getSyntax() {
        return "/MahHunt Vote";
    }

    @Override
    public Boolean getNeedOp() {
        return false;
    }

    @Override
    public CommandBuilder getSubCommandsArgs(String[] args) {
        CommandBuilder cm = new CommandBuilder("Vote");
        CommandBuilder create = new CommandBuilder("Create", true);
        cm.addSubCommandBuilder(create);
        return cm;

    }

    @Override
    public void perform(Player player, String[] args) throws IOException, InterruptedException, MenuManagerException, MenuManagerNotSetupException {
    }

}