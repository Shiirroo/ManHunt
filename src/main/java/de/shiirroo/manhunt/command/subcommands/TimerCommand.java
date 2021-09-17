package de.shiirroo.manhunt.command.subcommands;
import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.menu.MenuManager;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.menus.ConfigMenu;
import de.shiirroo.manhunt.event.menu.menus.GameModesMenu;
import de.shiirroo.manhunt.event.menu.menus.GamePresetMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.UUID;

public class TimerCommand extends SubCommand {

    public static HashSet<UUID> playerShowTimers = new HashSet<>();


    @Override
    public String getName() {
        return "Timer";
    }

    @Override
    public String getDescription() {
        return "Displays the playing time.";
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Timer";
    }

    @Override
    public Boolean getNeedOp() {
        return false;
    }

    @Override
    public CommandBuilder getSubCommandsArgs(String[] args)  {
        return null;
    }



    @Override
    public void perform(Player player, String[] args) {
        if(args.length == 1){
            if(playerShowTimers.contains(player.getUniqueId())){
                playerShowTimers.remove(player.getUniqueId());
                player.sendMessage(Component.text(ManHuntPlugin.getprefix() + ChatColor.RED +  "You are no longer showing any playing time."));
            } else {
                playerShowTimers.add(player.getUniqueId());
                player.sendMessage(Component.text(ManHuntPlugin.getprefix() + ChatColor.GREEN + "You are now showing the playing time."));
            }
        } else {
            player.sendMessage(Component.text(ManHuntPlugin.getprefix() + ChatColor.RED + "Command could not be executed like this"));
            try {
                MenuManager.openMenu(GamePresetMenu.class, player, null);
            } catch (MenuManagerException | MenuManagerNotSetupException ignored) {
            }
        }

    }
}
