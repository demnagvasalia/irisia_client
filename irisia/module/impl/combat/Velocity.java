package irisia.module.impl.combat;

import irisia.event.bus.Listen;
import irisia.event.system.impl.EventPacketReceive;
import irisia.module.Module;
import irisia.module.ModuleAttributes;
import irisia.property.impl.BooleanProperty;
import irisia.property.impl.DoubleProperty;
import irisia.property.impl.EnumProperty;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;

@ModuleAttributes(name = "Velocity", category = Module.Category.COMBAT, keyBind = Keyboard.KEY_NONE)
public class Velocity extends Module {
    public final BooleanProperty explosion = new BooleanProperty("Explosion", true);
    public final DoubleProperty horizontal = new DoubleProperty("Horizontal", 50, 0,100,1);
    public final DoubleProperty vertical = new DoubleProperty("Vertical", 50, 0,100,1);
    @Listen
    public void onPacketReceive(EventPacketReceive e) {
        if(e.getPacket() instanceof S12PacketEntityVelocity) {
            if(horizontal.getValueInteger() == 0 && vertical.getValueInteger() == 0) {
                e.setCancelled(true);
            }else {
                ((S12PacketEntityVelocity) e.getPacket()).motionX *= horizontal.getValueInteger() / 100;
                ((S12PacketEntityVelocity) e.getPacket()).motionY *= vertical.getValueInteger() / 100;
                ((S12PacketEntityVelocity) e.getPacket()).motionZ *= horizontal.getValueInteger() / 100;
            }
        }
        if(e.getPacket() instanceof S27PacketExplosion && explosion.isEnabled()) {
            e.setCancelled(true);
        }
    }
}
