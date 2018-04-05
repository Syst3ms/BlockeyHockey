package io.github.syst3ms.blockyhockey.game;

import io.github.syst3ms.blockyhockey.BlockyHockey;
import io.github.syst3ms.blockyhockey.team.enums.BlockyHockeyTeam;
import io.github.syst3ms.blockyhockey.util.LocationUtils;
import io.github.syst3ms.blockyhockey.util.SchedulerUtils;
import io.github.syst3ms.blockyhockey.util.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {
    private static Location puckLocation = new Location(Bukkit.getWorlds().get(0), 0.5, 0.5, 0.5);
    private static int[][] goalRanges = new int[][] {{0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}};
	// Managers
	private final BlockyHockey pluginInstance;
	private final GoalCounter goalCounter;
	private final BukkitScheduler scheduler;
    private final AttributeModifier followRangeModifier = new AttributeModifier("followRange", 0, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
    private final AttributeModifier movementSpeedModifier = new AttributeModifier("movementSpeed", 0, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
    private Timer timer;
	// Periods
	private int period = 1;
    private UUID puckUuid;
	private List<Player> playing = new ArrayList<>();
	// Task
	private int titleTaskId, timerTaskId, pausedTimerTaskId, puckDetectTaskId;

    public GameManager(BlockyHockey pluginInstance, BlockyHockeyTeam home, BlockyHockeyTeam away) {
		this.pluginInstance = pluginInstance;
		this.timer = new Timer(false);
		this.goalCounter = new GoalCounter(home, away);
		scheduler = Bukkit.getScheduler();
	}

    public static void setPuckLocation(Location puckLocation) {
        GameManager.puckLocation = puckLocation;
    }

    public UUID getPuckUuid() {
        return puckUuid;
    }

	public void removePuck() {
		Entity puckEntity = Bukkit.getEntity(puckUuid);
		if (puckEntity != null) {
			puckEntity.remove();
			Bukkit.getScheduler().cancelTask(puckDetectTaskId);
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
					spawnPuckEntity(puckLocation);
					startPuckDetection();
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
        loc.getWorld().spawn(loc, Endermite.class, e -> {
            e.setCustomName("Puck");
            e.setCustomNameVisible(true);
            e.setAI(false);
            e.setCollidable(true);
            e.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).addModifier(followRangeModifier);
            e.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(movementSpeedModifier);
            puckUuid = e.getUniqueId();
        });
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
		respawnPuck();
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
							for (Player player : playing) {
							    player.sendTitle(ChatColor.DARK_AQUA + "Tie", goalCounter.getScoreString(), 5, 50, 5);
                            }
						} else {
							for (Player player : playing) {
								player.sendTitle(winning.getChatName() + "§r win !", goalCounter.getScoreString(), 5, 50, 5);
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
        Bukkit.getScheduler().cancelTask(pausedTimerTaskId);
        pausedTimerTaskId = 0;
		period = 1;
		respawnPuck();
		timer = new Timer(3, 0, 0);
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

	public void pauseTimer(boolean linger) {
		Bukkit.getScheduler().cancelTask(timerTaskId);
		Bukkit.getScheduler().cancelTask(puckDetectTaskId);
		if (linger) {
            TextComponent timerComponent = new TextComponent(timer.getTimeString());
            timerComponent.setColor(ChatColor.GRAY);
            pausedTimerTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(pluginInstance, () -> {
                for (Player player : playing) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, timerComponent);
                }
            }, 1L, 30L);
        }
	}

	public void startPuckDetection() {
        Entity puck = Bukkit.getEntity(puckUuid);
        puckDetectTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                pluginInstance,
                () -> {
                    Location loc = puck.getLocation();
                    for (int i = 0; i < goalRanges.length; i++) {
                        if (LocationUtils.isInBox(loc, goalRanges[i])) {
                            scoreGoal(i == 0 ? goalCounter.getHome() : goalCounter.getAway());
                        }
                    }
                },
                1L,
                1L
        );
    }

    public void scoreGoal(BlockyHockeyTeam team) {
        pauseTimer(false);
        goalCounter.addScore(1, team);
        SchedulerUtils.scheduleForTimes(
                pluginInstance,
                () -> {
                    for (Player p : playing) {
                        p.sendTitle(
                                "§b§k!!! §r§b" + team.getChatName() + " Goal ! §k!!!",
                                goalCounter.getScoreString(),
                                6,
                                8,
                                6
                        );
                    }
                },
                1L,
                20L,
                5
        );
    }

    // Cleanup

	public void finishGame() {
		removePuck();
		period = 1;
		// TODO tp players
	}

	public List<Player> getPlaying() {
		return playing;
	}
}
