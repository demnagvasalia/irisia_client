package irisia.property.impl;


import irisia.property.Property;

import java.util.function.Supplier;

public class MultiSelectProperty<T extends Enum<T>> extends Property {
    private final T[] values;

    public MultiSelectProperty(final String label, final Supplier<Boolean> dependency, T... values) {
        this.values = values;
    }

    public MultiSelectProperty(final String label, T... values) {
        this.values = values;
    }


}
