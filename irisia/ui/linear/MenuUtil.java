package irisia.ui.linear;

import irisia.Irisia;
import irisia.module.Module;
import irisia.property.CategoryProperty;
import irisia.property.impl.BooleanProperty;
import irisia.property.impl.DoubleProperty;
import irisia.property.impl.EnumProperty;
import irisia.utils.Animator;
import irisia.utils.ColorHelper;
import irisia.utils.RoundedHelper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class MenuUtil {

    public @Setter @Getter int mouseX, mouseY;
    public @Setter boolean buttonLeft, buttonRight;
    public @Getter @Setter float animation;
    public void drawRect(float left, float top, float width, float height, Color color){
        Gui.drawRect(left,top, left + width, top + height, color.getRGB());
    }
    public void drawMod(Module module, float left, float top, float width, float height){
        this.drawRect(left, top, width, height, Palette.CATEGORY_LIGHT);
        this.drawRect(left + 2f, top, width - 4, height, isHovering(left, top, width, height) ? Palette.MODULE_HOVERED : Palette.MODULE_UNTOGGLED);
        if(isHovering(left, top, width, height)){
            if(this.buttonLeft)
                module.setToggled(!module.isToggled());
            if(this.buttonRight)
                module.setShowSettings(!module.isShowSettings());
        }
        //NWrapper.getIcons().drawStringWithShadow("C", left, top, -1);
        if(module.getProperties().size() > 0) {
            module.animatedAngle = Animator.animateHUD(module.animatedAngle, module.isShowSettings() ? 180 : 360, 3f);
            GlStateManager.pushMatrix();
            GlStateManager.translate(left + width - 12 + (module.isShowSettings() ? 8 : 0), top - 5 + (module.isShowSettings() ? 11 : 16), 0.0F);
            GlStateManager.rotate(module.animatedAngle, 0.0F, 0.0F, 1.0F);
            float f = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
            f = f * 100.0F / (Irisia.getInstance.getInstanceCollection().getFontManager().getMenuIcons().getStringWidth("C") + 32);
            GlStateManager.scale(1, 1, 1);
            Irisia.getInstance.getInstanceCollection().getFontManager().getMenuIcons().drawString("C", 0 - 0.01f, -8, -1);
            GlStateManager.popMatrix();
        }
    }
    public void drawNumberSetting(DoubleProperty doubleProperty, float left, float top, float width, float height){
        this.drawRect(left, top, width, height, Palette.SETTING_BACK);
        if(isHovering(left, top, width, height)){
            this.drawRect(left + 2, top, width - 4, height, Palette.SETTING_BACK_HOVERING);
        }
        Irisia.getInstance.getInstanceCollection().getFontManager().getRoboto().drawCenteredString(doubleProperty.getName(), left + width / 2, top + 3, -1);
    }
    public void drawBooleanSetting(BooleanProperty booleanProperty, float left, float top, float width, float height){
        this.drawRect(left, top, width, height, Palette.SETTING_BACK);
        if(isHovering(left, top, width, height)){
            if(buttonLeft || buttonRight)
                booleanProperty.setEnabled(!booleanProperty.isEnabled());
            this.drawRect(left + 2, top, width - 4, height, Palette.SETTING_BACK_HOVERING);
        }
        Irisia.getInstance.getInstanceCollection().getFontManager().getXiaolingSmall().drawStringWithShadow(booleanProperty.getName(), left + 4, top + 4, -1);
        Palette.FADE = ColorHelper.fadeBetween(Palette.FADE_PINK.getRGB(), Palette.FADE_PURPLE.getRGB(), ((System.currentTimeMillis()) % 1000 / (1000 / 2.0f)));
        RoundedHelper.drawRoundOutline(left + width - 14, top + 3.5f, 10,10,5,0.2f, new Color(1,1,1,0), Palette.FADE);
        booleanProperty.setAnimatedSize(Animator.animateHUD(booleanProperty.animatedSize, !booleanProperty.isEnabled() ? 10 : 0, 0.1f, 0.025F));
        RoundedHelper.drawRoundOutline(left + width - 14 + booleanProperty.getAnimatedSize(), top + 3.5f + booleanProperty.getAnimatedSize(), 10 - (booleanProperty.getAnimatedSize()* 2),10 - (booleanProperty.getAnimatedSize()* 2),5,0.2f, Palette.FADE, Palette.FADE);
    }
    public void drawCategory(CategoryProperty categoryProperty, float left, float top, float width, float height){
        this.drawRect(left,top,width,height, Palette.SETTING_BACK);
        this.drawRect(left + 2,top,width - 4,height, this.isHovering(left,top,width,height) ? Palette.ENUM_SETTING_BACK_HOVERING : Palette.SETTING_BACK_HOVERING );
        if(this.isHovering(left,top,width,height)){
            if(buttonLeft || buttonRight)
                categoryProperty.setExpanded(!categoryProperty.isExpanded());
        }
        Irisia.getInstance.getInstanceCollection().getFontManager().getRoboto().drawCenteredString(categoryProperty.getName(), left + width / 2, top + 5, -1);
    }
    public void drawEnum(EnumProperty enumProperty, float left, float top, float width, float height){
        this.drawRect(left,top,width,height, Palette.SETTING_BACK);
        if(this.isHovering(left,top,width,height)) {
            if(buttonLeft)
                enumProperty.showEnums = !enumProperty.showEnums;
            if(buttonRight)
                enumProperty.cycle();
            this.drawRect(left + 2, top, width - 4, height, Palette.SETTING_BACK_HOVERING);
        }
        RoundedHelper.drawRound(left + 4,top + 11,width - 8,height - 14, 3, this.isHovering(left,top,width,height) ? Palette.ENUM_SETTING_BACK_HOVERING : Palette.SETTING_BACK_HOVERING);
        Irisia.getInstance.getInstanceCollection().getFontManager().getNever().drawCenteredString(enumProperty.getName(), left + width / 2, top + 2, -1);
        if(enumProperty.showEnums){
            this.drawRect(left, top + 27, 115, enumProperty.getModes().size() * 10, Palette.SETTING_BACK);
            this.drawRect(left + 3.5f, top + 14, width - 7, height - 16.5f, this.isHovering(left,top,width,height) ? Palette.ENUM_SETTING_BACK_HOVERING : Palette.SETTING_BACK_HOVERING);
            int enumOffset = 0;
            for(int i = 0; i < enumProperty.getModes().size(); i++){
                if(i != enumProperty.getModes().size() - 1)
                    this.drawRect(left + 3.5f, top + 14 + (height - 16.5f) + enumOffset, 108, 10,  this.isHovering(left,top,width,height) ? Palette.ENUM_SETTING_BACK_HOVERING : Palette.SETTING_BACK_HOVERING);
                else{
                    this.drawRect(left + 3.5f, top + 8 + (height - 16.5f) + enumOffset, 6, 10,  this.isHovering(left,top,width,height) ? Palette.ENUM_SETTING_BACK_HOVERING : Palette.SETTING_BACK_HOVERING);
                    this.drawRect(left + width - 9.5f, top + 8 + (height - 16.5f) + enumOffset, 6, 10,  this.isHovering(left,top,width,height) ? Palette.ENUM_SETTING_BACK_HOVERING : Palette.SETTING_BACK_HOVERING);
                    RoundedHelper.drawRound(left + 4.1f , top + 13 + (height - 16.5f) + enumOffset, 106.8f, 10, 3,  this.isHovering(left,top,width,height) ? Palette.ENUM_SETTING_BACK_HOVERING : Palette.SETTING_BACK_HOVERING);
                }
                Irisia.getInstance.getInstanceCollection().getFontManager().getNever().drawCenteredString(enumProperty.getModes().get(i) + (enumProperty.getModes().get(i) == enumProperty.getSelected() ? EnumChatFormatting.GRAY + " [selected]" : "") , left + width / 2 - 0.01f, top + 25 + enumOffset, !isHovering(left + 3.5f, top + 23 + enumOffset, 108, 10) ? -1 : new Color(90,90,90).getRGB());
                if(isHovering(left + 3.5f, top + 23 + enumOffset, 108, 10) && (buttonRight || buttonLeft))
                    enumProperty.setIndex(i);
                enumOffset += 10;
            }
            this.drawRect(left + 5, top + 22f, 105, 0.5f, new Color(255,255,255,120));
        }
        Irisia.getInstance.getInstanceCollection().getFontManager().getXiaolingSmall().drawCenteredString(enumProperty.getSelected(), left + width / 2, top + 13 - (enumProperty.showEnums ? 1 : 0), -1);
    }
    public void drawKeybind(Module module, float left, float top, float width, float height){
        this.drawRect(left, top, width, height, Palette.SETTING_BACK);
        if(isHovering(left, top, width, height)){
            if(buttonLeft || buttonRight)module.setBinding(true);
            this.drawRect(left + 2, top, width - 4, height, Palette.SETTING_BACK_HOVERING);
        }
        Irisia.getInstance.getInstanceCollection().getFontManager().getXiaolingSmall().drawStringWithShadow("Keybind", left + 4, top + 4, -1);
        Irisia.getInstance.getInstanceCollection().getFontManager().getXiaolingSmall().drawStringWithShadow(module.isBinding() ? "..." : Keyboard.getKeyName(module.getKeyBind()), left + width - 4 - Irisia.getInstance.getInstanceCollection().getFontManager().getXiaolingSmall().getStringWidth(module.isBinding() ? "..." : Keyboard.getKeyName(module.getKeyBind())), top + 4, -1);
    }
    public boolean isHovering(float left, float top, float width, float height){
        return mouseX > left && mouseX < left + width && mouseY > top && mouseY < top + height;
    }
}
