package io.github.syst3ms.blockyhockey;

import io.github.syst3ms.blockyhockey.command.TeamCommands;
import io.github.syst3ms.blockyhockey.command.testing.GoalTitleTestCommand;
import io.github.syst3ms.blockyhockey.command.testing.PeriodTestCommand;
import io.github.syst3ms.blockyhockey.command.testing.PuckHitTestCommand;
import io.github.syst3ms.blockyhockey.command.testing.SpawningPuckTitleTest;
import io.github.syst3ms.blockyhockey.command.testing.TeamEquipTestCommand;
import io.github.syst3ms.blockyhockey.command.testing.TimerTestCommand;
import io.github.syst3ms.blockyhockey.game.GameManager;
import io.github.syst3ms.blockyhockey.listener.ConnectListener;
import io.github.syst3ms.blockyhockey.team.TeamManager;
import io.github.syst3ms.blockyhockey.team.enums.BlockyHockeyTeam;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockyHockey extends JavaPlugin {
	private TeamManager teamManager;
	private GameManager gameManager;

	@Override
	public void onEnable() {
		teamManager = new TeamManager();
		gameManager = new GameManager(this, BlockyHockeyTeam.NONE, BlockyHockeyTeam.NONE);
		Bukkit.getPluginManager().registerEvents(new ConnectListener(teamManager), this);
		//Bukkit.getPluginManager().registerEvents(new PuckListener(gameManager, teamManager), this);
		registerCommands();
	}

	private void registerCommands() {
		TeamCommands teamCommands = new TeamCommands(teamManager);
		getCommand("team").setExecutor(teamCommands);
		getCommand("teams").setExecutor(teamCommands);
        registerTestCommands();
	}

    private void registerTestCommands() {
        getCommand("timertest").setExecutor(new TimerTestCommand(this));
        getCommand("teamequiptest").setExecutor(new TeamEquipTestCommand(teamManager));
        getCommand("periodtest").setExecutor(new PeriodTestCommand(this));
        getCommand("spawnpucktitletest").setExecutor(new SpawningPuckTitleTest(this));
        getCommand("puckhittest").setExecutor(new PuckHitTestCommand(this));
        getCommand("goaltitletest").setExecutor(new GoalTitleTestCommand(this));
    }
}
