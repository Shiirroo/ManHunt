package de.shiirroo.manhunt;

import de.shiirroo.manhunt.command.ManHuntCommandManager;
import de.shiirroo.manhunt.event.Events;
import de.shiirroo.manhunt.event.block.onBlockBreak;
import de.shiirroo.manhunt.event.block.onBlockPlace;
import de.shiirroo.manhunt.event.entity.onEntityDamageByEntityEvent;
import de.shiirroo.manhunt.event.entity.onEntityDamageEvent;
import de.shiirroo.manhunt.event.entity.onEntityDeathEvent;
import de.shiirroo.manhunt.event.entity.onEntityMountEvent;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.MenuManager;
import de.shiirroo.manhunt.event.menu.MenuManagerException;
import de.shiirroo.manhunt.event.menu.MenuManagerNotSetupException;
import de.shiirroo.manhunt.event.menu.menus.PlayerMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.WorldMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePreset;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.GamePresetMenu;
import de.shiirroo.manhunt.event.menu.menus.setting.gamepreset.presets.*;
import de.shiirroo.manhunt.event.player.*;
import de.shiirroo.manhunt.gamedata.GameData;
import de.shiirroo.manhunt.gamedata.game.GameStatus;
import de.shiirroo.manhunt.teams.TeamManager;
import de.shiirroo.manhunt.utilis.repeatingtask.CompassTracker;
import de.shiirroo.manhunt.utilis.repeatingtask.GameTimes;
import de.shiirroo.manhunt.world.Worldreset;
import de.shiirroo.manhunt.world.save.SaveGame;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.net.URL;
import java.util.*;

public final class ManHuntPlugin extends JavaPlugin implements Serializable {
    public static final boolean debug = false;
    public static final Map<UUID, Menu> playerMenu = new HashMap<>();
    @Serial
    private static final long serialVersionUID = 1L;
    private static final List<GamePreset> gamePresetList = new ArrayList<>();
    public static Integer GameTimesTimer = 1;
    public static File savesFolder;
    public static File reloadFile;
    private static Plugin plugin;
    private static GameData gameData;
    private static TeamManager teamManager;
    private static Worldreset worldreset;

    public static GameData getGameData() {
        return gameData;
    }

    public static void newGameData(Plugin plugin) {
        gameData = new GameData(plugin);
    }

    public static String getprefix() {
        return ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Man" + ChatColor.RED + "Hunt" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    }

    public static TeamManager getTeamManager() {
        return teamManager;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void reloadGame() {
        try {
            if (reloadFile.exists()) reloadFile.delete();
            FileOutputStream outputStream = new FileOutputStream(reloadFile);
            BukkitObjectOutputStream oos = new BukkitObjectOutputStream(outputStream);
            GameData newGameData = new GameData(ManHuntPlugin.getGameData());
            oos.writeObject(newGameData);
            oos.flush();
        } catch (IOException e) {
            Bukkit.getLogger().info(getprefix() + ChatColor.RED + "Something went wrong while reloading.");

        }
    }

    public static Worldreset getWorldreset() {
        return worldreset;
    }

    public static void setUPReloadedWorld() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            try {
                if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
                    player.setGameMode(GameMode.ADVENTURE);
                }
                Menu menu = MenuManager.getMenu(PlayerMenu.class, player.getUniqueId()).open();
                playerMenu.put(player.getUniqueId(), menu);

                menu.setMenuItems();
            } catch (MenuManagerException | MenuManagerNotSetupException e) {
                e.printStackTrace();
            }
        });
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static void setUPWorld() {
        for (OfflinePlayer p : plugin.getServer().getOfflinePlayers()) {
            if (p.isWhitelisted())
                p.setWhitelisted(false);
        }

        plugin.getServer().setWhitelist(false);
        plugin.getServer().setDefaultGameMode(GameMode.ADVENTURE);
        plugin.getServer().setSpawnRadius(0);
        for (World w : Bukkit.getWorlds()) {
            w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, plugin.getConfig().getBoolean("ShowAdvancement"));
            w.setPVP(false);
            w.setTime(0);
            w.setDifficulty(Difficulty.PEACEFUL);
            w.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            w.getWorldBorder().setCenter(w.getSpawnLocation());
            w.getWorldBorder().setSize(20);
            w.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            w.setGameRule(GameRule.SPAWN_RADIUS, 0);
        }
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.isWhitelisted())
                p.setWhitelisted(false);

            p.getInventory().clear();
            p.setGameMode(GameMode.ADVENTURE);
            p.setHealth(20);
            p.setExp(0);
            onPlayerJoin.setUpPlayer(p);
            p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
        }
    }

    public static void checkConfig(GamePreset gamePreset, Integer get) {
        String[] name = gamePreset.presetName().split("\\.");
        for (String s : gamePreset.makeConfig().keySet()) {
            if (gameData.getGameConfig().getConfigCreators(s).getConfigSetting() != null && !gameData.getGameConfig().getConfigCreators(s).getConfigSetting().equals(gamePreset.makeConfig().get(s))) {
                if (get == gamePresetList.size()) {
                    GamePresetMenu.preset = gamePreset;
                    Bukkit.getLogger().info(getprefix() + "Game preset: " + name[name.length - 1]);
                } else {
                    checkConfig(gamePresetList.get(get), get + 1);

                }
                return;
            }
        }
        GamePresetMenu.preset = gamePreset;
        Bukkit.getLogger().info(getprefix() + "Game preset: " + name[name.length - 1]);
        Bukkit.getOnlinePlayers().forEach(GamePresetMenu::setFooderPreset);
    }

    @Override
    public void onEnable() {
        plugin = this;
        teamManager = new TeamManager(this);
        MenuManager.setup(this.getServer(), this);
        worldreset = new Worldreset();

        registerEvents();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new CompassTracker(), 1, 1);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new GameTimes(), 0, GameTimesTimer);

        if (gameData == null) {
            newGameData(this);
            setUPWorld();
        } else {
            ManHuntPlugin.getGameData().getPlayerData().updatePlayers(teamManager);
            if (!gameData.getGameStatus().isGameRunning()) {
                setUPReloadedWorld();
            }
        }

        Objects.requireNonNull(getCommand("ManHunt")).setExecutor(new ManHuntCommandManager());
        Objects.requireNonNull(getCommand("ManHunt")).setTabCompleter(new ManHuntCommandManager());
        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
        setGamePresetList();
        checkVersion();
        Bukkit.getLogger().info(getprefix() + "plugin started.");
    }

    private void checkVersion() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            int resourceID = 96047;
            try (InputStream inputStream = (new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceID)).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    String latest = scanner.next();
                    String current = plugin.getDescription().getVersion();
                    if (Integer.parseInt(current.replace(".", "")) < Integer.parseInt(latest.replaceAll("\\.", ""))) {
                        Bukkit.getLogger().info(getprefix() + "There is a newer version available - v" + latest + ", you are on - v" + current);
                        Bukkit.getLogger().info(getprefix() + "Please download the latest version - https://www.spigotmc.org/resources/" + resourceID + "\n");
                    } else {
                        Bukkit.getLogger().info(getprefix() + "You are running the latest version : v" + current);
                    }
                }
            } catch (IOException e) {
                Bukkit.getLogger().info(getprefix() + ChatColor.RED + "Something went wrong while check version");
            }
        });
    }

    @Override
    public void onDisable() {
        if (!getConfig().getBoolean("isReset") && getConfig().getInt("LoadSaveGame") == -1) {
            reloadGame();
        }
        if (gameData.getGameStatus().isGameRunning()) {
            gameData.getGameStatus().getAutoSave().saveGame(true, gameData);
            Bukkit.getLogger().info(getprefix() + "Game saved automatically.");
        }
        Bukkit.getLogger().info(getprefix() + "plugin stopped.");
    }

    public void setGamePresetList() {
        gamePresetList.add(new Hardcore());
        gamePresetList.add(new Turtle());
        gamePresetList.add(new Default());
        gamePresetList.add(new Custom());
        checkConfig(new Dream(), 0);
    }

    private void registerEvents() {

        getServer().getPluginManager().registerEvents(new Events(), this);

        //---------------------BLOCK------------------------
        getServer().getPluginManager().registerEvents(new onBlockPlace(), this);
        getServer().getPluginManager().registerEvents(new onBlockBreak(), this);
        //---------------------Entity-----------------------

        getServer().getPluginManager().registerEvents(new onEntityDamageByEntityEvent(), this);
        getServer().getPluginManager().registerEvents(new onEntityDamageEvent(), this);
        getServer().getPluginManager().registerEvents(new onEntityDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new onEntityMountEvent(), this);
        //---------------------Player-------------------------
        getServer().getPluginManager().registerEvents(new onAsyncPlayerChatEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerPickupItemEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerCommandPreprocessEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerInteractEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new onPlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new onPlayerMove(), this);
        getServer().getPluginManager().registerEvents(new onPlayerRespawnEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerDropItemEvent(), this);
        getServer().getPluginManager().registerEvents(new onInventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerSwapHandItemsEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerGameModeChangeEvent(), this);
        getServer().getPluginManager().registerEvents(new onPrepareItemCraftEvent(), this);
        //----------------------------------------------------

    }

    private void loadWorld(SaveGame saveGame) {
        worldreset.reset();
        gameData = saveGame.loadSave();
    }

    public void loadReloadGame() {
        if (getConfig().getBoolean("isReset")) {
            reloadFile.delete();
            return;
        }
        try {
            FileInputStream fout = new FileInputStream(reloadFile);
            reloadFile.delete();
            BukkitObjectInputStream oos = new BukkitObjectInputStream(fout);
            gameData = (GameData) oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Bukkit.getLogger().info(getprefix() + ChatColor.RED + "Something went wrong while LoadGame-Reloading.");
            reloadFile.delete();
        }
    }

    @Override
    public void onLoad() {
        Bukkit.getLogger().info(getprefix() + "plugin is loading.");
        worldreset = new Worldreset();
        savesFolder = new File("plugins//" + this.getDescription().getName() + "//Saves");
        if (!savesFolder.exists()) savesFolder.mkdir();

        reloadFile = new File(this.getDataFolder().getPath() + "//Reload_GameData.ser.");
        saveConfig();
        if (!getConfig().getBoolean("isReset")) {
            if (getConfig().getInt("LoadSaveGame") != -1) {
                int id = getConfig().getInt("LoadSaveGame");
                getConfig().set("LoadSaveGame", -1);
                saveConfig();
                SaveGame autoSave = new GameStatus().getAutoSave();
                if (autoSave != null && autoSave.getSaveSlot() == id) {
                    loadWorld(autoSave);
                } else {
                    for (SaveGame saveGame : WorldMenu.getGameSave()) {
                        if (saveGame.getSaveSlot() == id) {
                            loadWorld(saveGame);
                        }
                    }
                }
            } else if (reloadFile.exists()) {
                loadReloadGame();
            }
        }

        if (getConfig().getBoolean("BossbarCompass")) {
            getConfig().set("BossbarCompass", false);
        }
        if (getConfig().getBoolean("isReset")) {
            worldreset.reset();
            getConfig().set("isReset", false);
        }
        saveConfig();
    }
}
