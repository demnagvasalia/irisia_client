package irisia.ui.linear;

import irisia.property.impl.DoubleProperty;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;

import java.awt.*;

public class Slider {
    public DoubleProperty doubleProperty;
    private boolean canSlide;
    public float x, y, animatedVal;

    public Slider(float x, float y, DoubleProperty floatProperty) {
        this.x = x;
        this.y = y;
        this.doubleProperty = floatProperty;
    }
    public void draw(int mouseX, int mouseY, Color color) {
        if(mouseX <= x + 111 && mouseX >= x && mouseY <= y + 14 && mouseY >= y - 5 && canSlide) {
            double difference = (doubleProperty.getMax() - doubleProperty.getMin());
            doubleProperty.setValue(MathHelper.clamp_double((mouseX - x) * (difference / 111) + doubleProperty.getMin(), doubleProperty.getMin(), doubleProperty.getMax()));
        }
        Gui.drawRect(x, y + 7, x + 111, y +10, new Color(34,34,34,255).getRGB());
        Gui.drawRect(x, y + 7, (float) (x + 111 * (doubleProperty.getValue() - doubleProperty.getMin()) / (doubleProperty.getMax() - doubleProperty.getMin())), y + 10, color.getRGB());
        //LinearClientSingleton.INSTANCE.getBlurProcessor().bloom((int) x - 1, (int) y + 6, 3 + (int) (111 * (doubleProperty.getValue() - doubleProperty.getMin()) / (doubleProperty.getMax() - doubleProperty.getMin())), 4, 5,color);
    }

    public void mouseClicked(int mouseX, int mouseY) {
        canSlide = mouseX <= x + 111 && mouseX >= x && mouseY <= y + 23 && mouseY >= y - 17;
    }

    public void mouseReleased() {
        canSlide = false;
    }
}
