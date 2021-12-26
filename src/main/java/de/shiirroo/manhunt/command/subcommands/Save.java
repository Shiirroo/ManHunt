package de.shiirroo.manhunt.command.subcommands;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.CommandBuilder;
import de.shiirroo.manhunt.command.SubCommand;
import de.shiirroo.manhunt.event.menu.MenuManager;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.menus.setting.gamesave.GameSaveMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Save extends SubCommand {

    @Override
    public String getName() {
        return "Save";
    }

    @Override
    public String getDescription() {
        return "Save game data, then you can continue playing later. ";
    }

    @Override
    public String getSyntax() {
        return "/ManHunt Reset";
    }

    @Override
    public Boolean getNeedOp() {
        return true;
    }


    @Override
    public CommandBuilder getSubCommandsArgs(String[] args) {
        return null;
    }


    @Override
    public void perform(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage(ManHuntPlugin.getprefix() + ChatColor.RED + "I´m sorry, but you don´t have permission to perform this command");
            return;
        }
        if (args.length == 1) {
            try {
                MenuManager.getMenu(GameSaveMenu.class, player.getUniqueId()).setBack(false).open();
            } catch (MenuManagerException | MenuManagerNotSetupException e) {
                Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.RED + "Something went wrong while open game-saves");
            }
        }
    }
}
