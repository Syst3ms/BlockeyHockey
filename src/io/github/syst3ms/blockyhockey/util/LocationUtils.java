package io.github.syst3ms.blockyhockey.util;

import org.bukkit.Location;

public class LocationUtils {
    public static int[] getLocationRanges(Location loc1, Location loc2) {
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        return new int[]{minX, maxX, minY, maxY, minZ, maxZ};
    }

    public static boolean isInBox(Location loc, int[] range) {
        return range[0] <= loc.getBlockX() && loc.getBlockX() <= range[1] &&
               range[2] <= loc.getBlockY() && loc.getBlockY() <= range[3] &&
               range[4] <= loc.getBlockZ() && loc.getBlockZ() <= range[5];
    }
}
