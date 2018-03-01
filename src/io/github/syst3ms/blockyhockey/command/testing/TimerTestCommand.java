package io.github.syst3ms.blockyhockey.command.testing;

import io.github.syst3ms.blockyhockey.BlockyHockey;
import io.github.syst3ms.blockyhockey.game.timer.OvertimeTimer;
import io.github.syst3ms.blockyhockey.game.timer.StandardTimer;
import io.github.syst3ms.blockyhockey.game.timer.Timer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TimerTestCommand implements CommandExecutor {
	private final BlockyHockey pluginInstance;
	private int taskId;

	public TimerTestCommand(BlockyHockey pluginInstance) {
		this.pluginInstance = pluginInstance;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof ConsoleCommandSender)
			return false;
		if (s.equalsIgnoreCase("timertest")) {
			Player p = (Player) commandSender;
			Timer timer = strings.length  == 0 ? new StandardTimer(1, 30, 0) : new OvertimeTimer();
			taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
				pluginInstance,
				() -> {
					boolean stop = timer.advanceTime();
					if (stop) {
						Bukkit.getScheduler().cancelTask(taskId);
					}
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(timer.getTimeString()));
				},
				1L,
				2L
			);
			return true;
		}
		return false;
	}
}
