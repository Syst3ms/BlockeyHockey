package io.github.syst3ms.blockyhockey.game;

import io.github.syst3ms.blockyhockey.BlockyHockey;
import io.github.syst3ms.blockyhockey.game.timer.OvertimeTimer;
import io.github.syst3ms.blockyhockey.game.timer.StandardTimer;
import io.github.syst3ms.blockyhockey.game.timer.Timer;
import io.github.syst3ms.blockyhockey.team.enums.BlockyHockeyTeam;
import io.github.syst3ms.blockyhockey.util.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {
	public static final Location PUCK_LOCATION = new Location(Bukkit.getWorlds().get(0), 0.5, 0.5, 0.5);
	// Managers
	private final BlockyHockey pluginInstance;
	private final GoalCounter goalCounter;
	private Timer timer;
	// Periods
	private int period = 1;
	private boolean overtime;
	private UUID puckUuid;
	private List<Player> playing = new ArrayList<>();
	// Task
	private int titleTaskId, timerTaskId, pausedTimerTaskId;
	private final BukkitScheduler scheduler;

	public GameManager(BlockyHockey pluginInstance, BlockyHockeyTeam home, BlockyHockeyTeam away) {
		this.pluginInstance = pluginInstance;
		this.timer = new StandardTimer();
		this.goalCounter = new GoalCounter(home, away);
		scheduler = Bukkit.getScheduler();
	}

	public void removePuck() {
		Entity puckEntity = Bukkit.getEntity(puckUuid);
		if (puckEntity != null) {
			puckEntity.remove();
		}
	}

	public void respawnPuck() {
		AtomicInteger count = new AtomicInteger();
		final String[] hourglasses = {
			"⧖", "⋈",
			"⧖.", "⋈.",
			"⧖..", "⋈..",
			"⧖...", "⋈...",
			"⧖....", "⋈...."
		};
		titleTaskId = scheduler.scheduleSyncRepeatingTask(
			pluginInstance,
			() -> {
				int i = count.getAndIncrement();
				if (i == 10) {
					scheduler.cancelTask(titleTaskId);
					titleTaskId = 0;
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
			},
			1L,
			10L
		);
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
		ComponentBuilder builder = new ComponentBuilder(String.valueOf(remainingSeconds));
		builder.bold(true)
			   .color(remainingSeconds == 3 ? ChatColor.GREEN
											: remainingSeconds == 2 ? ChatColor.GOLD : ChatColor.DARK_RED);
		return builder.create()[0].toLegacyText();
	}

	// Timer

	public void startTimer() {
		scheduler.cancelTask(pausedTimerTaskId);
		pausedTimerTaskId = 0;
		timerTaskId = scheduler.scheduleSyncRepeatingTask(
			pluginInstance,
			() -> {
				boolean periodEnd = timer.advanceTime();
				if (periodEnd) {
					scheduler.cancelTask(timerTaskId);
					timerTaskId = 0;
					if (period < 3) {
						for (Player player : playing) {
							player.sendTitle("End of the " +
											 StringUtils.ordinal(period) +
											 " period", goalCounter.getScoreString(), 5, 50, 10);
						}
						period++;
						removePuck();
						scheduler.runTaskLater(
							pluginInstance,
							() -> {
								for (Player player : playing) {
									player.sendTitle("Starting the " +
													 StringUtils.ordinal(period) +
													" period", goalCounter.getScoreString(), 2, 8, 2);
								}
							},
							10 * 20 - 15
						);
						scheduler.runTaskLater(
							pluginInstance,
							this::respawnPuck,
							10 * 20
						);
					} else { // End of game
						BlockyHockeyTeam winning = goalCounter.getWinningTeam();
						if (winning == null) { // Tie
							startOvertime();
						} else {
							for (Player player : playing) {
								player.sendTitle(winning.getChatName() + "§r win !", "", 5, 4 * 20, 5);
							}
							finishGame();
						}
					}
				}
			},
			1L,2L
		);
	}

	public void startOvertime() {
		overtime = true;
		period = 1;
		timer = new OvertimeTimer();
		timerTaskId = scheduler.scheduleSyncRepeatingTask(
			pluginInstance,
			() -> {
				boolean periodEnd = timer.advanceTime();
				if (periodEnd) {
					scheduler.cancelTask(timerTaskId);
					timerTaskId = 0;
					for (Player player : playing) {
						player.sendTitle("End of the " +
										 StringUtils.ordinal(period) +
										 " overtime", goalCounter.getScoreString(), 5, 50, 10);
					}
					period++;
					removePuck();
					scheduler.runTaskLater(
						pluginInstance,
						() -> {
							for (Player player : playing) {
								player.sendTitle("Starting the " +
												 StringUtils.ordinal(period) +
												 " overtime", goalCounter.getScoreString(), 2, 8, 2);
							}
						},
						10 * 20 - 15
					);
					scheduler.runTaskLater(
						pluginInstance,
						this::respawnPuck,
						10 * 20
					);
				}
				for (Player player : playing) {
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(timer.getTimeString()));
				}
			},
			1L,2L
		);
	}

	public void pauseTimer() {
		Bukkit.getScheduler().cancelTask(timerTaskId);
		TextComponent timerComponent = new TextComponent(timer.getTimeString());
		timerComponent.setColor(ChatColor.GRAY);
		pausedTimerTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
			pluginInstance,
			() -> {
				for (Player player : playing) {
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, timerComponent);
				}
			},
			1L,
			30L
		);
	}

	// Cleanup

	public void finishGame() {
		removePuck();
		period = 1;
		overtime = false;
		// TODO tp players
	}

	public List<Player> getPlaying() {
		return playing;
	}
}
