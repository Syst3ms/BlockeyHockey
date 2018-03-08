package io.github.syst3ms.blockyhockey.command.testing;

import io.github.syst3ms.blockyhockey.team.BlockyHockeyPlayer;
import io.github.syst3ms.blockyhockey.team.TeamManager;
import io.github.syst3ms.blockyhockey.team.enums.BlockyHockeyTeam;
import io.github.syst3ms.blockyhockey.team.enums.TeamRole;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TeamEquipTestCommand implements CommandExecutor {
    private final TeamManager manager;

    public TeamEquipTestCommand(TeamManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender)
            return false;
        if (label.equalsIgnoreCase("teamequiptest")) {
            BlockyHockeyTeam chosen = BlockyHockeyTeam.VANTA_VIKINGS;
            if (args.length > 0) {
                String name = args[0];
                for (BlockyHockeyTeam team : BlockyHockeyTeam.values()) {
                    if (team.name().replace('_', ' ').equalsIgnoreCase(name) || team.getDisplayName().equalsIgnoreCase(name)) {
                        chosen = team;
                        break;
                    }
                }
            }
            sender.sendMessage("Equipping armor of " + chosen.getChatName());
            BlockyHockeyPlayer bhPlayer = manager.getPlayer((Player) sender);
            bhPlayer.setTeam(chosen);
            bhPlayer.setRole(TeamRole.OFFENSE);
            bhPlayer.equip();
            return true;
        }
        return false;
    }
}
