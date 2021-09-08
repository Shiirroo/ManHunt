package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.utilis.Worker;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.menu.MenuManager;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.menus.PlayerMenu;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class StopGame extends SubCommand {

    private final Config config;
    private final PlayerData playerData;
    private final TeamManager teamManager;

    public StopGame(Config config, PlayerData playerData, TeamManager teamManager) {
        this.config = config;
        this.playerData = playerData;
        this.teamManager = teamManager;
    }


    @Override
    public String getName() {
        return "Stop";
    }

    @Override
    public String getDescription() {
        return "Stop an active run";
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Stop";
    }

    @Override
    public Boolean getNeedOp() {
        return true;
    }

    @Override
    public CommandBuilder getSubCommandsArgs(String[] args){
        return null;
    }

    @Override
    public void perform(Player player, String[] args) throws MenuManagerException, MenuManagerNotSetupException {
        if(!player.isOp()){ player.sendMessage(config.getprefix() + ChatColor.RED + "I´m sorry, but you don´t have permission to perform this command");return;}
        if(StartGame.gameRunning != null) {
            Events.gameStartTime = null;
            StartGame.gameRunning.cancel();
            Worker.reminderTime = 1;
            Ready.setReadyVote();

            for(Player Gameplayer : Bukkit.getOnlinePlayers()){
                Gameplayer.setGameMode(GameMode.ADVENTURE);
                Gameplayer.getInventory().clear();
                Gameplayer.setHealth(20);
                MenuManager.openMenu(PlayerMenu.class, Gameplayer.getPlayer(), null);
                Gameplayer.setWhitelisted(false);
                Gameplayer.teleport(Bukkit.getWorld("world").getSpawnLocation());
                playerData.reset(Gameplayer, teamManager);
                playerData.setRole(Gameplayer, ManHuntRole.Unassigned, teamManager);
            }
            for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()){
                offlinePlayer.setWhitelisted(false);
            }
            Bukkit.getServer().setWhitelist(false);

            World world = Bukkit.getServer().getWorld("world");
            if(world != null){
                world.setPVP(false);
                world.setDifficulty(Difficulty.PEACEFUL);
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                world.setTime(0);
            }
            player.sendMessage(config.getprefix() + "Game stopped and reset");
            StartGame.gameRunning = null;

        }


    }

}


