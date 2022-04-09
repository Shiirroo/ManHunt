package de.shiirroo.manhunt.command.subcommands.vote;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.utilis.vote.VoteCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Objects;

public class VoteSkipNight extends Vote {
    @Override
    protected VoteCreator voteCreator() {
        return new VoteCreator(true, ManHuntPlugin.getPlugin(), ChatColor.GRAY + "Skipping Night " + ChatColor.GOLD + "VOTEPLAYERS " + ChatColor.BLACK + "| " + ChatColor.GOLD + "ONLINEPLAYERS" + ChatColor.GRAY + " [ " + ChatColor.GREEN + "TIMER " + ChatColor.GRAY + "]", 30);
    }

    @Override
    protected void editBossBarCreator() {
        getBossBarCreator().onComplete(aBoolean -> {
            if (aBoolean) {
                Objects.requireNonNull(Bukkit.getWorld("world")).setTime(1000L);
            }
            VoteCommand.resetVote();
            ManHuntPlugin.getGameData().getPlayerData().updatePlayers(ManHuntPlugin.getTeamManager());
        });
        getBossBarCreator().onShortlyComplete(aBoolean1 -> Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)));
    }

    @Override
    protected boolean requirement() {
        return Objects.requireNonNull(Bukkit.getWorld("world")).getTime() >= 13000L;
    }

    @Override
    protected String requirementMessage() {
        return ManHuntPlugin.getprefix() + "You can only skip at night time for day time.";
    }
}
