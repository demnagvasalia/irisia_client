package irisia.event.system.impl;

import irisia.event.system.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class Event3D extends Event {
    private float partialTicks;
}
