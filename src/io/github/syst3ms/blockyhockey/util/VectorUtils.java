package io.github.syst3ms.blockyhockey.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * With courtesy of bi0qaw
 */
public class VectorUtils {
    public static final double PASS_MODIFIER = 1.5;
    public static final double SHOOT_MODIFIER = 2;
    public static final double PI = Math.PI;
    public static final double DEG_TO_RAD = PI / 180;
    public static final double RAD_TO_DEG =  180 / PI;
    private static final Random rnd = new Random();

    public static float getYaw(Vector vector) {
        if (((Double) vector.getX()).equals((double) 0) && ((Double) vector.getZ()).equals((double) 0)){
            return 0;
        }
        return (float) (Math.atan2(vector.getZ(), vector.getX()) * RAD_TO_DEG);
    }

    public static Vector fromYawAndPitch(float yaw, float pitch) {
        double y = Math.sin(pitch * DEG_TO_RAD);
        double div = Math.cos(pitch * DEG_TO_RAD);
        double x = Math.cos(yaw * DEG_TO_RAD);
        double z = Math.sin(yaw * DEG_TO_RAD);
        return new Vector(x,0,z).multiply(div).setY(y);
    }

    public static float fromNotchYaw(float notchYaw){
        float y = notchYaw + 90;
        if (y > 180){
            y -= 360;
        }
        return y;
    }

    public static float fromNotchPitch(float notchPitch){
        return -notchPitch;
    }

    /*
     * Passing :
     *   - 10 % chance of going upwards between 0 and -5 of MC pitch
     *   - 25 % chance of going outwards between -5 and 5 of MC yaw
     *
     * Shooting :
     *   - 80 % chance of going upwards between 0 and -10 of MC pitch
     *     OR
     *   - 20 % chance of going upwards between 0 and -20 of MC pitch
     *   - 90 % chance of going outwards between -10 and 10 of MC yaw
     *     OR
     *   - 10 % chance of going outwards between -15 and 15 of MC yaw
     */
    public static Vector calculateVelocity(Location shooter, boolean pass) {
        float shooterYaw = shooter.getYaw();
        double upwardRandom = Math.random();
        double outwardRandom = Math.random();
        float pitchModifier = 0;
        float yawModifier = 0;
        if (pass) {
            if (upwardRandom <= 0.1) {
                pitchModifier = rnd.nextInt(5) - 5;
            }
            if (outwardRandom <= 0.25) {
                yawModifier = rnd.nextInt(10) - 5;
            }
        } else {
            if (upwardRandom <= 0.8) {
                pitchModifier = rnd.nextInt(10) - 10;
            } else {
                pitchModifier = rnd.nextInt(20) - 20;
            }
            if (upwardRandom <= 0.9) {
                yawModifier = rnd.nextInt(20) - 10;
            } else {
                yawModifier = rnd.nextInt(30) - 15;
            }
        }
        return fromYawAndPitch(
                fromNotchYaw(shooterYaw + yawModifier),
                fromNotchPitch(pitchModifier)
        ).normalize().multiply(pass ? PASS_MODIFIER : SHOOT_MODIFIER);
    }
}
