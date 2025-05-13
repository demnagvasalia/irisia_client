package irisia.managers;

import irisia.utils.FontRenderer;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

public class FontManager {
    @Getter @Setter FontRenderer icons, verdana, verdanaSmall, lexenddeca, NeverBig, xiaoling, xiaolingSmall, never, roboto, menuIcons;
    public void generateTextures(){
        icons = new FontRenderer(FontManager.loadFontFromAssets("icons.ttf", 45));
        verdana = new FontRenderer(FontManager.loadFontFromAssets("verdana.ttf", 13));
        verdanaSmall = new FontRenderer(FontManager.loadFontFromAssets("verdana.ttf", 10));
        NeverBig = new FontRenderer(FontManager.loadFontFromAssets("never.ttf", 23));
        xiaoling = new FontRenderer(FontManager.loadFontFromAssets("xiaoling.ttf", 23));
        xiaolingSmall = new FontRenderer(FontManager.loadFontFromAssets("xiaoling.ttf", 18));
        never = new FontRenderer(FontManager.loadFontFromAssets("never500.ttf", 14));
        roboto = new FontRenderer(FontManager.loadFontFromAssets("roboto.ttf", 16));
        menuIcons = new FontRenderer(FontManager.loadFontFromAssets("menuIcons.ttf", 23));
        /*lexenddeca =

         */
    }

    public static Font loadFontFromAssets(final String fontName, final int size){
        try {
            return Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("fonts/" + fontName)).getInputStream()).deriveFont(Font.PLAIN, (float)size);
        }
        catch (FontFormatException | IOException ex2) {
            final Exception ex = null;
            final Exception ignored = ex;
            return null;
        }
    }
}
