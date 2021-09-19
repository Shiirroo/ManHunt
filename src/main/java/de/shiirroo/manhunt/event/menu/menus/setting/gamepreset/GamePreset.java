package de.shiirroo.manhunt.event.menu.menus.setting.gamepreset;
import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.command.subcommands.Ready;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.presets.Custom;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class GamePreset {


    public String presetName(){
        return this.getClass().getName();
    };

    public abstract String getSpeedRunnersMaxSize();

    public abstract String getAssassinMaxSize();

    public abstract String getHunterMaxSize();


    public abstract ItemStack displayItem();

    public abstract int getSpeedRunnerSize();

    public abstract boolean setPlayersGroup();

    public abstract HashMap<String, Object> makeConfig();

    protected void setConfig(){
        if(makeConfig() != null)


            makeConfig().forEach((s, o) -> {
                ManHuntPlugin.getConfigCreators(s).setConfigSetting(o);
                if(Ready.ready != null && s.equalsIgnoreCase("ReadyStartTime")) Ready.ready.getbossBarCreator().setTime((Integer) o);
            });
    }
}
