package irisia.utils;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public final class RotationHelper {
    protected static Minecraft mc = Minecraft.getMinecraft();

    public static float[] findRotations(final Entity entity) {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        final double xDif = entity.posX - player.posX;
        final double zDif = entity.posZ - player.posZ;

        final AxisAlignedBB entityBB = entity.getEntityBoundingBox().expand(0.1F, 0.1F, 0.1F);
        final double playerEyePos = (player.posY + player.getEyeHeight());
        final double yDif = playerEyePos > entityBB.maxY ? entityBB.maxY - playerEyePos : // Higher than max, aim at max
                playerEyePos < entityBB.minY ? entityBB.minY - playerEyePos : // Lower than min, aim at min
                        0; // Else aim straight

        final double fDist = MathHelper.sqrt_double(xDif * xDif + zDif * zDif);

        return new float[]{
                (float) (StrictMath.atan2(zDif, xDif) * (180.0D / Math.PI)) - 90.0F,
                (float) (-(StrictMath.atan2(yDif, fDist) * (180.0D / Math.PI)))
        };
    }

    public static float[] findRotations(double posX, double posY, double posZ) {
        double x = posX - mc.thePlayer.posX, z = posZ - mc.thePlayer.posZ, y = posY + mc.thePlayer.getEyeHeight() - mc.thePlayer.posY;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (MathHelper.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-(MathHelper.atan2(y, d3) * 180.0D / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float getYawBetween(final float yaw, final double srcX, final double srcZ, final double destX, final double destZ) {
        final double xDist = destX - srcX;
        final double zDist = destZ - srcZ;
        final float var1 = (float) (StrictMath.atan2(zDist, xDist) * 180.0D / Math.PI) - 90.0F;
        return yaw + MathHelper.wrapAngleTo180_float(var1 - yaw);
    }

    public static float[] findRotations(final Vec3 vector) {
        final double posX = vector.xCoord;
        final double posY = vector.yCoord;
        final double posZ = vector.zCoord;

        final EntityPlayerSP player = mc.thePlayer;

        final double diffX = posX - player.posX;
        final double diffY = posY - (player.posY + player.getEyeHeight());
        final double diffZ = posZ - player.posZ;

        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) *
                180.0 / Math.PI) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) *
                180.0 / Math.PI));

        return new float[] { player.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - player.rotationYaw), player.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - player.rotationPitch) };
    }

    public static float[] applyRotations(final Entity entity, final float aimSpeed, final float prevYaw, final float prevPitch, final boolean gcdModulo) {
        float[] rawRotations = findRotations(entity);
        float[] optimalRotations = {
                interpolateAngle(prevYaw, rawRotations[0], aimSpeed),
                interpolateAngle(prevPitch, rawRotations[1], aimSpeed)
        };
        if(gcdModulo) return applyGCD(optimalRotations, new float[]{entity.prevRotationYaw, entity.prevRotationPitch}); else return optimalRotations;
    }

    public static float[] applyRotations(final float[] rawRotations, final Entity entity, final float aimSpeed, final float prevYaw, final float prevPitch) {
        float[] optimalRotations = {
                interpolateAngle(prevYaw, rawRotations[0], aimSpeed),
                interpolateAngle(prevPitch, rawRotations[1], aimSpeed)
        };
        return applyGCD(optimalRotations, new float[]{entity.prevRotationYaw, entity.prevRotationPitch});
    }

    public static void setAnglesClientside(final float yaw, final float pitch, final float speed) {
        mc.thePlayer.rotationYawHead = yaw;
        mc.thePlayer.renderYawOffset = yaw;
        mc.thePlayer.rotationPitchHead = pitch;
    }

    public static float interpolateAngle(float current, float intended, float factor) {
        float dif = MathHelper.wrapAngleTo180_float(intended - current);
        if (dif > factor) dif = factor;
        if (dif < -factor) dif = -factor;
        return current + dif;
    }

    public static double getMouseGCD() {
        final float sens = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        final float pow = sens * sens * sens * 8.0F;
        return pow * 0.15D;
    }

    public static float[] applyGCD(final float[] rotations, final float[] prevRots) {
        final float yawDif = rotations[0] - prevRots[0];
        final float pitchDif = rotations[1] - prevRots[1];
        final double gcd = getMouseGCD();

        rotations[0] -= yawDif % gcd;
        rotations[1] -= pitchDif % gcd;
        return rotations;
    }
}
