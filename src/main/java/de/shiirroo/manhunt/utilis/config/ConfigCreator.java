package de.shiirroo.manhunt.utilis.config;

import org.bukkit.plugin.Plugin;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ConfigCreator implements Serializable {

    private final String configName;
    private Boolean configSettingBool;
    private Integer configSettingInt;
    private Integer min;
    private Integer max;
    private Integer defaultValue;
    private List<String> lore;

    public ConfigCreator(String configName) {
        this.configName = configName;
    }

    public ConfigCreator(String configName, Integer min, Integer max, Integer defaultValue) {
        this.configName = configName;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
    }

    public ConfigCreator configCreator(Plugin plugin) {
        Object configSetting = plugin.getConfig().get(configName);
        if (configSetting instanceof Integer) {
            if ((Integer) configSetting >= min && (Integer) configSetting <= max) {
                configSettingInt = (Integer) configSetting;
            } else {
                configSettingInt = defaultValue;
                plugin.getConfig().set(configName, defaultValue);
                plugin.saveConfig();
            }
        }
        if (configSetting instanceof Boolean)
            this.configSettingBool = (Boolean) configSetting;
        return this;
    }


    public List<String> getLore() {
        return lore;
    }

    public ConfigCreator setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }


    public Object getConfigSetting() {
        if (this.configSettingBool != null) {
            return configSettingBool;
        } else {
            return configSettingInt;
        }
    }

    public void setConfigSetting(Object configSetting, Plugin plugin) {
        if (configSetting instanceof Integer configSettingInt) {
            if (configSettingInt >= this.min && configSettingInt <= this.max && !Objects.equals(this.configSettingInt, configSettingInt)) {
                this.configSettingInt = configSettingInt;
                plugin.getConfig().set(this.configName, configSettingInt);
                plugin.saveConfig();
            }
        } else if (configSetting instanceof Boolean configSettingBool) {
            if (this.configSettingBool != configSettingBool) {
                this.configSettingBool = configSettingBool;
                plugin.getConfig().set(this.configName, configSettingBool);
                plugin.saveConfig();
            }
        }
    }


    public Integer getMin() {
        return this.min;
    }

    public Integer getMax() {
        return this.max;
    }

    public String getConfigName() {
        return this.configName;
    }
}
