package io.github.syst3ms.blockyhockey;

import io.github.syst3ms.blockyhockey.command.TeamCommand;
import io.github.syst3ms.blockyhockey.listener.ConnectListener;
import io.github.syst3ms.blockyhockey.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockyHockey extends JavaPlugin {
	@Override
	public void onEnable() {
		TeamManager teamManager = new TeamManager();
		Bukkit.getPluginManager().registerEvents(new ConnectListener(teamManager),this);
		getCommand("team").setExecutor(new TeamCommand(teamManager));
	}
}
