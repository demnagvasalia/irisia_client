package irisia.module.impl.player;

import irisia.Irisia;
import irisia.event.bus.Listen;
import irisia.event.system.impl.EventUpdate;
import irisia.module.Module;
import irisia.module.ModuleAttributes;
import irisia.property.impl.BooleanProperty;
import irisia.property.impl.DoubleProperty;
import irisia.utils.Stopwatch;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;

@ModuleAttributes(name = "Chest Stealer", category = Module.Category.PLAYER)
public class ChestStealer extends Module {
    public final DoubleProperty delayProperty = new DoubleProperty("Delay", 0.3, 0.1, 5, 0.1);
    public final BooleanProperty autoCloseProperty = new BooleanProperty("Auto Close", true);
    public final BooleanProperty onlyValuableProperty = new BooleanProperty("Only Valuable", false);
    Stopwatch timer = new Stopwatch();

    @Listen
    public void onUpdate(EventUpdate event){
        if (this.mc.thePlayer != null && this.mc.thePlayer.openContainer != null && this.mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest)this.mc.thePlayer.openContainer;
            if(!isEmpty(chest)){
                int slotAmount = chest.inventorySlots.size() == 90 ? 54 : 27;
                for(int i = 0; i < slotAmount; ++i) {
                    if (chest.getSlot(i).getHasStack() && timer.hasTimeElapsed((long) (1000 * delayProperty.getValueFloat()), false)) {
                        if(onlyValuableProperty.isEnabled()) {
                            for (String name : Irisia.getInstance.getItems()) {
                                System.out.println(name);
                                if(chest.getSlot(i).getStack() != null) {
                                    if (chest.getSlot(i).getStack().getDisplayName().toLowerCase().contains(name.toLowerCase())) {
                                        GrabItem(chest.windowId, i);
                                        timer.reset();
                                    }
                                }
                            }
                        }else {
                            GrabItem(chest.windowId, i);
                            timer.reset();
                        }
                    }
                }
            }else if(autoCloseProperty.isEnabled())mc.thePlayer.closeScreen();
        }
    }
    public void GrabItem(int windowId, int count){
        this.mc.playerController.windowClick(windowId, count, 0, 1, this.mc.thePlayer);
    }
    public boolean isEmpty(Container container) {
        int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27;
        for(int i = 0; i < slotAmount; ++i) {
            for(String pies : Irisia.getInstance.getItems()) {
                if (container.getSlot(i).getHasStack()) {
                    if(onlyValuableProperty.isEnabled()) {
                        if (container.getSlot(i).getStack().getDisplayName().toLowerCase().contains(pies.toLowerCase())) {
                            return false;
                        }
                    }else {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
