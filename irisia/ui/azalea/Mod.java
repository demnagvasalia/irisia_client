package irisia.ui.azalea;

import irisia.Irisia;
import irisia.module.Module;

import java.awt.*;

public class Mod {

    public void drawModule(Module module, float x, float y, float index, float settingSize) {
        int count = 0;
        count += index;
        drawRectangle(x - 90.5f, y + count + 20, 140 + 1, 25 + settingSize + .5f, new Color(110,110,110).getRGB());
        drawRectangle(x - 90.5f, y + count + 20 - 0.5f, 3, .5f, new Color(110,110,110).getRGB());
        drawRectangle(x - 90.5f + Irisia.getInstance.instanceCollection.getFontManager().getVerdanaSmall().getWidth(module.getName()) + 5, y + count + 20 - 0.5f, 140 -4 - Irisia.getInstance.instanceCollection.getFontManager().getVerdanaSmall().getWidth(module.getName()), .5f, new Color(110,110,110).getRGB());
        drawRectangle(x - 90, y + count + 20, 140, 25 + settingSize, new Color(14,14,14).getRGB());
        Irisia.getInstance.instanceCollection.getFontManager().getVerdanaSmall().drawStringWithShadow(module.getName(), x - 86, y + count + 18, -1);
    }
    public void drawRectangle(float left, float top, float width, float height, int color) {
        Gui.drawRect(left, top, left + width, top + height, color);
    }
}
