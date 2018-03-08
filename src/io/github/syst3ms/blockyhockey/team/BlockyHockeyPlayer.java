package io.github.syst3ms.blockyhockey.team;

import io.github.syst3ms.blockyhockey.team.enums.BlockyHockeyTeam;
import io.github.syst3ms.blockyhockey.team.enums.PlayerStatus;
import io.github.syst3ms.blockyhockey.team.enums.TeamRole;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class BlockyHockeyPlayer {
	public static final int STICK_SLOT = 0;
	private final Player player;
	private BlockyHockeyTeam team;
	private TeamRole role;
	private PlayerStatus status;
	private boolean isRef;

	public BlockyHockeyPlayer(Player player, BlockyHockeyTeam team, TeamRole role, boolean isRef) {
		this.player = player;
		this.team = team;
		this.role = role;
		this.isRef = isRef;
		this.status = PlayerStatus.NOT_PLAYING;
	}

	public void equip() {
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setHeldItemSlot(STICK_SLOT);
		inventory.setItemInMainHand(role.getStickItem());
		inventory.setArmorContents(team.getArmor());
		player.updateInventory();
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}

	public BlockyHockeyTeam getTeam() {
		return team;
	}

	public void setTeam(BlockyHockeyTeam team) {
		this.team = team;
	}

	public TeamRole getRole() {
		return role;
	}

	public void setRole(TeamRole role) {
		this.role = role;
	}

	public boolean isRef() {
		return isRef;
	}

	public void setRef(boolean ref) {
		isRef = ref;
	}

	public Player getPlayer() {
		return player;
	}
}
