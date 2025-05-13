package irisia.property.impl;

import irisia.Irisia;
import irisia.event.system.impl.EventValueUpdate;
import irisia.property.Property;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class EnumProperty extends Property {
    @Expose
    @SerializedName("value")
    private String selected;
    public boolean hidden;
    public boolean showEnums;
    private final HashMap<String, ArrayList<Property>> childrenMap = new HashMap<>();
    private int index;
    private List<String> modes;
    private Supplier<Boolean> dependency;


    public EnumProperty(String name, String defaultSelected, String... options) {
        this.name = name;
        this.modes = Arrays.asList(options);
        this.index = modes.indexOf(defaultSelected);
        this.selected = modes.get(index);
    }


    public EnumProperty(String name, Supplier<Boolean> dependency, String defaultSelected, String... options) {
        super(dependency);
        this.name = name;
        this.modes = Arrays.asList(options);
        this.index = modes.indexOf(defaultSelected);
        this.selected = modes.get(index);
    }

    @Override
    public String getIdentifier() {
        return super.getIdentifier();
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        EventValueUpdate up = new EventValueUpdate(selected);
        Irisia.getInstance.getInstanceCollection().getBus().publish(up);
        if(!up.isCancelled()) {
            this.selected = selected;
        }
        index = modes.indexOf(selected);
    }

    public boolean is(String mode) {
        return mode.equals(selected);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        this.selected = modes.get(index);
    }

    public List<String> getModes() {
        return modes;
    }

    public void setModes(List<String> modes) {
        this.modes = modes;
    }

    public void cycle() {
        if (index < modes.size() - 1) {
            index++;
            selected = modes.get(index);
        } else if (index >= modes.size() - 1) {
            index = 0;
            selected = modes.get(0);
        }
    }

    public ArrayList<Property> getChildren(String mode) {
        return childrenMap.get(mode);
    }

    public void setChildren(String mode, ArrayList<Property> children) {
        childrenMap.put(mode, children);
    }
}
