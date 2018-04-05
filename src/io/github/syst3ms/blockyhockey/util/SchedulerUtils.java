package io.github.syst3ms.blockyhockey.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SchedulerUtils {
    public static void scheduleForTimes(Plugin plugin, Runnable task, long delay, long period, int repeatAmount) {
        int[] data = new int[2];
        data[0] = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                plugin,
                () -> {
                    if (data[1]++ < repeatAmount) {
                        task.run();
                    } else {
                        Bukkit.getScheduler().cancelTask(data[0]);
                    }
                },
                delay,
                period
        );
    }
}
