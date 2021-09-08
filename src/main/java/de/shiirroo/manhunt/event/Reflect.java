package de.shiirroo.manhunt.event;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_16_R3.BlockPosition;

public class Reflect
{
    public static final Class<?> NBTTagCompound = getNMSClass("NBTTagCompound");
    public static final Class<?> CraftItemStack = getCBClass("inventory", "CraftItemStack");
    public static final Class<?> ItemStack = getNMSClass("ItemStack");

    public static Version getServerVersion() {
        return Version.fromString(getVersion());
    }

    public static Version getProtocolVersion(Player player)
    {
        return Version.UNKNOWN;
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>[] params) {
        try {
            return clazz.getConstructor(params); } catch (NoSuchMethodException e) {
        }
        return null;
    }

    public static boolean isCastable(Object object, Class<?> clazz)
    {
        try {
            clazz.cast(object);
            return true; } catch (ClassCastException e) {
        }
        return false;
    }

    public static Object getNMSPlayer(Player player)
    {
        try {
            Method getHandle = player.getClass().getMethod("getHandle", new Class[0]);
            return getHandle.invoke(player, new Object[0]);
        } catch (Exception e) {
        }
        return null;
    }

    public static Method getMethod(Method method)
    {
        method.setAccessible(true);
        return method;
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Method getHandle = player.getClass().getMethod("getHandle", new Class[0]);
            Object nmsPlayer = getHandle.invoke(player, new Object[0]);
            Field pConnectionField = nmsPlayer.getClass().getField("playerConnection");
            Object pConnection = pConnectionField.get(nmsPlayer);
            Method sendMethod = pConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") });
            sendMethod.invoke(pConnection, new Object[] { packet });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Field getField(Field field) {
        field.setAccessible(true);
        return field;
    }

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23) + ".";
    }

    public static Class<?> getNMSClass(String nmsName) {
        String name = "net.minecraft.server." + getVersion() + nmsName;
        try {
            return Class.forName(name); } catch (ClassNotFoundException e) {
        }
        return null;
    }

    public static Class<?> getCBClass(String cbPackage, String cbName)
    {
        String name = "org.bukkit.craftbukkit." + getVersion() + cbPackage + "." + cbName;
        try {
            return Class.forName(name); } catch (ClassNotFoundException e) {
        }
        return null;
    }

    public static Class<?> getClasses(String cbPackage)
    {
        String name = "org.bukkit.craftbukkit." + getVersion() + cbPackage;
        try {
            return Class.forName(name); } catch (ClassNotFoundException e) {
        }
        return null;
    }

    public static enum Version
    {
        UNKNOWN,v1_16_R3, v1_9, v1_8, v1_7, v1_6, v1_5, v1_4, v1_3, v1_2, v1_1;

        public static Version fromString(String input) {
            String tmp = input.replace("v", "");

            if (tmp.startsWith("1_1")) {
                return v1_1;
            }

            if (tmp.startsWith("1_2")) {
                return v1_2;
            }

            if (tmp.startsWith("1_3")) {
                return v1_3;
            }

            if (tmp.startsWith("1_4")) {
                return v1_4;
            }

            if (tmp.startsWith("1_5")) {
                return v1_5;
            }

            if (tmp.startsWith("1_6")) {
                return v1_6;
            }

            if (tmp.startsWith("1_7")) {
                return v1_7;
            }

            if (tmp.startsWith("1_8")) {
                return v1_8;
            }

            if (tmp.startsWith("1_9")) {
                return v1_9;
            }

            if (tmp.startsWith("v1_16_R3")) {
                return v1_16_R3;
            }

            return UNKNOWN;
        }
    }
}