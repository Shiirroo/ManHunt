package de.shiirroo.manhunt.command;

import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import org.bukkit.entity.Player;

import java.io.IOException;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract Boolean getNeedOp() ;

    public abstract CommandBuilder getSubCommandsArgs(String[] args);


    public abstract void perform(Player player, String[] args) throws IOException, InterruptedException, MenuManagerException, MenuManagerNotSetupException;


}
