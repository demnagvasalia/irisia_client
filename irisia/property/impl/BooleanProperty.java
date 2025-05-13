package irisia.property.impl;

import irisia.Irisia;
import irisia.event.system.impl.EventValueUpdate;
import irisia.property.Property;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.function.Supplier;

public class BooleanProperty extends Property {
    @Expose
    @SerializedName("value")
    private boolean enabled;
    public @Getter @Setter float animatedSize;
    private ArrayList<Property> children = new ArrayList<>();

    public BooleanProperty(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public BooleanProperty(String name, Supplier<Boolean> dependency, boolean enabled) {
        super(dependency);
        this.name = name;
        this.enabled = enabled;
    }


    @Override
    public String getIdentifier() {
        return super.getIdentifier();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        EventValueUpdate evu = new EventValueUpdate(enabled);
        Object valueNew = evu.getValue();
        Irisia.getInstance.getInstanceCollection().getBus().publish(new EventValueUpdate(enabled));
        if(!evu.isCancelled()) {
            this.enabled = (boolean) valueNew;
        }
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public ArrayList<Property> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Property> children) {
        this.children = children;
    }

    public void addChild(Property child) {
        children.add(child);
    }

    public void addChildren(Property... children) {
        for (Property child : children)
            addChild(child);
    }
}
