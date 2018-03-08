package io.github.syst3ms.blockyhockey.command.testing;

import io.github.syst3ms.blockyhockey.BlockyHockey;
import io.github.syst3ms.blockyhockey.game.GameManager;
import io.github.syst3ms.blockyhockey.game.Timer;
import io.github.syst3ms.blockyhockey.team.enums.BlockyHockeyTeam;
import io.github.syst3ms.blockyhockey.util.StringUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class PeriodTestCommand implements CommandExecutor {
    private final BlockyHockey plugin;
    private int timerTaskId;

    public PeriodTestCommand(BlockyHockey plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender)
            return false;
        if (label.equalsIgnoreCase("periodtest")) {
            Player p = (Player) sender;
            BukkitScheduler scheduler = Bukkit.getScheduler();
            Timer timer = new Timer(5, 0, 0);
            timerTaskId = scheduler.scheduleSyncRepeatingTask(
                    plugin,
                    () -> {
                        boolean periodEnd = timer.advanceTime();
                        if (periodEnd) {
                            scheduler.cancelTask(timerTaskId);
                            timerTaskId = 0;
                            Bukkit.broadcastMessage("The period ended !");
                        }
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(timer.getTimeString()));
                    },
                    1L,2L
            );
            return true;
        }
        return false;
    }
}
