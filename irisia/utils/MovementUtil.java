package irisia.utils;

import irisia.event.system.impl.EventMove;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

public enum MovementUtil {
    getInstance();

    public static final Minecraft mc = Minecraft.getMinecraft();
    private final float BASE_MOVE_SPEED_NCP = 0.2873F;
    private final float BASE_MOVE_SPEED_SPRINT = 0.2875F;

    public final float getMoveYawDirection(){
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if(mc.thePlayer.moveForward < 0F)
            rotationYaw += 180F;
        float forced = 1F;
        if(mc.thePlayer.moveForward < 0F)
            forced = -0.5F;
        else if(mc.thePlayer.moveForward > 0F)
            forced = 0.5F;
        if(mc.thePlayer.moveStrafing > 0F)
            rotationYaw -= 90F * forced;
        if(mc.thePlayer.moveStrafing < 0F)
            rotationYaw += 90F * forced;
        return rotationYaw;
    }

    public float getPlayerDirection() {
        float direction = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward > 0) {
            if (mc.thePlayer.moveStrafing > 0) {
                direction -= 45;
            } else if (mc.thePlayer.moveStrafing < 0) {
                direction += 45;
            }
        } else if (mc.thePlayer.moveForward < 0) {
            if (mc.thePlayer.moveStrafing > 0) {
                direction -= 135;
            } else if (mc.thePlayer.moveStrafing < 0) {
                direction += 135;
            } else {
                direction -= 180;
            }
        } else {
            if (mc.thePlayer.moveStrafing > 0) {
                direction -= 90;
            } else if (mc.thePlayer.moveStrafing < 0) {
                direction += 90;
            }
        }

        return direction;
    }

    public void strafe(double speed) {
        /*
        forward = left
        back = right
        left = back
        right = jump
         */
        float a = mc.thePlayer.rotationYaw * 0.017453292F;
        float l = mc.thePlayer.rotationYaw * 0.017453292F - (float) Math.PI * 1.5f;
        float r = mc.thePlayer.rotationYaw * 0.017453292F + (float) Math.PI * 1.5f;
        float rf = mc.thePlayer.rotationYaw * 0.017453292F + (float) Math.PI * 0.19f;
        float lf = mc.thePlayer.rotationYaw * 0.017453292F + (float) Math.PI * -0.19f;
        float lb = mc.thePlayer.rotationYaw * 0.017453292F - (float) Math.PI * 0.76f;
        float rb = mc.thePlayer.rotationYaw * 0.017453292F - (float) Math.PI * -0.76f;
        if (mc.gameSettings.keyBindLeft.isPressed()) {
            if (mc.gameSettings.keyBindRight.isPressed() && !mc.gameSettings.keyBindJump.isPressed()) {
                mc.thePlayer.motionX -= (MathHelper.sin(lf) * speed);
                mc.thePlayer.motionZ += (MathHelper.cos(lf) * speed);
            } else if (mc.gameSettings.keyBindJump.isPressed() && !mc.gameSettings.keyBindBack.isPressed()) {
                mc.thePlayer.motionX -= (MathHelper.sin(rf) * speed);
                mc.thePlayer.motionZ += (MathHelper.cos(rf) * speed);
            } else {
                mc.thePlayer.motionX -= (MathHelper.sin(a) * speed);
                mc.thePlayer.motionZ += (MathHelper.cos(a) * speed);
            }
        } else if (mc.gameSettings.keyBindRight.isPressed()) {
            if (mc.gameSettings.keyBindRight.isPressed() && !mc.gameSettings.keyBindJump.isPressed()) {
                mc.thePlayer.motionX -= (MathHelper.sin(lb) * speed);
                mc.thePlayer.motionZ += (MathHelper.cos(lb) * speed);
            } else if (mc.gameSettings.keyBindJump.isPressed() && !mc.gameSettings.keyBindRight.isPressed()) {
                mc.thePlayer.motionX -= (MathHelper.sin(rb) * speed);
                mc.thePlayer.motionZ += (MathHelper.cos(rb) * speed);
            } else {
                mc.thePlayer.motionX += (MathHelper.sin(a) * speed);
                mc.thePlayer.motionZ -= (MathHelper.cos(a) * speed);
            }
        } else if (mc.gameSettings.keyBindRight.isPressed() && !mc.gameSettings.keyBindBack.isPressed() && !mc.gameSettings.keyBindLeft.isPressed() && !mc.gameSettings.keyBindRight.isPressed()) {
            mc.thePlayer.motionX += (MathHelper.sin(l) * speed);
            mc.thePlayer.motionZ -= (MathHelper.cos(l) * speed);
        } else if (mc.gameSettings.keyBindRight.isPressed() && !mc.gameSettings.keyBindBack.isPressed() && !mc.gameSettings.keyBindLeft.isPressed() && !mc.gameSettings.keyBindBack.isPressed()) {
            mc.thePlayer.motionX += (MathHelper.sin(r) * speed);
            mc.thePlayer.motionZ -= (MathHelper.cos(r) * speed);
        }

    }


    public final double getNCPBaseSpeed() {
        double base = BASE_MOVE_SPEED_NCP;
        if (mc.thePlayer != null && mc.thePlayer.isPotionActive(1)) {
            base *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return base;
    }

    public final double getBaseSpeed() {
        double base = BASE_MOVE_SPEED_SPRINT;
        if (mc.thePlayer != null && mc.thePlayer.isPotionActive(1)) {
            base *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return base;
    }

    public final boolean isPlayerMoving(){
        return ((mc.thePlayer.moveForward > 0 || mc.thePlayer.moveForward < 0) || (mc.thePlayer.moveStrafing > 0 || mc.thePlayer.moveStrafing < 0));
    }

    public void setSpeed(double speed, double forward, double strafe, float yaw) {
        if ((forward == 0.0D) && (strafe == 0.0D)) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D)  {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
        }
    }
    public void strafe(EventMove event, double speed) {
        float direction = (float) Math.toRadians(getPlayerDirection());

        if (isPlayerMoving()) {
            event.setX(mc.thePlayer.motionX = -Math.sin(direction) * speed);
            event.setZ(mc.thePlayer.motionZ = Math.cos(direction) * speed);
        } else {
            event.setX(mc.thePlayer.motionX = 0);
            event.setZ(mc.thePlayer.motionZ = 0);
        }
    }
    public final void setSpeed(final double speed){
        setSpeed(speed, mc.thePlayer.moveForward, mc.thePlayer.moveStrafing, mc.thePlayer.rotationYaw);
    }

    public final void setSpeed(final float speed){
        setSpeed(speed, mc.thePlayer.moveForward, mc.thePlayer.moveStrafing, mc.thePlayer.rotationYaw);
    }

    public float getSpeed(final Entity target) {
        return (float) Math.sqrt(target.motionX *target.motionX +target.motionZ *target.motionZ) * mc.timer.timerSpeed;
    }

    public final void setSpeed3(final double speed){
        setSpeed(speed, mc.thePlayer.moveForward, mc.thePlayer.moveStrafing, mc.thePlayer.rotationYaw);
    }

}
