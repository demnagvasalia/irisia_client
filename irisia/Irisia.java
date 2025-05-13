package irisia;

import irisia.config.CSystem;
import lombok.Getter;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Irisia {
    getInstance();
    @Getter
    public InstanceCollection instanceCollection = new InstanceCollection();
    @Getter private Set<String> items = new HashSet<String>();
    File mainFolder = new File("irisia");
    public void startUp() {
        Display.setTitle("Irisia Client");
        items.addAll(Arrays.asList("Sword", "Axe","Chestplate","Helmet", "Leggings", "Boots", "Bow", "Stone", "Planks", "Arrow", "Apple", "Potion"));
        if(!mainFolder.exists()) mainFolder.mkdir();
        System.out.println("Starting irisia...");
        instanceCollection.initializeInstances();

    }
}
