package irisia.module;

import irisia.Irisia;
import irisia.property.CategoryProperty;
import irisia.property.Property;
import irisia.property.impl.BooleanProperty;
import irisia.property.impl.DoubleProperty;
import irisia.property.impl.EnumProperty;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import irisia.interfaces.Toggleable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module implements Toggleable {
    public Module(){
        this.setName(getClass().getAnnotation(ModuleAttributes.class).name());
        this.setKeyBind(getClass().getAnnotation(ModuleAttributes.class).keyBind());
        this.setCategory(getClass().getAnnotation(ModuleAttributes.class).category());
        this.setToggled(getClass().getAnnotation(ModuleAttributes.class).toggled());
        this.setSuffix("");
    }

    @Getter @Setter private boolean showSettings, binding;
    @Getter @Setter private Category category;
    @Getter @Setter private float x = 0, y = 0;
    @Getter
    private List<Property> properties = new ArrayList<>();
    public Minecraft mc = Minecraft.getMinecraft();
    public float animatedAngle;
    public Property getPropertyByName(String name){
        Property returnProp = null;
        for(Property property : properties){
            if(property.getName() == name){
                returnProp = property;
                break;
            }
        }
        return returnProp;
    }
    public void setToggled(boolean toggled) {
        if (this.toggled != toggled) {
            this.toggled = toggled;

            if (toggled) {
                onEnable();
                //Ventura.getInstance.getEventBus().subscribe(this);
            } else {
                //Ventura.getInstance.getEventBus().unsubscribe(this);
                onDisable();
            }
        }
    }
    public List<Property> getNotHiddenProperties(){
        List<Property> newProperties = new ArrayList<>();
        for(Property p : properties){
            if(p.isCapable() && this.showSettings)
                newProperties.add(p);
        }
        return newProperties;
    }
    public void registerProperties(final Property... p){
        this.properties.addAll(Arrays.asList(p));
    }
    public void reflectProperties() {
        Field[] fieldArray = this.getClass().getDeclaredFields();
        int n = fieldArray.length;
        int n2 = 0;
        while (n2 < n) {
            Field field = fieldArray[n2];
            Class<Property> type = (Class<Property>) field.getType();
            if (type.isAssignableFrom(Property.class) || type.isAssignableFrom(DoubleProperty.class) || type.isAssignableFrom(EnumProperty.class) || type.isAssignableFrom(BooleanProperty.class)|| type.isAssignableFrom(CategoryProperty.class)) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    this.properties.add((Property)field.get(this));
                }
                catch (IllegalAccessException illegalAccessException) {
                }
            }
            ++n2;
        }
    }
    public void toggle(){
        setToggled(!toggled);
    }

    public void onEnable() {
        if(Irisia.getInstance.getInstanceCollection().getBus() != null)
            Irisia.getInstance.getInstanceCollection().getBus().subscribe(this);

    }

    public void onDisable() {
        if(Irisia.getInstance.getInstanceCollection().getBus() != null)
            Irisia.getInstance.getInstanceCollection().getBus().unsubscribe(this);
    }

    @Getter @Setter
    private int keyBind;

    @Getter @Setter
    private String name;

    public String getNameAndSuffix(){
        return this.name + " " + EnumChatFormatting.GRAY + this.suffix;
    }

    @Getter
    private boolean toggled;
    @Getter private String suffix;
    public enum Category{
        COMBAT,
        MOVEMENT,
        PLAYER,
        RENDER,
        EXPLOIT,
        GHOST;
        public int count, scrollBarY;
        public float animatedScrollY;
    }
    public void setSuffix(String suffix){
        this.suffix = suffix.replace(".", EnumChatFormatting.GRAY + " | " + EnumChatFormatting.WHITE);
    }
}
