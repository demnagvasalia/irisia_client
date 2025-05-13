package irisia.module.impl.movement;

import irisia.module.Module;
import irisia.module.ModuleAttributes;
import irisia.property.impl.DoubleProperty;

@ModuleAttributes(name = "Keep Sprint", category = Module.Category.PLAYER)
public class KeepSprint extends Module {
    public final DoubleProperty slow = new DoubleProperty("Slow %", 40, 0, 100, 1);

}
