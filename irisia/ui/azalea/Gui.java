package irisia.ui.azalea;

import irisia.Irisia;
import irisia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class Gui extends GuiScreen {
    Mod mod = new Mod();
    Category currentCategory = Category.HUD;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        Gui.drawRect(sr.getScaledWidth() / 2 - 207f, sr.getScaledHeight() / 2 - 172, sr.getScaledWidth() / 2 + 207, sr.getScaledHeight() / 2 + 172, new Color(70, 70, 70).getRGB());
        Gui.drawRect(sr.getScaledWidth() / 2 - 206.5f, sr.getScaledHeight() / 2 - 171.5f, sr.getScaledWidth() / 2 + 206.5f, sr.getScaledHeight() / 2 + 171.5f, new Color(14, 14, 14).getRGB());
        Gui.drawRect(sr.getScaledWidth() / 2 - 205.5f, sr.getScaledHeight() / 2 - 170.5f, sr.getScaledWidth() / 2 + 205.5f, sr.getScaledHeight() / 2 + 170.5f, new Color(110, 110, 110).getRGB());
        Gui.drawRect(sr.getScaledWidth() / 2 - 205, sr.getScaledHeight() / 2 - 170, sr.getScaledWidth() / 2 + 205, sr.getScaledHeight() / 2 + 170, new Color(14, 14, 14).getRGB());
        Gui.drawRect(sr.getScaledWidth() / 2 - 205 + 42.5f, sr.getScaledHeight() / 2 - 170f, sr.getScaledWidth() / 2 + 205f, sr.getScaledHeight() / 2 + 170f, new Color(24, 24, 24).getRGB());
        int categoryIndex = 0;
        for (Category category : Category.values()) {
            if (category == currentCategory) {
                drawRectangle(sr.getScaledWidth() / 2 - 205f, sr.getScaledHeight() / 2 - 170f + (categoryIndex * 42.5f), 42.5f, 42.5f, new Color(110, 110, 110).getRGB());
                drawRectangle(sr.getScaledWidth() / 2 - 205f, sr.getScaledHeight() / 2 - 170f + (categoryIndex * 42.5f) + .5f, 42.5f, 41.5f, new Color(24, 24, 24).getRGB());
            } else {
                drawRectangle(sr.getScaledWidth() / 2 - 205f, sr.getScaledHeight() / 2 - 170f + (categoryIndex * 42.5f), 42.5f, 42.5f, new Color(110, 110, 110).getRGB());
                drawRectangle(sr.getScaledWidth() / 2 - 205f, sr.getScaledHeight() / 2 - 170f + (categoryIndex * 42.5f), 42, 42.5f, new Color(14, 14, 14).getRGB());
            }
            Irisia.getInstance.instanceCollection.getFontManager().getIcons().drawString(category.letter, sr.getScaledWidth() / 2 - 205.5f + 35f - Irisia.getInstance.instanceCollection.getFontManager().getIcons().getWidth(category.letter), sr.getScaledHeight() / 2 - 170f + (categoryIndex * 42.5f) + Irisia.getInstance.instanceCollection.getFontManager().getIcons().getHeight(category.letter) / 2 - 2, -1);
            categoryIndex += 1;
        }
        Irisia.getInstance.instanceCollection.getFontManager().getIcons().drawString("J", 5, 5, -1);
        if (currentCategory == Category.HUD) {
            //hud
        } else if (currentCategory == Category.CONFIG) {
            //combat
        } else {
            float moduleIndexHalf1 = 0, settingSizeHalf1 = 0;
            for(Module m : Irisia.getInstance.getInstanceCollection().getModuleManager().getModulesByHalfOfCategory(currentCategory.name(), 1)){
                settingSizeHalf1 = m.getNotHiddenProperties().size() * 15;

                mod.drawModule(m, sr.getScaledWidth() / 2 - 145f + 100,sr.getScaledHeight() / 2 - 170f + 5, moduleIndexHalf1, settingSizeHalf1);

                moduleIndexHalf1 += settingSizeHalf1;
                moduleIndexHalf1 += 35;
            }
            float moduleIndexHalf2 = 0, settingSizeHalf2 = 0;
            for(Module m : Irisia.getInstance.getInstanceCollection().getModuleManager().getModulesByHalfOfCategory(currentCategory.name(), 2)){
                settingSizeHalf2 = m.getNotHiddenProperties().size() * 15;

                mod.drawModule(m, sr.getScaledWidth() / 2 - 145f + 270,sr.getScaledHeight() / 2 - 170f + 5, moduleIndexHalf2, settingSizeHalf2);

                moduleIndexHalf2 += settingSizeHalf2;
                moduleIndexHalf2 += 35;
            }
        }
    }

    public void drawRectangle(float left, float top, float width, float height, int color) {
        Gui.drawRect(left, top, left + width, top + height, color);
    }
    public boolean isHovering(float left, float top, float width, float height, float mouseX, float mouseY){
        return mouseX > left && mouseX < left + width && mouseY > top && mouseY < top + height;
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, int button){
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int categoryIndex = 0;
        for (Category category : Category.values()) {

            if(isHovering(sr.getScaledWidth() / 2 - 205f, sr.getScaledHeight() / 2 - 170f + (categoryIndex * 42.5f), 42.5f, 42.5f, mouseX, mouseY)){
                currentCategory = category;
            }
            categoryIndex += 1;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
    public enum Category{
        HUD("D"),
        COMBAT("B"),
        MOVEMENT("G"),
        PLAYER("F"),
        RENDER("H"),
        EXPLOIT("J"),
        GHOST("A"),
        CONFIG("I");

        public String letter;
        Category(String letter){
            this.letter = letter;
        }
    }
}
