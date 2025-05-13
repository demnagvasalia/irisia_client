package irisia.module.impl.render;

import irisia.Irisia;
import irisia.event.bus.Listen;
import irisia.event.system.impl.EventUI;
import irisia.utils.Animator;
import irisia.utils.ColorHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import irisia.module.Module;
import irisia.module.ModuleAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@ModuleAttributes(name = "HUD", category = Module.Category.RENDER, keyBind = Keyboard.KEY_NONE)
public class HUD extends Module {
    public HUD(){
        this.setToggled(true);
    }
    @Listen
    public void onEventUI(final EventUI event){
        List<Module> modules = new ArrayList<>(Irisia.getInstance.getInstanceCollection().getModuleManager().getModules());
        modules.sort(Comparator.comparingDouble(m -> (double) Irisia.getInstance.getInstanceCollection().getFontManager().getXiaolingSmall().getStringWidth(((Module)m).getNameAndSuffix())).reversed());
        Irisia.getInstance.getInstanceCollection().getFontManager().getXiaolingSmall().drawStringWithShadow("I" + EnumChatFormatting.WHITE + "risia", 3,3, ColorHelper.rainbowColor(0.3F, 0.5F, 1.0F,  1));
        int count = 0;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        for(Module m : modules){
            m.setX(Animator.animateHUD(m.getX(), !m.isToggled() ? -Irisia.getInstance.getInstanceCollection().getFontManager().getXiaolingSmall().getStringWidth(m.getNameAndSuffix()) : +3, 0.7f));
            m.setY(Animator.animateHUD(m.getY(), count, 0.7f));
            Irisia.getInstance.getInstanceCollection().getFontManager().getXiaolingSmall().drawStringWithShadow(m.getNameAndSuffix(), m.getX(), m.getY() + 13, -1);
            if(m.isToggled())
                count += 9;
        }
    }
}
