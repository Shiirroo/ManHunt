package de.shiirroo.manhunt.teams;

import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import de.shiirroo.manhunt.command.subcommands.VoteCommand;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import java.util.ArrayList;
import java.util.List;


/**
 * Wrapper on server scoreboard
 */
public class TeamManager  {

    private final Scoreboard board;
    private List<String> suffix = new ArrayList<>();

    public TeamManager(Plugin plugin) {
        ScoreboardManager manager = plugin.getServer().getScoreboardManager();
        suffix.add("");suffix.add(ChatColor.GRAY+" ["+ChatColor.GREEN+ "✔" +ChatColor.GRAY+"]");suffix.add(ChatColor.GRAY+" ["+ChatColor.RED+ "❌" +ChatColor.GRAY+"]");
        this.board = manager.getMainScoreboard();
        try {
            deleteIndexTeam();
            for(ManHuntRole manHuntRole : ManHuntRole.values()){
                for(int i=0;i!=3;i++) {
                    if(this.board.getTeam(manHuntRole + "-" + i) == null) {
                        createTeam(i,manHuntRole);
                    }
                }
            }



            setInvisibleNameTag();


        }catch(NullPointerException e) {
            System.out.println("NullPointerException thrown!");
            }


    }

    private void createTeam(Integer i, ManHuntRole manHuntRole){
        Team team = this.board.registerNewTeam(manHuntRole + "-" + i);
        team.color(manHuntRole.getTextColor());
        team.prefix(Component.text(manHuntRole + " - "));
        team.suffix(Component.text(suffix.get(i)));
        team.displayName(Component.text(manHuntRole + " - "));
        team.setAllowFriendlyFire(false);
    }

    private void deleteIndexTeam(){
            for(Player onlineplayers : Bukkit.getOnlinePlayers()){
                for(Team team : this.board.getTeams()){
                    team.removeEntry(onlineplayers.getName());
                }
        }
            for(OfflinePlayer offlineplayers : Bukkit.getOfflinePlayers()){
                for(Team team : this.board.getTeams()){
                    team.removeEntry(offlineplayers.getName());
                }
        }
    }

    public void addPlayer(ManHuntRole teamName, Player player) {
        String name = getName(teamName, player);
        Team team = this.board.getTeam(name);
        if (team == null)
            throw new RuntimeException("No team with name " + name + " found");
        team.addEntry(player.getName());
        changePlayerName(player, name);
    }
    public void updatePlayer(ManHuntRole teamName, Player player) {
        String name = getName(teamName, player);
        Team team = this.board.getTeam(name);
        if (team == null)
            throw new RuntimeException("No team with name " + name + " found");
        team.addEntry(player.getName());
    }


    public void changePlayerName(Player player, String name) {
        Team team = board.getTeam(name);
        if (team == null) return;
        TextColor cc = team.color();
        if(player.isOp()) {
            player.displayName(Component.text(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED +"ADMIN"+ChatColor.DARK_GRAY+ "] "+ ChatColor.RESET ).append(team.displayName().color(cc).append(Component.text("" + player.getName() + ChatColor.RESET + ChatColor.GRAY))));
        } else {
            player.displayName(team.displayName().color(cc).append(Component.text("" + player.getName() + ChatColor.RESET + ChatColor.GRAY)));
        }
    }

    public void changePlayerName(Player player, ManHuntRole teamName) {
        String name = getName(teamName, player);
        Team team = board.getTeam(name);
        if (team == null) return;
        TextColor cc = team.color();
        if(player.isOp()) {
            player.displayName(Component.text(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED +"ADMIN"+ChatColor.DARK_GRAY+ "] "+ ChatColor.RESET ).append(team.displayName().color(cc).append(Component.text("" + player.getName() + ChatColor.RESET + ChatColor.GRAY))));
        } else {
            player.displayName(team.displayName().color(cc).append(Component.text("" + player.getName() + ChatColor.RESET + ChatColor.GRAY)));
        }
    }

    public String getName(ManHuntRole teamName, Player player){
        String name;
        if(StartGame.gameRunning != null && VoteCommand.vote == null){
            name = teamName + "-0";
        } else if((Ready.ready != null && Ready.ready.hasPlayerVote(player)) || (VoteCommand.vote != null && VoteCommand.vote.hasPlayerVote(player))){
            name = teamName + "-1";
        } else {
            name = teamName + "-2";
        }
        return name;
    }

    public void removePlayer(ManHuntRole teamName, Player player) {
        String name = getName(teamName, player);
        Team team = board.getTeam(name);
        if (team == null)
            throw new RuntimeException("No team with name " + teamName + " found");
        team.removeEntry(player.getName());
        player.displayName(Component.text(player.getName()));
    }


    private void setInvisibleNameTag() {
        for(int i=0;i!=3;i++) {
            Team team = this.board.getTeam(ManHuntRole.Speedrunner + "-" + i);
            if (team == null)
                throw new RuntimeException("No team with name " + ManHuntRole.Speedrunner + " found");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        }
    }
}

