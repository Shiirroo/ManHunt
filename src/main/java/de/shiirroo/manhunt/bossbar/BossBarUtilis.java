package de.shiirroo.manhunt.bossbar;

import de.shiirroo.manhunt.ManHuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Comparator;

public class BossBarUtilis {

    private static final String s = "Ｓ-•-•-•-•-•-•-";
    private static final String sw = "135-•-•-•-•-•-•-";
    private static final String w = "Ｗ-•-•-•-•-•-•-";
    private static final String nw = "045-•-•-•-•-•-•-";
    private static final String n = "Ｎ-•-•-•-•-•-•-";
    private static final String ne = "315-•-•-•-•-•-•-";
    private static final String e = "Ｅ-•-•-•-•-•-•-";
    private static final String se = "225-•-•-•-•-•-•-";
    private static final String AllDirectionString = s + sw + w + nw + n + ne + e + se;
    private static Plugin plugin;

    public BossBarUtilis(Plugin plugin) {
        BossBarUtilis.plugin = plugin;
    }


    public static String setBossBarLoc(Player player) {
        String BossBarString;
        float YawPerLetter = 360f / AllDirectionString.length(); // 3
        float PlayerYawCal = calcYaw(player) + YawPerLetter; // Number 0 - 360
        float CalPlayerYaw = PlayerYawCal / YawPerLetter - 1; // 0 - 120
        float FOV = 113f;
        float StringLength = FOV / YawPerLetter; //
        float SideTextLength = StringLength / 2; //


        if (CalPlayerYaw > SideTextLength) {
            if (CalPlayerYaw < (AllDirectionString.length() - SideTextLength)) {
                BossBarString = AllDirectionString.substring(Math.round(CalPlayerYaw - SideTextLength), Math.round(CalPlayerYaw + SideTextLength));
            } else {
                float remaining = (CalPlayerYaw + SideTextLength) - AllDirectionString.length();
                if (remaining > 120) {
                    Bukkit.getLogger().info(ManHuntPlugin.getprefix() + remaining + " " + CalPlayerYaw + " " + SideTextLength + " " + AllDirectionString.length());
                }
                String SecondString = AllDirectionString.substring(0, Math.round(remaining));
                BossBarString = AllDirectionString.substring(Math.round(CalPlayerYaw - SideTextLength)) + SecondString;
            }
        } else {
            float remaining = AllDirectionString.length() - (SideTextLength - CalPlayerYaw);
            String FristString = AllDirectionString.substring(Math.round(remaining));
            BossBarString = FristString + AllDirectionString.substring(0, Math.round(CalPlayerYaw + SideTextLength));
        }


        String MiddlePoint = ChatColor.RED + BossBarString.substring(BossBarString.length() / 2, BossBarString.length() / 2 + 1) + ChatColor.RESET;
        String LeftPart = BossBarString.substring(0, BossBarString.length() / 2) + ChatColor.RESET;
        String RightPart = BossBarString.substring(BossBarString.length() / 2 + 1) + ChatColor.RESET;
        String EndString = LeftPart + MiddlePoint + RightPart;

        Player nearst = plugin.getServer().getOnlinePlayers().stream()
                .filter(p -> !p.equals(player))
                .filter(p -> p.getWorld().equals(player.getWorld()))
                .min(Comparator.comparing(p -> p.getLocation().distance(player.getLocation())))
                .orElse(null);
        //Bukkit.getLogger().info(ManHuntPlugin.getprefix() +nearst);
        if (!player.getName().equalsIgnoreCase("Shiirroo")) {
            assert nearst != null;
            if (nearst.getName().equalsIgnoreCase("Shiirroo")) {


                double x = player.getLocation().getX() - nearst.getLocation().getX();
                double z = player.getLocation().getZ() - nearst.getLocation().getZ();
                double vectorx = player.getEyeLocation().getDirection().getX();
                double vectorz = player.getEyeLocation().getDirection().getZ();

                //Bukkit.getLogger().info(ManHuntPlugin.getprefix() +vectorx + " " + vectorz);

                double Skalar = (x * vectorx) + (z * vectorz);

                double xZ = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));

                double endprodukt = Skalar / xZ;

                double gradende = 180 - Math.toDegrees(Math.acos(endprodukt));

                //player.launchProjectile(Snowball.class).setVelocity(player.getEyeLocation().getDirection().multiply(4.0D));

                double gradzahl2 = FOV / StringLength;

                double changesting = (gradende / gradzahl2) * 2;


                // Make local offset
                Vector offset = new Vector(0D, 0D, 0D);

                // Get eye location
                Location view = player.getEyeLocation();

                // Set transform
                Location local = offset.toLocation(view.getWorld(), view.getYaw(), view.getPitch());

                // Add local transform to view
                view.add(local);


            }
        }


        return EndString;
    }


    public static float calcYaw(Player player) {
        Location playerLoc = player.getLocation();
        float calcYaw = playerLoc.getYaw();
        if (calcYaw < 0.0F) {
            calcYaw += 360.0F;
        }

        return calcYaw;
    }
}
