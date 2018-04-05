package io.github.syst3ms.blockyhockey.game;

import io.github.syst3ms.blockyhockey.team.enums.BlockyHockeyTeam;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class GoalCounter {
	private int homeScore, awayScore;
	private BlockyHockeyTeam home, away;

	public GoalCounter(BlockyHockeyTeam home, BlockyHockeyTeam away) {
		this.home = home;
		this.away = away;
	}

	public String getScoreString() {
		ComponentBuilder builder = new ComponentBuilder(String.valueOf(homeScore));
		if (homeScore >= awayScore) {
			builder.bold(true);
		}
		builder.color(home.getChatColor());
		builder.append(" - ", ComponentBuilder.FormatRetention.NONE);
		builder.append(String.valueOf(awayScore));
		if (awayScore >= homeScore) {
			builder.bold(true);
		}
		builder.color(away.getChatColor());
		return BaseComponent.toLegacyText(builder.create());
	}

	public void addScore(int amount, BlockyHockeyTeam team) {
	    if (team == home) {
	        homeScore += amount;
        } else if (team == away) {
	        awayScore += amount;
        } else {
            throw new IllegalArgumentException("The team '" + team.getDisplayName() + "' is not playing !");
        }
    }

    public void setScore(int amount, BlockyHockeyTeam team) {
        if (team == home) {
            homeScore = amount;
        } else if (team == away) {
            awayScore = amount;
        } else {
            throw new IllegalArgumentException("The team '" + team.getDisplayName() + "' is not playing !");
        }
    }

	public int getHomeScore() {
		return homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public BlockyHockeyTeam getHome() {
		return home;
	}

	public BlockyHockeyTeam getAway() {
		return away;
	}

	public BlockyHockeyTeam getWinningTeam() {
		return homeScore > awayScore ? home : awayScore > homeScore ? away : null;
	}
}
