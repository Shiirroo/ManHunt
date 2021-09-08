package de.shiirroo.manhunt.utilis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Vote {

    private Set<UUID> votePlayers = new HashSet<>();
    private BossBarCreator bossBarCreator;


    public Vote(Boolean bossbarForVote, Plugin plugin, String title, Integer voteTime){
        this.bossBarCreator = new BossBarCreator( plugin, title, voteTime, bossbarForVote, votePlayers);

    }

    public BossBarCreator getbossBarCreator() {
        return this.bossBarCreator;
    }


    public Set<UUID> getPlayers() {
        return votePlayers;
    }

    public void addVote(Player player) {
        if(Bukkit.getOnlinePlayers().size() > 1){
        this.votePlayers.add(player.getUniqueId());
        if(this.bossBarCreator.isRunning())
            this.bossBarCreator.getBossBar().setTitle(this.bossBarCreator.updateBossBarTitle());
        }
    }

    public void cancelVote(){
        this.bossBarCreator.cancel();
        this.votePlayers.clear();
    }

    public void removeVote(Player player){
        this.votePlayers.remove(player.getUniqueId());
    }

    public boolean hasPlayerVote(Player player) {
        if(votePlayers.contains(player.getUniqueId())){
            return true;
        }
        return false;
    }

}
