package irisia.module.impl.player;

import irisia.Irisia;
import irisia.event.bus.Listen;
import irisia.event.system.impl.EventUpdate;
import irisia.module.Module;
import irisia.module.ModuleAttributes;
import irisia.property.impl.BooleanProperty;
import irisia.utils.MovementUtil;

@ModuleAttributes(name = "Sprint", category = Module.Category.PLAYER)
public class Sprint extends Module {
    public final BooleanProperty omniDirectional = new BooleanProperty("Omni Directional", false);
    @Listen
    public void eventUpdate(EventUpdate event) {
        if ((!(mc.thePlayer.moveForward > 0) || omniDirectional.isEnabled()) && !mc.gameSettings.keyBindSneak.isKeyDown()&& !mc.thePlayer.isCollidedHorizontally && mc.thePlayer.getFoodStats().getFoodLevel() > 6 && !Irisia.getInstance.getInstanceCollection().getModuleManager().getModuleByName("Scaffold").isToggled() && MovementUtil.getInstance.isPlayerMoving()) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
