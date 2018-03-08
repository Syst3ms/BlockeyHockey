package io.github.syst3ms.blockyhockey.command.testing;

import com.google.common.collect.ImmutableMap;
import io.github.syst3ms.blockyhockey.BlockyHockey;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.atomic.AtomicInteger;

public class SpawningPuckTitleTest implements CommandExecutor {
    private int titleTaskId;
    private final BlockyHockey pluginInstance;

    public SpawningPuckTitleTest(BlockyHockey pluginInstance) {
        this.pluginInstance = pluginInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender)
            return false;
        if (label.equalsIgnoreCase("spawnpucktitletest")) {
            Player p = (Player) sender;
            p.sendTitle(
                    "Spawning puck...",
                    "",
                    10,
                    2 * 20,
                    0
            );
            AtomicInteger count = new AtomicInteger(3);
            titleTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                    pluginInstance,
                    () -> {
                        int i = count.getAndDecrement();
                        if (i == 0) {
                            p.sendTitle(
                                    "Spawning puck...",
                                    "§2§l> GO <",
                                    5,
                                    2 * 20 + 10,
                                    5
                            );
                            Bukkit.getScheduler().cancelTask(titleTaskId);
                        } else {
                            p.sendTitle(
                                    "Spawning puck...",
                                    getSubtitleText(i),
                                    0,
                                    21,
                                    0
                            );
                        }
                    },
                    2 * 20L,
                    20L
            );
        }
        return false;
    }

    private static final ImmutableMap<Integer, ChatColor> REMAINING_COLORS = ImmutableMap.of(3, ChatColor.GREEN, 2, ChatColor.GOLD, 1, ChatColor.DARK_RED);

    private String getSubtitleText(int remainingSeconds) {
        if (remainingSeconds > 3) {
            return "";
        }
        ComponentBuilder builder = new ComponentBuilder("> ");
        builder.append(String.valueOf(remainingSeconds))
               .bold(true)
               .color(REMAINING_COLORS.get(remainingSeconds))
               .append(" <", ComponentBuilder.FormatRetention.NONE);
        return TextComponent.toLegacyText(builder.create());
    }
}
