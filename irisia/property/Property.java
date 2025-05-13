package irisia.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import irisia.module.Module;

import java.util.function.Supplier;

public class Property {
    @Expose
    @SerializedName("name")
    public String name;
    public boolean focused;
    public float x,y;
    private Module parentModule;
    public String identifier;
    public int value;
    private final Supplier<Boolean> dependency;

    public Property(Supplier<Boolean> dependency) {
        this.dependency = dependency;
    }

    public Property() {
        this(() -> true);
    }

    public boolean isCapable(){
        return this.dependency.get();
    }

    public String getName() {
        return name;
    }
    public String getNameWithParent(){
        return parentModule.getName() +"-" + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFocused() {
        return focused;
    }
    public void setParentModule(Module module) {
        this.parentModule = module;
    }

    public Module getParentModule() {
        return parentModule;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
