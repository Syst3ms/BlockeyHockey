package io.github.syst3ms.blockyhockey.game;

import io.github.syst3ms.blockyhockey.BlockyHockey;
import io.github.syst3ms.blockyhockey.team.enums.BlockyHockeyTeam;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {
	public static final Integer[] DEFAULT_TIME = {2, 30, 0};
	public static final Location PUCK_LOCATION = new Location(Bukkit.getWorlds().get(0), 0.5, 0.5, 0.5);
	private final BlockyHockey pluginInstance;
	private Timer timer;
	private UUID puckUuid;
	private BlockyHockeyTeam home = BlockyHockeyTeam.NONE, away = BlockyHockeyTeam.NONE;
	private List<Player> playing = new ArrayList<>();
	private int homeScore, awayScore;
	private int titleTaskId, timerTaskId;


	public GameManager(BlockyHockey pluginInstance) {
		this.pluginInstance = pluginInstance;
		timer = new StandardTimer(DEFAULT_TIME[0], DEFAULT_TIME[1], DEFAULT_TIME[2]);
	}

	public void respawnPuck() {
		BukkitScheduler scheduler = Bukkit.getScheduler();
		AtomicInteger count = new AtomicInteger();
		final String[] hourglasses = {
			"⧖", "⋈",
			"⧖.", "⋈.",
			"⧖..", "⋈..",
			"⧖...", "⋈...",
			"⧖....", "⋈...."
		};
		titleTaskId = scheduler.scheduleSyncRepeatingTask(pluginInstance, () -> {
			int i = count.getAndIncrement();
			if (i == 10) {
				scheduler.cancelTask(titleTaskId);
				titleTaskId = -1;
				for (Player p : playing) {
					p.sendTitle(
						"Spawning puck " + hourglasses[0],
						ChatColor.BOLD + "" + ChatColor.GREEN + "> GO <",
						2,
						10,
						2
					);
				}
				spawnPuckEntity(PUCK_LOCATION);
				return;
			}
			String hourglass = hourglasses[i];
			for (Player p : playing) {
				p.sendTitle(
					"Spawning puck " + hourglass,
					getSubtitleText(5 - i/	2),
					0,
					5,
					0
				);
			}
		}, 1L, 10L);
	}

	private void spawnPuckEntity(Location loc) {
		Endermite puck = loc.getWorld().spawn(loc, Endermite.class, e -> {
			e.setCustomName("Puck");
			e.setCustomNameVisible(true);
			e.setAI(false);
			e.setCollidable(true);
			puckUuid = e.getUniqueId();
		});
		Bukkit.getScheduler().runTaskLater(
			pluginInstance,
			() -> puck.setGravity(false),
			10L
		);
	}

	private String getSubtitleText(int remainingSeconds) {
		if (remainingSeconds > 3) {
			return "";
		}
		ComponentBuilder builder = new ComponentBuilder("");
		builder.append(String.valueOf(remainingSeconds))
			   .bold(true)
			   .color(remainingSeconds == 3 ? ChatColor.GREEN
											: remainingSeconds == 2 ? ChatColor.GOLD : ChatColor.DARK_RED);
		return builder.create()[0].toLegacyText();
	}

	// Timer

	public void startTimer() {
		BukkitScheduler scheduler = Bukkit.getScheduler();
		timerTaskId = scheduler.scheduleSyncRepeatingTask(
			pluginInstance,
			() -> {

			},
			1L,2L
		)
	}

	public BlockyHockeyTeam getHome() {
		return home;
	}

	public void setHome(BlockyHockeyTeam home) {
		this.home = home;
	}

	public BlockyHockeyTeam getAway() {
		return away;
	}

	public void setAway(BlockyHockeyTeam away) {
		this.away = away;
	}

	public List<Player> getPlaying() {
		return playing;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}
}
