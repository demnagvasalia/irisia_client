package irisia.property.impl;

import irisia.Irisia;
import irisia.event.system.impl.EventValueUpdate;
import irisia.property.Property;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import irisia.ui.linear.Slider;

import java.util.function.Supplier;

public class DoubleProperty extends Property {
    public Slider sliderSet = new Slider(0, 0, this);
    double min, max, increment;
    @Expose
    @SerializedName("value")
    private double value;
    private Supplier<Boolean> dependency;

    public DoubleProperty(String name, Supplier<Boolean> dependency, double defaultValue, double minimum, double maximum, double increment) {
        super(dependency);
        this.name = name;
        this.dependency = dependency;
        this.value = defaultValue;
        this.min = minimum;
        this.max = maximum;
        this.increment = increment;
    }

    public DoubleProperty(String name, double defaultValue, double minimum, double maximum, double increment) {
        this.name = name;
        this.dependency = dependency;
        this.value = defaultValue;
        this.min = minimum;
        this.max = maximum;
        this.increment = increment;
    }


    public static double clamp(double value, double min, double max) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }

    public double getValue() {
        return value;
    }

    public int getValueInteger() {
        return (int)value;
    }

    public float getValueFloat() {
        return (float) value;
    }


    public long getValueLong() {
        return (long) value;
    }


    public void setValue(double value) {
        EventValueUpdate evu = new EventValueUpdate(value);
        Object valueNew = evu.getValue();
        Irisia.getInstance.getInstanceCollection().getBus().publish(evu);
        valueNew = clamp((Double) valueNew, this.min, this.max);
        valueNew = Math.round((Double)valueNew * (1.0 / this.increment)) / (1.0 / this.increment);
        if(!evu.isCancelled()) {
            this.value = (double) valueNew;
        }
    }


    @Override
    public String getIdentifier() {
        return super.getIdentifier();
    }


    public void increment(boolean positive) {
        if (positive) {
            setValue(getValue() + getIncrement());
        }
        if (!positive) {
            setValue(getValue() - getIncrement());
        }
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }
}
