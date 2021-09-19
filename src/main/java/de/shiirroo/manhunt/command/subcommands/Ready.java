package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePresetMenu;
import de.shiirroo.manhunt.utilis.config.Config;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.utilis.vote.Vote;
import de.shiirroo.manhunt.teams.model.ManHuntRole;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class Ready extends SubCommand {

    private static final HashMap<UUID, Long> playerReadyTime = new HashMap<>();
    public static Vote ready;

    @Override
    public String getName() {
        return "Ready";
    }

    @Override
    public String getDescription() {
        return "ManHunt ready to start";
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Ready";
    }

    @Override
    public Boolean getNeedOp() {
        return false;
    }

    @Override
    public CommandBuilder getSubCommandsArgs(String[] args) {
        return null;
    }


    @Override
    public void perform(Player p, String[] args) {
        if(StartGame.gameRunning == null){
            if(ready != null){
                    if(!setReady(p))
                        p.sendMessage(ManHuntPlugin.getprefix() + "You're too fast, have a little patience");
            }
        } else {
            p.sendMessage(ManHuntPlugin.getprefix() + "You can´t change ready status while running match");
        }

    }

    public static boolean setReady(Player p){
        if(ready == null) setReadyVote();
        if(isPlayerHasCooldown(p)) {
            if (ready.hasPlayerVote(p)) {
                readyRemove(p, false);
                return true;
            } else return readyAdd(p);
        }
        return false;
    }

    public static void setReadyVote(){
        Bukkit.setWhitelist(false);
        ready = new Vote(false,ManHuntPlugin.getPlugin(), ChatColor.GREEN + "Game will start in " + ChatColor.GOLD+ "TIMER", Config.getReadyStartTime());
        ready.getbossBarCreator().onComplete(aBoolean -> {
                    ready = null;
                    if(aBoolean) {
                        if (GamePresetMenu.preset.setPlayersGroup())
                            StartGame.Start();
                    }
                }
        );
        ready.getbossBarCreator().onShortlyComplete(aBoolean -> {
            Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f));
            Bukkit.setWhitelist(true);
            ;});
    }




    public static boolean readyAdd(Player p){
            if(startGame()){
                ready.addVote(p);
                playerReadyTime.put(p.getUniqueId(), (new Date().getTime() +5000L));
                Events.playerMenu.get(p.getUniqueId()).setMenuItems();
                ManHuntPlugin.getPlayerData().setUpdateRole(p, ManHuntPlugin.getTeamManager());
                return true;
            }

        return false;
    }

    public static void readyRemove(Player p, Boolean LeaveOrJoin){
        if(ready.hasPlayerVote(p)) {
            playerReadyTime.remove(p.getUniqueId());
            ready.removeVote(p);
            ready.getbossBarCreator().cancel();
            playerReadyTime.put(p.getUniqueId(), (new Date().getTime() + 5000L));
            if(p.isOnline()) {
                Events.playerMenu.get(p.getUniqueId()).setMenuItems();
                ManHuntPlugin.getPlayerData().setUpdateRole(p, ManHuntPlugin.getTeamManager());
            }
        }
        if(LeaveOrJoin) {
            setOtherPlayerUnready();
        }
    }

    public static boolean isPlayerHasCooldown(Player p){
        Long cooldown = playerReadyTime.get(p.getUniqueId());
        if(cooldown == null) return true;
        return (new Date().getTime() - cooldown) > 0;
    }

    public static boolean startGame(){

        if(Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count() >1 && (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() != 0
                || ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Unassigned).size() >= 1)){
                if (ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() == Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count()
                        ||  ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Hunter).size() == Bukkit.getOnlinePlayers().size() ||
                        ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Assassin).size() == Bukkit.getOnlinePlayers().size()){
                    return false;
                }
                if(!GamePresetMenu.preset.getSpeedRunnersMaxSize().equalsIgnoreCase("ထ")){
                   if(Integer.parseInt(GamePresetMenu.preset.getSpeedRunnersMaxSize()) < ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() ||
                            ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Speedrunner).size() != Integer.parseInt(GamePresetMenu.preset.getSpeedRunnersMaxSize()))
                    {
                        return false;
                    }
                }
                if(!GamePresetMenu.preset.getHunterMaxSize().equalsIgnoreCase("ထ")){
                   if(Integer.parseInt(GamePresetMenu.preset.getHunterMaxSize()) < ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Hunter).size() |
                            ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Hunter).size() != Integer.parseInt(GamePresetMenu.preset.getHunterMaxSize()))
                    {
                        return false;
                    }
                 }
                if(!GamePresetMenu.preset.getAssassinMaxSize().equalsIgnoreCase("ထ")){
                    if(Integer.parseInt(GamePresetMenu.preset.getAssassinMaxSize()) < ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Assassin).size()  ||
                            ManHuntPlugin.getPlayerData().getPlayersByRole(ManHuntRole.Assassin).size() != Integer.parseInt(GamePresetMenu.preset.getAssassinMaxSize()))
                    {
                    return false;
                    }
                }

                if((ready.getPlayers().size() +1) == Bukkit.getOnlinePlayers().stream().filter(e -> !e.getGameMode().equals(GameMode.SPECTATOR)).count()){
                        ready.startVote();
                }
                return true;
        }
        return false;
    }


    private static void setOtherPlayerUnready(){
        if(ready.getPlayers().size() >= 1){
            Optional<UUID> uuid = ready.getPlayers().stream().findFirst();
            if(uuid.isPresent() && ready.hasPlayerVote(Objects.requireNonNull(Bukkit.getPlayer(uuid.get()))))
                ready.removeVote(Bukkit.getPlayer(uuid.get()));
                Events.playerMenu.get(uuid.get()).setMenuItems();
                ManHuntPlugin.getPlayerData().setUpdateRole(Bukkit.getPlayer(uuid.get()), ManHuntPlugin.getTeamManager());
        }
    }



//ChatColor.GREEN + "Game will start in " + ChatColor.GOLD+ startGame
    //if(StartGame.setPlayer())  StartGame.Start();
    //config.getReadyStartTime()





}