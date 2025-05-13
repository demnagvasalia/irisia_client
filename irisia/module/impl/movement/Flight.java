package irisia.module.impl.movement;

import irisia.config.CSystem;
import irisia.event.bus.Listen;
import irisia.event.system.impl.EventUpdate;
import irisia.utils.MovementUtil;
import org.lwjgl.input.Keyboard;
import irisia.module.Module;
import irisia.module.ModuleAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;


@ModuleAttributes(name = "Flight", category = Module.Category.MOVEMENT, keyBind = Keyboard.KEY_F)
public class Flight extends Module {
    @Override
    public void onEnable(){
        super.onEnable();
    }

    @Override
    public void onDisable(){
        super.onDisable();
        mc.timer.timerSpeed = 1f;
    }
    @Listen
    public void onUpdate(final EventUpdate event){
        if (!mc.thePlayer.onGround)
            mc.thePlayer.motionY = 0;

        MovementUtil.getInstance.setSpeed(1);
    }
}
