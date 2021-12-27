package de.shiirroo.manhunt.world.save;

import de.shiirroo.manhunt.ManHuntPlugin;
import de.shiirroo.manhunt.event.menu.Menu;
import de.shiirroo.manhunt.event.menu.menus.setting.WorldMenu;
import de.shiirroo.manhunt.gamedata.GameData;
import de.shiirroo.manhunt.utilis.Utilis;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SaveGame {

    private final HashMap<String, File> saveWorldFolder = new LinkedHashMap<>();
    private File saveFolder;
    private File gameDataFolder;
    private int saveSlot;
    private String saveName;
    private boolean isLoading = false;

    public SaveGame(){
        super();
    }

    public SaveGame setSlot(int saveSlot){
        this.saveSlot = saveSlot;
        this.saveFolder = new File(ManHuntPlugin.savesFolder.getPath() + "//Save_" + saveSlot);
        if(Bukkit.getWorlds().size() == 0){
            Arrays.asList("world","world_nether","world_the_end").forEach(worldName ->{
                this.saveWorldFolder.put(worldName, new File(saveFolder.getPath() + "//" + worldName));
            });
        } else {
            Bukkit.getWorlds().forEach(world -> {
                this.saveWorldFolder.put(world.getName(), new File(saveFolder.getPath() + "//" + world.getName()));
            });
        }
        this.gameDataFolder = new File(this.saveFolder.getPath() + "//GameData.ser");
        return this;
    }

    public SaveGame setSaveName(String saveName) {
        this.saveName = saveName;
        return this;
    }

    public String getSaveName() {
        return saveName;
    }

    public long saveGame(boolean force, GameData gameData){
        isLoading = true;
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "save-all");
        long saveUPTime = Calendar.getInstance().getTime().getTime();
        if(force)
            deleteSave();
        if(!this.saveFolder.exists())
            this.saveFolder.mkdir();
        this.saveWorldFolder.forEach((worldName, file) -> {
            if (!file.exists()) {
                try {
                    FileUtils.copyDirectory(new File(worldName), file);
                } catch (IOException e) {
                    isLoading = false;
                    Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.RED +"World save failed");
                }
            }});
        if(!this.gameDataFolder.exists()) {
            try {
                FileOutputStream outputStream = new FileOutputStream(gameDataFolder);
                BukkitObjectOutputStream oos = new BukkitObjectOutputStream(outputStream);
                System.out.println(gameData.getPlayerData().getPlayers().size());
                GameData newGameData = new GameData(gameData);
                System.out.println(gameData.getPlayerData().getPlayers().size());
                oos.writeObject(newGameData);
                oos.flush();
                System.out.println(newGameData.getPlayerData().getPlayers().size());
            } catch (IOException e) {
                Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.RED +"Game-Data save failed");
            }
        };
        isLoading = false;
        WorldMenu.gameSaveMenuHashMap.values().forEach(Menu::setMenuItems);
        Bukkit.getLogger().info(ManHuntPlugin.getprefix() + saveName + " was saved. ( " +  (Calendar.getInstance().getTime().getTime() - saveUPTime) + " ms )");
        return (Calendar.getInstance().getTime().getTime() - saveUPTime);
    }

    public int getSaveSlot() {
        return saveSlot;
    }

    public String getDateString() {
        BasicFileAttributeView basicfile = Files.getFileAttributeView(Path.of(saveFolder.getPath()), BasicFileAttributeView.class);
        try {
            basicfile.readAttributes();
            BasicFileAttributes attr = basicfile.readAttributes();
            long date = attr.creationTime().toMillis();
            Instant instant = Instant.ofEpochMilli(date);
            LocalDateTime localDateTime =  LocalDateTime.ofInstant(instant, ZoneId.of("Europe/Berlin"));
            return  DateTimeFormatter.ofPattern("dd/MM/yyyy kk:mm:ss").format(localDateTime);
        } catch (IOException e) {
            Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.RED + "Something went wrong while getting File Date.");
            return "";
        }
    }


    public long deleteSave(){
        isLoading = true;
        long deleteUPTime = Calendar.getInstance().getTime().getTime();
        if(saveExists()) {
            Utilis.deleteRecursively(saveFolder, true);
        }
        WorldMenu.gameSaveMenuHashMap.values().forEach(Menu::setMenuItems);
        isLoading = false;
        Bukkit.getLogger().info(ManHuntPlugin.getprefix() + saveName + " was deleted. ( " +  (Calendar.getInstance().getTime().getTime() - deleteUPTime) + " ms )");
        return (Calendar.getInstance().getTime().getTime() - deleteUPTime);
    };

    public GameData loadSave(){
        if(saveExists()){
            this.saveWorldFolder.forEach((worldName, file) -> {
                File world = new File(worldName);
                if(!world.exists()) world.mkdir();
                if (file.exists()) {
                    try {
                        FileUtils.copyDirectory(file, new File(worldName));
                    } catch (IOException e) {
                        Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.RED +"World load failed");
                    }
                }});
            if(this.gameDataFolder.exists()) {
                try {
                    FileInputStream fout = new FileInputStream(gameDataFolder);
                    BukkitObjectInputStream oos = new BukkitObjectInputStream(fout);
                    GameData gameData = (GameData) oos.readObject();
                    Bukkit.getLogger().info(ManHuntPlugin.getprefix() + "SaveGame-" + (saveName!= null? saveName :saveSlot) + " is loaded");
                    return gameData;

                } catch (IOException | ClassNotFoundException e) {
                    Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.RED +"Game-Data load failed");
                }
            };
        }
        return null;
    }

    public GameData getGameSaveData(){
        if(saveExists()) {
            if (this.gameDataFolder.exists()) {
                try {
                    FileInputStream fout = new FileInputStream(gameDataFolder);
                    BukkitObjectInputStream oos = new BukkitObjectInputStream(fout);
                    return (GameData) oos.readObject();

                } catch (IOException | ClassNotFoundException e) {
                    Bukkit.getLogger().info(ManHuntPlugin.getprefix() + ChatColor.RED +"Load Game-Data failed");
                }
            }
        }
        return null;
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public boolean saveExists(){
        if(!gameDataFolder.exists())
            return false;
        for (File file: saveWorldFolder.values()) {
            if(!file.exists())
                return false;
        }
        return true;
    }

}
