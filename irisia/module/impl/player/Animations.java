package irisia.module.impl.player;

import irisia.module.Module;
import irisia.module.ModuleAttributes;
import irisia.property.impl.BooleanProperty;
import irisia.property.impl.DoubleProperty;
import irisia.property.impl.EnumProperty;
import org.lwjgl.input.Keyboard;

@ModuleAttributes(name = "Animations", category = Module.Category.COMBAT, keyBind = Keyboard.KEY_R)
public class Animations extends Module {
    public EnumProperty animationsModeProperty = new EnumProperty("Mode","1.7","1.7","Exhibition","Swing","Swang","Swong","Fan");
    public BooleanProperty smoothHitProperty = new BooleanProperty("Smooth Hit",false);

}
