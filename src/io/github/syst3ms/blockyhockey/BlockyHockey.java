package io.github.syst3ms.blockyhockey;

import io.github.syst3ms.blockyhockey.command.TeamCommands;
import io.github.syst3ms.blockyhockey.command.testing.TimerTestCommand;
import io.github.syst3ms.blockyhockey.listener.ConnectListener;
import io.github.syst3ms.blockyhockey.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockyHockey extends JavaPlugin {
	private TeamManager teamManager;

	@Override
	public void onEnable() {
		teamManager = new TeamManager();
		Bukkit.getPluginManager().registerEvents(new ConnectListener(teamManager),this);
		registerCommands();
	}

	private void registerCommands() {
		TeamCommands teamCommands = new TeamCommands(teamManager);
		getCommand("team").setExecutor(teamCommands);
		getCommand("teams").setExecutor(teamCommands);
		TimerTestCommand timerTestCommand = new TimerTestCommand(this);
		getCommand("timertest").setExecutor(timerTestCommand);
	}
}
