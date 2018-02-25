package io.github.syst3ms.blockyhockey.command;

import io.github.syst3ms.blockyhockey.team.BlockyHockeyPlayer;
import io.github.syst3ms.blockyhockey.team.TeamManager;
import io.github.syst3ms.blockyhockey.team.enums.BlockyHockeyTeam;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {
	private final TeamManager manager;

	public TeamCommand(TeamManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (s.equalsIgnoreCase("team")) {
			if (commandSender instanceof ConsoleCommandSender) {
				commandSender.sendMessage("This command can't be used from the console !");
				return true;
			}
			Player p = (Player) commandSender;
			BlockyHockeyPlayer bhPlayer = manager.getPlayer(p);
			if (strings.length == 0) {
				p.sendMessage("§cPlease specify a team name !");
				return true;
			}
			String name = strings[0];
			BlockyHockeyTeam chosen = null;
			for (BlockyHockeyTeam team : BlockyHockeyTeam.values()) {
				if (team.name().replace('_', ' ').equalsIgnoreCase(name) || team.getDisplayName().equalsIgnoreCase(name)) {
					chosen = team;
					break;
				}
			}
			if (chosen == null) {
				p.sendMessage("§cCan't find a team by the name of '" + name + "' !");
				return true;
			}
			bhPlayer.setTeam(chosen);
			p.sendMessage("§aYou chose " + chosen.getChatName() + "§a as your team !");
			return true;
		}
		return false;
	}
}
