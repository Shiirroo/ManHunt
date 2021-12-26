package de.shiirroo.manhunt.command.subcommands.vote;

import de.shiirroo.manhunt.utilis.vote.BossBarCreator;
import de.shiirroo.manhunt.utilis.vote.VoteCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public abstract class Vote {

    private final VoteCreator voteCreator = voteCreator();

    public VoteCreator getVoteCreator() {
        return voteCreator;
    }

    protected abstract VoteCreator voteCreator();

    protected BossBarCreator getBossBarCreator(){
        return getVoteCreator().getbossBarCreator();
    }

    protected abstract void editBossBarCreator();

    protected abstract boolean requirement();

    protected abstract String requirementMessage();

    public void startVote(Player player){
        if(requirement()) {
            editBossBarCreator();
            getVoteCreator().startVote();
            getVoteCreator().addVote(player);
        } else {
            player.sendMessage(Component.text(requirementMessage()));
        }

    }
}
