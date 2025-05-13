package irisia;

import irisia.altmanager.AltManager;
import irisia.event.bus.PubSub;
import irisia.managers.FontManager;
import lombok.Getter;
import irisia.managers.ModuleManager;

import java.io.File;

public class InstanceCollection {
    @Getter
    private ModuleManager moduleManager;
    @Getter
    private AltManager altManager;
    @Getter
    private PubSub bus;
    @Getter
    private FontManager fontManager;

    public void initializeInstances(){
        bus = PubSub.newInstance(System.err::println);
        bus.subscribe(this);
        moduleManager = new ModuleManager();
        fontManager = new FontManager();
        fontManager.generateTextures();
        altManager = new AltManager(new File(Irisia.getInstance.mainFolder + "\\alts"));
    }
}
