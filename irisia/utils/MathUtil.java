package irisia.utils;

import net.minecraft.util.MathHelper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtil {
    public static double calculateCompensation(float target, float current, long delta, double speed){
        float diff = current - target;
        if (delta < 1) {
            delta = 1;
        }
        if (delta > 1000) {
            delta = 16;
        }
        if (diff > speed) {
            double jebacczarnych = (speed * delta / (1000 / 60) < 0.5 ? 0.5 : speed * delta / (1000 / 60));
            current -= jebacczarnych;
            if (current < target) {
                current = target;
            }
        } else if (diff < -speed) {
            double jebacczarnych = (speed * delta / (1000 / 60) < 0.5 ? 0.5 : speed * delta / (1000 / 60));
            current += jebacczarnych;
            if (current > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public int factorial(int n){
        int result = 1;
        for(int i = 2; i <= n; i++){
            result *= i;
        }
        return result;
    }
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double output = 1.0 / StrictMath.sqrt(2.0 * StrictMath.PI * (sigma * sigma));
        return (float) (output * StrictMath.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }

    public float fastSin(float angle){
        return MathHelper.sin(angle);
    }

    public double fastSin(double angle){
        return MathHelper.sin((float) angle);
    }

    public static double roundToDecimalPlace(final double value, final double inc) {
        final double halfInc = inc / 2.0;
        final double floored = Math.floor(value / inc) * inc;
        if (value >= floored + halfInc) {
            return new BigDecimal(Math.ceil(value / inc) * inc, MathContext.DECIMAL64).stripTrailingZeros().doubleValue();
        }
        return new BigDecimal(floored, MathContext.DECIMAL64).stripTrailingZeros().doubleValue();
    }

    public static float getRandomInRange(float min, float max) {
        Random random = new Random();
        float range = max - min;
        float scaled = random.nextFloat() * range;
        float shifted = scaled + min;
        return shifted;
    }

    public static int getRandomInRange(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    /*
    TODO: zoptymalizowane obliczenia
     */
    public static double calculateDifference(double x, double y, double z){
        return Math.sqrt(x * x + y * y + z * z);
    }


    // nie uzywac przy setSpeed(), cos sie BUGUJE xd
    public static double calculateDistance(float x, float y, float x1, float y1) {
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }
    public static float calculateDistance(double x, double y, double x1, double y1) {
        return (float) Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }
}
