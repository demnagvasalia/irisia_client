package irisia.event.bus;

@FunctionalInterface
public interface Listener<Event> {

    void invoke(Event event);
}