package io.github.syst3ms.blockyhockey.team;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.syst3ms.blockyhockey.team.enums.BlockyHockeyTeam;
import io.github.syst3ms.blockyhockey.team.enums.TeamRole;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeamManager {
	private Multimap<BlockyHockeyTeam, BlockyHockeyPlayer> teamMap = HashMultimap.create();
	private Map<Player, BlockyHockeyPlayer> playerMap = new HashMap<>();

	public void createPlayer(Player p) {
		BlockyHockeyPlayer bhPlayer = new BlockyHockeyPlayer(p, BlockyHockeyTeam.NONE, TeamRole.NONE, p.isOp());
		teamMap.put(BlockyHockeyTeam.NONE, bhPlayer);
		playerMap.put(p, bhPlayer);
	}

	public void deletePlayer(Player p) {
		BlockyHockeyPlayer bhPlayer = playerMap.remove(p);
		teamMap.remove(bhPlayer.getTeam(), bhPlayer);
	}

	public BlockyHockeyPlayer getPlayer(Player p) {
		return playerMap.get(p);
	}
}
