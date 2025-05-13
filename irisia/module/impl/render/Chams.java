package irisia.module.impl.render;

import irisia.property.impl.BooleanProperty;
import irisia.property.impl.DoubleProperty;
import irisia.ui.azalea.Gui;
import org.lwjgl.input.Keyboard;
import irisia.module.Module;
import irisia.module.ModuleAttributes;


@ModuleAttributes(name = "Chams", category = Module.Category.RENDER)
public class Chams extends Module {
    public final DoubleProperty red = new DoubleProperty("Red", 255,0,255,1);
    public final DoubleProperty green = new DoubleProperty("Green", 255,0,255,1);
    public final DoubleProperty blue = new DoubleProperty("Blue", 255,0,255,1);
    public final DoubleProperty opacity = new DoubleProperty("Opacity", 255,0,255,1);
    public final BooleanProperty material = new BooleanProperty("Material", true);
    public final BooleanProperty player = new BooleanProperty("Player", true);
}
