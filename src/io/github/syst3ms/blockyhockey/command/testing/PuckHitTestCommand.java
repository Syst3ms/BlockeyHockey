package io.github.syst3ms.blockyhockey.command.testing;

import io.github.syst3ms.blockyhockey.BlockyHockey;
import io.github.syst3ms.blockyhockey.util.VectorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicReference;

public class PuckHitTestCommand implements CommandExecutor {
    private final BlockyHockey pluginInstance;
    private int particleTask;

    public PuckHitTestCommand(BlockyHockey pluginInstance) {
        this.pluginInstance = pluginInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender)
            return false;
        if (label.equalsIgnoreCase("puckhittest")) {
            Player p = (Player) sender;
            boolean pass = args.length == 0 || args[0].equalsIgnoreCase("pass");
            Location loc = p.getLocation();
            Endermite puck = loc.getWorld().spawn(loc, Endermite.class, e -> {
                e.setCustomName("Puck");
                e.setCustomNameVisible(true);
                e.setCollidable(false);
                e.setSilent(true);
                e.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                 .addModifier(new AttributeModifier("still", -10, AttributeModifier.Operation.ADD_NUMBER));
            });
            particleTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                    pluginInstance,
                    () -> {
                        if (puck.isDead()) {
                            Bukkit.getScheduler().cancelTask(particleTask);
                            return;
                        }
                        Location l = puck.getLocation().add(0, puck.getHeight() / 2, 0);
                        l.getWorld().spawnParticle(
                                Particle.BLOCK_CRACK,
                                l,
                                0,
                                Float.MIN_VALUE,
                                0,
                                0,
                                1
                        );
                    },
                    1L,
                    1L
            );
            Bukkit.getScheduler().runTaskLater(
                    pluginInstance,
                    () -> {
                        Vector vel = VectorUtils.calculateVelocity(loc, pass);
                        puck.setVelocity(puck.getVelocity().add(vel));
                    },
                    30L
            );
        }
        return false;
    }
}
