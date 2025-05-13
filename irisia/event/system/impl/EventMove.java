package irisia.event.system.impl;

import irisia.event.system.Event;
import lombok.Getter;
import lombok.Setter;

public class EventMove extends Event<Event> {
    @Getter @Setter
    private double x,y,z;

    public EventMove(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
