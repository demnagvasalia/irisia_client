package irisia.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

public class CategoryProperty extends Property{
    @Expose
    @SerializedName("value")
    private int value;

    public boolean expanded;
    public ArrayList<Property> propertiesOnCat;

    public CategoryProperty(final String displayName) {
        super(() -> true);
        this.propertiesOnCat = new ArrayList<>();
        this.name = displayName;
    }
    public CategoryProperty(final String displayName, Supplier<Boolean> dependency) {
        super(dependency);
        this.propertiesOnCat = new ArrayList<>();
        this.name = displayName;
    }

    public void registerCategorySettings(final Property... settings) {
        this.propertiesOnCat.addAll(Arrays.asList(settings));
    }

    public CategoryProperty(final String displayName, boolean expanded) {
        super(() -> true);
        this.expanded = expanded;
        this.propertiesOnCat = new ArrayList<>();
        this.name = displayName;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }
}
