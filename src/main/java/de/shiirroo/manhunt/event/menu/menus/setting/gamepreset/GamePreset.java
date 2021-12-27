package de.shiirroo.manhunt.event.menu.menus.setting.gamepreset;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.command.subcommands.StartGame;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class GamePreset {


    public String presetName(){
        return this.getClass().getName();
    }

    public abstract String getSpeedRunnersMaxSize();

    public abstract String getAssassinMaxSize();

    public abstract String getHunterMaxSize();

    protected abstract ItemStack displayItem();

    protected abstract int getSpeedRunnerSize();

    public abstract boolean setPlayersGroup();

    public abstract HashMap<String, Object> makeConfig();

    protected void setConfig(){
        if(makeConfig() != null)
            makeConfig().forEach((s, o) -> {
                ManHuntPlugin.getGameData().getGameConfig().getConfigCreators(s).setConfigSetting(o, ManHuntPlugin.getPlugin());
                if(s.equalsIgnoreCase("ReadyStartTime")) Ready.ready.getbossBarCreator().setTime((Integer) o);
                if(s.equalsIgnoreCase("HuntStartTime")) StartGame.bossBarGameStart.setTime((Integer) o);
            });
    }
}
