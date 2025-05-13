package irisia.property.impl;

import irisia.property.Property;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.awt.*;

public class ColourProperty extends Property {
    @Expose
    @SerializedName("value")
    private Integer value;

    public ColourProperty(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setRGBA(Integer value) {
        if (value == null) {
            this.value = value;
            return;
        }

        this.value = value >>> 8 | (value & 0xFF) << 24;
    }

    public int getValue() {
        return value;
    }


    public int getRed() {
        return value != null ? value >>> 16 & 0xFF : 0;
    }

    public int getGreen() {
        return value != null ? value >>> 8 & 0xFF : 0;
    }

    public int getBlue() {
        return value != null ? value & 0xFF : 0;
    }

    public int getAlpha() {
        return value != null ? value >>> 24 : 0;
    }

    public Color getAwtColor() {
        if (value == null) return null;
        return new Color(value, true);
    }

    public float[] getHSB() {
        if (value == null) return new float[]{0.0F, 0.0F, 0.0F};
        float[] hsbValues = new float[3];

        float saturation, brightness;
        float hue;

        int cMax = Math.max(value >>> 16 & 0xFF, value >>> 8 & 0xFF);
        if ((value & 0xFF) > cMax) cMax = value & 0xFF;

        int cMin = Math.min(value >>> 16 & 0xFF, value >>> 8 & 0xFF);
        if ((value & 0xFF) < cMin) cMin = value & 0xFF;

        brightness = (float) cMax / 255.0F;
        saturation = cMax != 0 ? (float) (cMax - cMin) / (float) cMax : 0;

        if (saturation == 0) {
            hue = 0;
        } else {
            float redC = (float) (cMax - (value >>> 16 & 0xFF)) / (float) (cMax - cMin), // @off
                    greenC = (float) (cMax - (value >>> 8 & 0xFF)) / (float) (cMax - cMin),
                    blueC = (float) (cMax - (value & 0xFF)) / (float) (cMax - cMin); // @on

            hue = ((value >>> 16 & 0xFF) == cMax ?
                    blueC - greenC :
                    (value >>> 8 & 0xFF) == cMax ? 2.0F + redC - blueC : 4.0F + greenC - redC) / 6.0F;

            if (hue < 0) hue += 1.0F;
        }

        hsbValues[0] = hue;
        hsbValues[1] = saturation;
        hsbValues[2] = brightness;

        return hsbValues;
    }




}
