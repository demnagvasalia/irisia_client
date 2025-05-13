package irisia.utils;

import irisia.utils.Stopwatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class Animator {
    /*
  Credits: AmirCC (animation util)
   */
    Stopwatch stopwatch = new Stopwatch();
    public static float animate(float current, float end, float minSpeed) {
        float movement = (end - current) * 0.205f * 240 / Minecraft.getDebugFPS();;
        if (movement > 0) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return current + movement;
    }

    public static float animateHUD(float current, float end, float minSpeed) {
        float movement = (end - current) * 0.025f * 240 / Minecraft.getDebugFPS();

        if (movement > 0) {
            movement = Math.max(minSpeed, movement);movement = Math.min(end - current, movement);
        } else if (movement < 0) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return Float.isNaN(current + movement) ? 1 : current + movement;
    }

    public static float calculateCompensation(float target, float current, long delta, double speed) {
        float diff = current - target;
        if (delta < 1) {
            delta = 1;
        }
        if (delta > 1000) {
            delta = 16;
        }
        if (diff > speed) {
            double xD = (speed * delta / (1000 / 60) < 0.5 ? 0.5 : speed * delta / (1000 / 60));
            current -= xD;
            if (current < target) {
                current = target;
            }
        } else if (diff < -speed) {
            double xD = (speed * delta / (1000 / 60) < 0.5 ? 0.5 : speed * delta / (1000 / 60));
            current += xD;
            if (current > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public static double animateLinear(final double target, double current, double speed) {
        final boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        final double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            current += factor;
        } else {
            current -= factor;
        }
        return current;
    }

    public static double easeQuart(double current, double end, double minSpeed, double initialSpeed) {
        if(Minecraft.getDebugFPS() > 1) {
            double value = (end - current) * initialSpeed * 240 / Minecraft.getDebugFPS();
            if (value > 0) {
                value = Math.max(minSpeed, value);
                value = Math.min(end - current, value);
            } else if (value < 0) {
                value = Math.min(-minSpeed, value);
                value = Math.max(end - current, value);
            }
            return current + value;
        } else
            return 0;
    }

    public static float easeQuart(float current, float end, float minSpeed, float initialSpeed) {
        if(Minecraft.getDebugFPS() > 1) {
            float value = (end - current) * initialSpeed * 240 / Minecraft.getDebugFPS();
            if (value > 0) {
                value = Math.max(minSpeed, value);
                value = Math.min(end - current, value);
            } else if (value < 0) {
                value = Math.min(-minSpeed, value);
                value = Math.max(end - current, value);
            }
            return current + value;
        } else
            return 0;
    }

    public static float animateHUD(float current, float end, float minSpeed, float initialSpeed) {
        float movement = (end - current) * initialSpeed;
        if (movement > 0) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return current + movement;
    }

    public static float animateValue(float value, final float maxValue, final float speed) {
        float animationValue = value;
        float animationOffset = speed / Minecraft.getDebugFPS();
        if (animationValue < maxValue)
            animationValue += animationOffset;
        if (animationValue > maxValue)
            animationValue -= animationOffset;
        return animationValue;
    }

    public static float linearAnimation(float current, float end, float speed){
        if(current < end)
            current += 0.1;
        if(current > end)
            current -= 0.1;
        return current;
    }

    public static float progressiveEasing(final float start, final float end, final float initialSpeed) {
        float interpolated = (end - start) * initialSpeed * 240F / Minecraft.getDebugFPS(); /* we do this just so the animation is fast even if our fps rate is slow */;
        if (interpolated > 0) {
            interpolated = initialSpeed / 2;// + (interpolated / calculateExpansion(start,initialSpeed));
        } else if (interpolated < 0) {
            interpolated = -initialSpeed / 2 ;//+  (interpolated / calculateExpansion(start,initialSpeed));
        }
        return start + interpolated ;
    }

    public static float calculateExpansion(float base, float exp){
        return (float) Math.exp(base) * exp;
    }

    public static float test(final float base, final float factor){
        float result;
        if(base != 0) {
            result = Math.max(base * factor, base / factor);
        } else {
            result = Math.max(base * (base * factor), base / (base / factor));
        }
        return result;
    }
    public static void popUp(float base, ScaledResolution sr, Stopwatch stopwatch, Runnable... runnables){
        if(stopwatch.hasTimeElapsed(310L, false))
            base = animateValue(base, 1, 0.0004f);
        else
            base = animateValue(base, 1.03f, 0.005f);

        GL11.glPushMatrix();
        GL11.glTranslated(sr.getScaledWidth() / 2 - base * sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - base * sr.getScaledHeight() / 2,0);
        GL11.glScalef(base, base, base);
        for(Runnable runnable : runnables){
            runnable.run();
        }
        GL11.glPopMatrix();
    }
    public static float popUp(float base, Stopwatch stopwatch){
        if(stopwatch.hasTimeElapsed(340L, false))
            base = Animator.animateValue(base, 1f, 0.2f);
        else
            base = Animator.animateValue(base, 1.03f, 2);
        return base;
    }
    public float next(float normalizedTime) {
        if ((normalizedTime/=1) < (1/2.75f)) {
            return (7.5625f*normalizedTime*normalizedTime);
        } else if (normalizedTime < (2/2.75f)) {
            return (7.5625f*(normalizedTime-=(1.5f/2.75f))*normalizedTime + .75f);
        } else if (normalizedTime < (2.5/2.75)) {
            return (7.5625f*(normalizedTime-=(2.25f/2.75f))*normalizedTime + .9375f);
        } else {
            return (7.5625f*(normalizedTime-=(2.625f/2.75f))*normalizedTime + .984375f);
        }
    }
}
