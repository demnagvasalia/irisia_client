package irisia.module.impl.render;

import irisia.module.Module;
import irisia.module.ModuleAttributes;
import irisia.property.impl.DoubleProperty;

@ModuleAttributes(name = "View Model", category = Module.Category.RENDER)
public final class ViewModel extends Module {
    public final DoubleProperty xProperty = new DoubleProperty("X", 0, -10, 10, 0.01);
    public final DoubleProperty yProperty = new DoubleProperty("Y", 0, -10, 10, 0.01);
    public final DoubleProperty zProperty = new DoubleProperty( "Z", 0, -10, 10, 0.01);

}
