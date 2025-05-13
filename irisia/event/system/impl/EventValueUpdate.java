package irisia.event.system.impl;


import irisia.event.system.Event;

public final class EventValueUpdate extends Event<Event> {
    private Object value;

    public EventValueUpdate(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
