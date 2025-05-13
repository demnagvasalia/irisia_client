package irisia.module.impl.combat;

import irisia.event.bus.Listen;
import irisia.event.system.impl.EventUpdate;
import irisia.module.Module;
import irisia.module.ModuleAttributes;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@ModuleAttributes(name = "Anti Bot", category = Module.Category.COMBAT)
public class AntiBot extends Module {
    @Listen
    public void onUpdate(EventUpdate eventUpdate){
        for(Entity entity : mc.theWorld.getLoadedEntityList()){
            if(isPresentOnTab(entity) && entity.isInvisible()){
                mc.theWorld.removeEntity(entity);
            }
        }
    }
    boolean isPresentOnTab(final Entity e){
        NetworkPlayerInfo networkPlayerInfo;
        for (NetworkPlayerInfo playerInfo : mc.getNetHandler().getPlayerInfoMap()) {
            networkPlayerInfo = playerInfo;
            if(networkPlayerInfo.getGameProfile().getName() == e.getName()){
                return true;
            }
        }
        return false;
    }
}
