package de.shiirroo.manhunt.teams;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.Serializable;
import java.util.Objects;


/**
 * Wrapper on server scoreboard
 */
public class TeamManager  {

    private final Scoreboard board;

    public TeamManager(Plugin plugin) {
        ScoreboardManager manager = plugin.getServer().getScoreboardManager();

        this.board = manager.getMainScoreboard();
        try {
            if (this.board.getTeam(ManHuntRole.Assassin.toString()) == null) {
                Team Blue = this.board.registerNewTeam(ManHuntRole.Assassin.toString());
                createTeam(Blue, NamedTextColor.BLUE, ManHuntRole.Assassin + " - ");
            }

            deleteIndexTeam(ManHuntRole.Assassin);

            if (this.board.getTeam(ManHuntRole.Hunter.toString()) == null) {
                Team RED = this.board.registerNewTeam(ManHuntRole.Hunter.toString());
                 createTeam(RED, NamedTextColor.RED, ManHuntRole.Hunter + " - ");
            }

            deleteIndexTeam(ManHuntRole.Hunter);

            if (this.board.getTeam(ManHuntRole.Speedrunner.toString()) == null) {
                Team GREEN = this.board.registerNewTeam(ManHuntRole.Speedrunner.toString());
                createTeam(GREEN, NamedTextColor.DARK_PURPLE, ManHuntRole.Speedrunner + " - ");
            }

            deleteIndexTeam(ManHuntRole.Speedrunner);

            if (this.board.getTeam(ManHuntRole.Unassigned.toString()) == null) {
                Team GREEN = this.board.registerNewTeam(ManHuntRole.Unassigned.toString());
                createTeam(GREEN, NamedTextColor.YELLOW, ManHuntRole.Unassigned + " - ");
            }


            deleteIndexTeam(ManHuntRole.Unassigned);
            setInvisibleNameTag();


        }catch(NullPointerException e) {
            System.out.println("NullPointerException thrown!");
            }


    }

    private void createTeam(Team team, NamedTextColor color, String component){
        team.color(color);
        team.prefix(Component.text(component));
        team.displayName(Component.text(component));
        team.setAllowFriendlyFire(false);

    }

    private void deleteIndexTeam(ManHuntRole mhr){
            for(Player onlineplayers : Bukkit.getOnlinePlayers()){
                Objects.requireNonNull(this.board.getTeam(mhr.toString())).removeEntry(onlineplayers.getName());

        }
            for(OfflinePlayer offlineplayers : Bukkit.getOfflinePlayers()){
                Objects.requireNonNull(this.board.getTeam(mhr.toString())).removeEntry(Objects.requireNonNull(offlineplayers.getName()));

        }
    }

    public void addPlayer(ManHuntRole teamName, Player player) {
        Team team = this.board.getTeam(teamName.toString());
        if (team == null)
            throw new RuntimeException("No team with name " + teamName + " found");
        team.addEntry(player.getName());
        changePlayerName(player, teamName);
    }

    public void changePlayerName(Player player, ManHuntRole role) {
        Team team = board.getTeam(role.toString());
        if (team == null) return;
        TextColor cc = team.color();
        if(player.isOp()) {
            player.displayName(Component.text(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED +"ADMIN"+ChatColor.DARK_GRAY+ "] "+ ChatColor.RESET ).append(team.displayName().color(cc).append(Component.text("" + player.getName() + ChatColor.RESET + ChatColor.GRAY))));
        } else {
            player.displayName(team.displayName().color(cc).append(Component.text("" + player.getName() + ChatColor.RESET + ChatColor.GRAY)));
        }
    }

    public void removePlayer(ManHuntRole teamName, Player player) {
        Team team = this.board.getTeam(teamName.toString());
        if (team == null)
            throw new RuntimeException("No team with name " + teamName + " found");
        team.removeEntry(player.getName());
        player.displayName(Component.text(player.getName()));
    }

    private void setInvisibleNameTag() {
        Team team = this.board.getTeam(ManHuntRole.Speedrunner.toString());
        if (team == null)
            throw new RuntimeException("No team with name " + ManHuntRole.Speedrunner + " found");
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
    }
}

