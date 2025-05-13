package irisia.event.system;

public class Event<M extends Event> {
    boolean cancelled;
    State state;
    Direction direction;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    // removed static keywords, useless

    public enum State{
        PRE,
        POST
    }

    public enum Direction{
        OUTBOUND,
        INBOUND
    }
}