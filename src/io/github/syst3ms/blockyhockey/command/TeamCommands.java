package io.github.syst3ms.blockyhockey.command;

import io.github.syst3ms.blockyhockey.team.BlockyHockeyPlayer;
import io.github.syst3ms.blockyhockey.team.TeamManager;
import io.github.syst3ms.blockyhockey.team.enums.BlockyHockeyTeam;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamCommands implements CommandExecutor {
	private final TeamManager manager;

	public TeamCommands(TeamManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
		if (s.equalsIgnoreCase("team")) {
			if (sender instanceof ConsoleCommandSender) {
				sender.sendMessage("This command can't be used from the console !");
				return true;
			}
			Player p = (Player) sender;
			BlockyHockeyPlayer bhPlayer = manager.getPlayer(p);
			if (strings.length == 0) {
				p.sendMessage("Your team : " + bhPlayer.getTeam().getChatName());
				return true;
			}
			String name = strings[0];
			if (name.equalsIgnoreCase("leave") || name.equalsIgnoreCase("quit")) {
				bhPlayer.setTeam(BlockyHockeyTeam.NONE);
				p.sendMessage("§aYou left your current team !");
				return true;
			}
			BlockyHockeyTeam chosen = null;
			for (BlockyHockeyTeam team : BlockyHockeyTeam.values()) {
				if (team.name().replace('_', ' ').equalsIgnoreCase(name) ||
					team.getDisplayName().equalsIgnoreCase(name)) {
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
		} else if (s.equalsIgnoreCase("teams")) {
			List<String> messages = new ArrayList<>(BlockyHockeyTeam.values().length + 1);
			messages.add("List of all teams :");
			for (BlockyHockeyTeam team : BlockyHockeyTeam.values()) {
				if (team == BlockyHockeyTeam.NONE)
					continue;
				messages.add("  - " + team.getChatName());
			}
			for (String message : messages) {
				if (sender instanceof ConsoleCommandSender)
					message = ChatColor.stripColor(message);
				sender.sendMessage(message);
			}

			return true;
		}
		return false;
	}
}
