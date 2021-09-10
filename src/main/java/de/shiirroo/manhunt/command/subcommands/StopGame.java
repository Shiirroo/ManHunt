package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
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
        if(!player.isOp()){ player.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED + "I´m sorry, but you don´t have permission to perform this command");return;}
        if(StartGame.gameRunning != null) {
            Events.gameStartTime = null;
            StartGame.gameRunning.cancel();
            Worker.reminderTime = 1;
            StartGame.gameRunning = null;
            Ready.setReadyVote();

            for(Player Gameplayer : Bukkit.getOnlinePlayers()){
                Gameplayer.setGameMode(GameMode.ADVENTURE);
                Gameplayer.getInventory().clear();
                Gameplayer.setHealth(20);
                Gameplayer.setExp(0);
                MenuManager.openMenu(PlayerMenu.class, Gameplayer.getPlayer(), null);
                Gameplayer.setWhitelisted(false);
                Gameplayer.teleport(Bukkit.getWorld("world").getSpawnLocation());
                ManHuntPlugin.getPlayerData().reset(Gameplayer, ManHuntPlugin.getTeamManager());
                ManHuntPlugin.getPlayerData().addUnassigned(Gameplayer, ManHuntPlugin.getTeamManager());
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
                world.getWorldBorder().setCenter(world.getSpawnLocation());
                world.getWorldBorder().setSize(20);
            }
            player.sendMessage(ManHuntPlugin.getprefix() + "Game stopped and reset");

        }


    }

}



