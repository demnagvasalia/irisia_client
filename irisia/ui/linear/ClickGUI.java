package irisia.ui.linear;

import irisia.Irisia;
import irisia.module.Module;
import irisia.property.CategoryProperty;
import irisia.property.Property;
import irisia.property.impl.BooleanProperty;
import irisia.property.impl.DoubleProperty;
import irisia.property.impl.EnumProperty;
import irisia.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;

public class
ClickGUI extends GuiScreen {
    MenuUtil mu = new MenuUtil();
    Translate translate;
    int opacityAnimation;

    public ClickGUI(){
       // mu.setAnimation(0.5f);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.translate = new Translate(0, 0);
        opacityAnimation = 0;
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        for(Module.Category category : Module.Category.values()) {
            for(Module module : Irisia.getInstance.getInstanceCollection().getModuleManager().getModulesByCat(category)) {
                if(module.isBinding()){
                    if (key != Keyboard.KEY_BACK && key != Keyboard.KEY_LMETA && key != Keyboard.KEY_ESCAPE) {
                        module.setKeyBind(key);
                        module.setBinding(false);
                    } else module.setBinding(false);
                }if(key == Keyboard.KEY_ESCAPE) mc.thePlayer.closeScreen();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        translate.interpolate(this.width,this.height, 8);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        double xmod = this.width/2-(translate.getX()/2);
        double ymod = this.height/2-(translate.getY()/2);
        double scaleX = (translate.getX()/this.width) , scaleY = (translate.getY()/this.height) ;
        GL11.glPushMatrix();
        GlStateManager.translate(xmod,ymod, 0);
        GlStateManager.scale(scaleX, scaleY, 1);
        if(opacityAnimation == 0){
            opacityAnimation = (int) Math.max(255, opacityAnimation + Math.tanh(70) * 5);
        }

    //
        /*
        mu.setAnimation(Animator.animateHUD(mu.getAnimation(), 1, 0.01f));
        GL11.glPushMatrix();
        GL11.glTranslated(sr.getScaledWidth() / 2 - mu.getAnimation() * sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - mu.getAnimation() * sr.getScaledHeight() / 2,0);
        GL11.glScalef(mu.getAnimation(), mu.getAnimation(), mu.getAnimation());

         */
        mu.setMouseX(mouseX);
        mu.setMouseY(mouseY);
        int catOffset = 0;
        for(Module.Category category : Module.Category.values()){
            Stencil.initStencilToWrite();
            mu.drawRect(30 + catOffset, 50, 115, 310, new Color(255,255,255));
            Stencil.readStencilBuffer(1);
            category.count = 0;
            float math = (30 + 10 -  Irisia.getInstance.getInstanceCollection().getFontManager().getNeverBig().getHeight(category.name()) / 2) + .5f;
            int modOffset = 0;
            for(Module module : Irisia.getInstance.getInstanceCollection().getModuleManager().getModulesByCat(category)){
                mu.drawMod(module, 30 + catOffset, 50 + modOffset + category.animatedScrollY, 115, 17);
                for(int i = 0; i < 34; i++){
                    if(module.isToggled()){
                        Color color = ColorHelper.fadeBetween(Palette.FADE_PINK.getRGB(), Palette.FADE_PURPLE.getRGB(), ((System.currentTimeMillis() + (i * 1000)) % 1000 / (1000 / 2.0f)));
                        mu.drawRect(32 + catOffset, 50 + modOffset + category.animatedScrollY, 111, i /2 + 1, color);
                        if(mu.isHovering(32 + catOffset, 50 + modOffset + category.animatedScrollY, 111, i /2 + 1))
                            mu.drawRect(32 + catOffset, 50 + modOffset + category.animatedScrollY, 111, i /2 + 1, new Color(1,1,1,80));
                    }
                }
                Irisia.getInstance.getInstanceCollection().getFontManager().getXiaoling().drawString(module.getName(), 32 + catOffset + 111 / 2 - Irisia.getInstance.getInstanceCollection().getFontManager().getXiaoling().getStringWidth(module.getName()) / 2 + 0.01f, 50 + modOffset + 4 + category.animatedScrollY, -1);
                int propertyOffset = 0;
                if(module.isShowSettings()){
                    mu.drawKeybind(module, 30 + catOffset, 50 + modOffset + 17 + category.animatedScrollY, 115, 17);
                    category.count += 17;
                    modOffset += 17;
                }
                for(Property p : module.getNotHiddenProperties()){
                    if(p instanceof DoubleProperty){
                        mu.drawNumberSetting((DoubleProperty) p, 30 + catOffset, 50 + modOffset + 17 + propertyOffset + category.animatedScrollY, 115, 22);
                        if(mu.buttonLeft)((DoubleProperty) p).sliderSet.mouseClicked(mouseX, mouseY);
                        ((DoubleProperty) p).sliderSet.x = 30 + catOffset + 2;
                        ((DoubleProperty) p).sliderSet.y = 50 + modOffset + 17 + propertyOffset + 7 + category.animatedScrollY;
                        Color color = ColorHelper.fadeBetween(Palette.FADE_PINK.getRGB(), Palette.FADE_PURPLE.getRGB(), ((System.currentTimeMillis()) % 1000 / (1000 / 2.0f)));
                        ((DoubleProperty) p).sliderSet.draw(mouseX, mouseY, color);
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        Irisia.getInstance.getInstanceCollection().getFontManager().getNever().drawString(decimalFormat.format(((DoubleProperty) p).getValue()), 30 + catOffset + 3, 50 + modOffset + category.animatedScrollY + 17 + propertyOffset + 3.5f, -1);
                        Irisia.getInstance.getInstanceCollection().getFontManager().getNever().drawString(decimalFormat.format(((DoubleProperty) p).getMax()), 30 + catOffset + 113 - Irisia.getInstance.getInstanceCollection().getFontManager().getNever().getStringWidth(decimalFormat.format(((DoubleProperty) p).getMax())), 50 + modOffset  + category.animatedScrollY+ 17 + propertyOffset + 3.5f, -1);
                        category.count += 22;
                        modOffset += 22;
                    }
                    if(p instanceof BooleanProperty){
                        mu.drawBooleanSetting((BooleanProperty) p, 30 + catOffset, 50 + modOffset + category.animatedScrollY + 17 + propertyOffset, 115, 17);
                        category.count += 17;
                        modOffset += 17;
                    }
                    if(p instanceof EnumProperty){
                        mu.drawEnum((EnumProperty) p, 30 + catOffset, 50 + modOffset  + category.animatedScrollY+ 17 + propertyOffset, 115, 27);
                        if(((EnumProperty) p).showEnums) {
                            modOffset += ((EnumProperty) p).getModes().size() * 10;
                            category.count += ((EnumProperty) p).getModes().size() * 10;
                        }
                        category.count += 27;
                        modOffset += 27;
                    }
                    if(p instanceof CategoryProperty){
                        mu.drawCategory((CategoryProperty) p, 30 + catOffset, 50 + modOffset  + category.animatedScrollY+ 17 + propertyOffset, 115, 17);
                        category.count += 17;
                        modOffset+= 17;
                    }
                }
                category.count += 17;
                modOffset += 17;
            }
            //LinearClientSingleton.INSTANCE.getBlurProcessor().bloom((int) (32f + catOffset), 45, 111, 5, 15, new Color(1,1,1));
            Stencil.uninitStencilBuffer();
            mu.drawRect(30 + catOffset, 35, 115, 15, Palette.CATEGORY_DARK);
            RoundedHelper.drawRound(30.9f + catOffset, 30, 113.2f, 10, 5, Palette.CATEGORY_DARK);
            Irisia.getInstance.getInstanceCollection().getFontManager().getNeverBig().drawStringWithShadow(category.name(), 30 + catOffset + 5, math, -1);
            if(category.count < 300){
                RoundedHelper.drawRound(32.5f + catOffset - 15 - 4 + 15, 46 + category.count + 3,  113f + 5, 1.5f,4, Palette.CATEGORY_LIGHT);
            }
            if(category.count < 300 && category.scrollBarY < 0){
                category.scrollBarY = 0;
            }
            if(mu.isHovering(30 + catOffset, 50, 115, 310) && category.count > 300) {
                category.animatedScrollY = Animator.animateHUD(category.animatedScrollY, category.scrollBarY, 0.1f);
                int wheelsize = Mouse.getDWheel();
                if (wheelsize < 0 && category.scrollBarY > -category.count + 310)
                    category.scrollBarY -= 15f;
                if (wheelsize > 0 && category.scrollBarY < 0) category.scrollBarY +=15f;
            }
            catOffset += 130;

        }
        mu.setButtonRight(false);
        mu.setButtonLeft(false);
        GL11.glPopMatrix();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button){
        int catOffset = 0;
        for(Module.Category category : Module.Category.values()) {
            if (mu.isHovering(30 + catOffset, 50, 115, 310)) {
                mu.setButtonLeft(button == 0);
                mu.setButtonRight(button == 1);
            }
            catOffset+= 130;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for(Module.Category cat : Module.Category.values()){
            for (Module module : Irisia.getInstance.getInstanceCollection().getModuleManager().getModulesByCat(cat)) {
                if (module.isShowSettings()) {
                    for (Property property : module.getProperties()) {
                        if (property instanceof DoubleProperty) {
                            ((DoubleProperty) property).sliderSet.mouseReleased();
                        }
                    }
                }
            }
        }
    }


    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
