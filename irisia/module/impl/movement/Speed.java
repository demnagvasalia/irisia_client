package irisia.module.impl.movement;

import irisia.event.bus.Listen;
import irisia.event.system.impl.EventUpdate;
import irisia.property.impl.DoubleProperty;
import irisia.property.impl.EnumProperty;
import irisia.utils.MovementUtil;
import org.lwjgl.input.Keyboard;
import irisia.module.Module;
import irisia.module.ModuleAttributes;


@ModuleAttributes(name = "Speed", category = Module.Category.MOVEMENT, keyBind = Keyboard.KEY_X)
public class Speed extends Module {
    public final EnumProperty mode = new EnumProperty("Mode", "Vanilla", "Vanilla", "Ground Strafe");
    public final DoubleProperty speed = new DoubleProperty("Speed", () -> mode.getSelected().equals("Vanilla") || mode.getSelected().equals("Ground Strafe"), 0.4, 0.01, 2, 0.01);
    @Override
    public void onEnable(){
        super.onEnable();
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }
    @Listen
    public void onUpdate(final EventUpdate event){
        this.setSuffix(mode.getSelected());
        switch (mode.getSelected()){
            case "Vanilla": {
                if(mc.thePlayer.onGround && MovementUtil.getInstance.isPlayerMoving()) {
                    mc.thePlayer.jump();
                }
                MovementUtil.getInstance.setSpeed(speed.getValue());
            }
            case "Ground Strafe": {
                if(mc.thePlayer.onGround && MovementUtil.getInstance.isPlayerMoving()) {
                    MovementUtil.getInstance.setSpeed(speed.getValue());
                    mc.thePlayer.jump();
                }
            }
        }
    }
}
