package io.github.syst3ms.blockyhockey.command.testing;

import io.github.syst3ms.blockyhockey.game.GoalCounter;
import io.github.syst3ms.blockyhockey.team.enums.BlockyHockeyTeam;
import io.github.syst3ms.blockyhockey.util.SchedulerUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GoalTitleTestCommand implements CommandExecutor {
    private Plugin pluginInstance;

    public GoalTitleTestCommand(Plugin pluginInstance) {
        this.pluginInstance = pluginInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender)
            return false;
        if (label.equalsIgnoreCase("goaltitletest")) {
            Player p = (Player) sender;
            GoalCounter goalCounter = new GoalCounter(BlockyHockeyTeam.VANTA_VIKINGS, BlockyHockeyTeam.COURAGEOUS_COUGARS);
            goalCounter.addScore(1, BlockyHockeyTeam.VANTA_VIKINGS);
            String title = BaseComponent.toLegacyText(new ComponentBuilder("!!!")
                    .bold(true)
                    .obfuscated(true)
                    .append(" ", ComponentBuilder.FormatRetention.NONE)
                    .append(BlockyHockeyTeam.VANTA_VIKINGS.getName())
                    .color(BlockyHockeyTeam.VANTA_VIKINGS.getChatColor())
                    .bold(true)
                    .append(" Goal ! ", ComponentBuilder.FormatRetention.NONE)
                    .bold(true)
                    .append("!!!")
                    .obfuscated(true)
                    .create()
            );
            SchedulerUtils.scheduleForTimes(
                    pluginInstance,
                    () -> {
                        p.sendTitle(
                                title,
                                goalCounter.getScoreString(),
                                6,
                                8,
                                6
                        );
                    },
                    1L,
                    20L,
                    5
            );
            return true;
        }
        return false;
    }
}
