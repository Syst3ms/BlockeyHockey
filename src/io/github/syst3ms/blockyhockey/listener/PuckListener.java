package io.github.syst3ms.blockyhockey.listener;

import io.github.syst3ms.blockyhockey.game.GameManager;
import io.github.syst3ms.blockyhockey.team.BlockyHockeyPlayer;
import io.github.syst3ms.blockyhockey.team.TeamManager;
import io.github.syst3ms.blockyhockey.util.VectorUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class PuckListener implements Listener {
    private final GameManager gameManager;
    private final TeamManager teamManager;

    public PuckListener(GameManager gameManager, TeamManager teamManager) {
        this.gameManager = gameManager;
        this.teamManager = teamManager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) ||
            gameManager.getPuckUuid().equals(event.getEntity().getUniqueId()))
            return;
        Player p = (Player) event.getDamager();
        BlockyHockeyPlayer bhPlayer = teamManager.getPlayer(p);
        PlayerInventory inv = p.getInventory();
        Material stickType = bhPlayer.getRole().getStickType();
        if (inv.getItemInMainHand().getType() != stickType &&
            inv.getItemInOffHand().getType() != stickType)
            return;
        event.setCancelled(true);
        Vector vel = VectorUtils.calculateVelocity(event.getDamager().getLocation(), false);
        event.getEntity().setVelocity(vel);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event) {
        if (!event.getRightClicked().getUniqueId().equals(gameManager.getPuckUuid()))
            return;
        Player p = event.getPlayer();
        BlockyHockeyPlayer bhPlayer = teamManager.getPlayer(p);
        PlayerInventory inv = p.getInventory();
        Material stickType = bhPlayer.getRole().getStickType();
        if (inv.getItemInMainHand().getType() != stickType &&
            inv.getItemInOffHand().getType() != stickType)
            return;
        event.setCancelled(true);
        Vector vel = VectorUtils.calculateVelocity(event.getPlayer().getLocation(), true);
        event.getRightClicked().setVelocity(vel);
    }
}
