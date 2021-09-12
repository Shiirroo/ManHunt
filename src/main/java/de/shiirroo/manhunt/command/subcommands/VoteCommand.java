package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.utilis.BossBarCreator;
import de.shiirroo.manhunt.utilis.Config;
import de.shiirroo.manhunt.teams.PlayerData;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.utilis.Vote;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VoteCommand extends SubCommand {

    public static Vote vote;
    public static Boolean pause = false;
    public static List<Long> pauseList = new ArrayList<>();
    public static List<Long> unPauseList = new ArrayList<>();




    @Override
    public String getName() {
        return "Vote";
    }

    @Override
    public String getDescription() {
        return "Vote for one of the available votes or create a new one";
    }

    @Override
    public String getSyntax() {
        return "/MahHunt Vote or Vote [Votename]";
    }

    @Override
    public Boolean getNeedOp() {
        return false;
    }

    @Override
    public CommandBuilder getSubCommandsArgs(String[] args) {
        CommandBuilder cm = new CommandBuilder("Vote");
        CommandBuilder create = new CommandBuilder("Create");
        create.addSubCommandBuilder(new CommandBuilder("Skip-Day"));
        create.addSubCommandBuilder(new CommandBuilder("Skip-Night"));
        create.addSubCommandBuilder(new CommandBuilder("Pause"));
        cm.addSubCommandBuilder(create);
        return cm;

    }

    @Override
    public void perform(Player player, String[] args) throws IOException, InterruptedException, MenuManagerException, MenuManagerNotSetupException {
        if(StartGame.gameRunning != null && Ready.ready == null && StartGame.gameRunning.isRunning() == false) {
            if (vote != null && args.length == 1) {
                vote.addVote(player);
            } else if (vote == null) {
                if (Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count()>= 2 && args.length == 3 && args[1].equalsIgnoreCase("Create")) {
                    switch (args[2].toLowerCase()) {
                        case "skip-night":
                            skipNight(player);
                            break;
                        case "skip-day":
                            skiptDay(player);
                            break;
                        case "pause":
                            setPause(player);
                            break;
                    }
                } else {
                    player.sendMessage(Component.text(ManHuntPlugin.getprefix() + "Currently no votes can be created"));
                }
            } else {
                player.sendMessage(Component.text(ManHuntPlugin.getprefix() + "There is already a vote in progress"));
            }
        }
    }


    public void skipNight(Player player) {
        if (Bukkit.getWorld("world").getTime() >= 13000L) {
            vote = new Vote(true, ManHuntPlugin.getPlugin(), ChatColor.GRAY + "Skipping Night " + ChatColor.GOLD + "VOTEPLAYERS " + ChatColor.BLACK + "| " + ChatColor.GOLD + "ONLINEPLAYERS" + ChatColor.GRAY + " [ "  + ChatColor.GREEN + "TIMER " + ChatColor.GRAY + "]" , 30);
            BossBarCreator bbc = vote.getbossBarCreator();
            bbc.onComplete(aBoolean -> {
                if (aBoolean) {
                    Bukkit.getWorld("world").setTime(1000L);
                }
                vote = null;
                ManHuntPlugin.getPlayerData().updatePlayers(ManHuntPlugin.getTeamManager());
                bbc.onShortlyComplete(aBoolean1 -> {
                    Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f));
                });
            });
            vote.startVote();
            vote.addVote(player);
        } else {
                player.sendMessage(Component.text(ManHuntPlugin.getprefix() + "You can only skip at night time for day time"));
        }
    }

    public void setPause(Player player) {
        if(pause){
            vote = new Vote(true, ManHuntPlugin.getPlugin(), ChatColor.GRAY + "Continue Game ? " + ChatColor.GOLD + "VOTEPLAYERS " + ChatColor.BLACK + "| " + ChatColor.GOLD + "ONLINEPLAYERS" + ChatColor.GRAY + " [ "  + ChatColor.GREEN + "TIMER " + ChatColor.GRAY + "]" , 30);
            BossBarCreator bbc = vote.getbossBarCreator();
            bbc.setHowManyPlayersinPercent(100);
            bbc.onComplete(aBoolean -> {
                if (aBoolean) {
                    pause = false;
                    unPauseList.add(Calendar.getInstance().getTime().getTime());
                }
                vote = null;
                ManHuntPlugin.getPlayerData().updatePlayers(ManHuntPlugin.getTeamManager());
                bbc.onShortlyComplete(aBoolean1 -> {
                    Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f));
                });
            });
            vote.startVote();
            vote.addVote(player);

        } else {
            vote = new Vote(true, ManHuntPlugin.getPlugin(), ChatColor.GRAY + "Pause Game ? " + ChatColor.GOLD + "VOTEPLAYERS " + ChatColor.BLACK + "| " + ChatColor.GOLD + "ONLINEPLAYERS" + ChatColor.GRAY + " [ "  + ChatColor.GREEN + "TIMER " + ChatColor.GRAY + "]" , 30);
            BossBarCreator bbc = vote.getbossBarCreator();
            bbc.setHowManyPlayersinPercent(75);
            bbc.onComplete(aBoolean -> {
                if (aBoolean) {
                    pause = true;
                    pauseList.add(Calendar.getInstance().getTime().getTime());
                }
                vote = null;
                ManHuntPlugin.getPlayerData().updatePlayers(ManHuntPlugin.getTeamManager());
                bbc.onShortlyComplete(aBoolean1 -> {
                    Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f));
                });
            });
            vote.startVote();
            vote.addVote(player);
        }
    }




    public void skiptDay(Player player) {
        if (Bukkit.getWorld("world").getTime() <= 13000L) {
            vote = new Vote(true, ManHuntPlugin.getPlugin(), ChatColor.GRAY + "Skipping Day " + ChatColor.GOLD + "VOTEPLAYERS " + ChatColor.BLACK + "| " + ChatColor.GOLD + "ONLINEPLAYERS" + ChatColor.GRAY + " [ "  + ChatColor.GREEN + "TIMER " + ChatColor.GRAY + "]" , 30);
            BossBarCreator bbc = vote.getbossBarCreator();
            bbc.onComplete(aBoolean -> {
                if (aBoolean) {
                    Bukkit.getWorld("world").setTime(13000L);
                }
                vote = null;
                ManHuntPlugin.getPlayerData().updatePlayers(ManHuntPlugin.getTeamManager());
                bbc.onShortlyComplete(aBoolean1 -> {
                    Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f));
                });
            });
            vote.startVote();
            vote.addVote(player);
        } else {
            player.sendMessage(Component.text(ManHuntPlugin.getprefix() + "You can only skip at day time for night time"));
        }
    }


}