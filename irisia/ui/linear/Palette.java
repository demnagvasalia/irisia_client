package irisia.ui.linear;


import irisia.utils.ColorHelper;

import java.awt.*;

public class Palette {
    public static Color CATEGORY_LIGHT = new Color(35,35,35);
    public static Color CATEGORY_DARK = new Color(31,31,31);
    public static Color MODULE_UNTOGGLED = new Color(42,42,42);
    public static Color MODULE_HOVERED = new Color(35,35,35);
    public static Color SETTING_BACK = new Color(37,37,38);
    public static Color SETTING_BACK_HOVERING = new Color(30,30,31);
    public static Color ENUM_SETTING_BACK_HOVERING = new Color(24,24,25);
    public static Color FADE_PINK = new Color(242,29,215);
    public static Color FADE_PURPLE = new Color(115,5,245);
    public static Color FADE = ColorHelper.fadeBetween(Palette.FADE_PINK.getRGB(), Palette.FADE_PURPLE.getRGB(), ((System.currentTimeMillis()) % 1000 / (1000 / 2.0f)));
}
