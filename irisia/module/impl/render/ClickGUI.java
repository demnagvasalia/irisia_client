package irisia.module.impl.render;

import irisia.ui.azalea.Gui;
import org.lwjgl.input.Keyboard;
import irisia.module.Module;
import irisia.module.ModuleAttributes;


@ModuleAttributes(name = "ClickGUI", category = Module.Category.RENDER, keyBind = Keyboard.KEY_RSHIFT)
public class ClickGUI extends Module {
    @Override
    public void onEnable(){
        super.onEnable();
        mc.displayGuiScreen(new irisia.ui.linear.ClickGUI());
        toggle();
    }
}
