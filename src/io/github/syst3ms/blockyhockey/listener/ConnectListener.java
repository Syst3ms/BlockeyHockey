package io.github.syst3ms.blockyhockey.listener;

import io.github.syst3ms.blockyhockey.team.BlockyHockeyPlayer;
import io.github.syst3ms.blockyhockey.team.TeamManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectListener implements Listener {
	private final TeamManager manager;

	public ConnectListener(TeamManager manager) {
		this.manager = manager;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		manager.createPlayer(event.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event)  {
		manager.deletePlayer(event.getPlayer());
	}
}
