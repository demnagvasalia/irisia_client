package irisia.event.system.impl;

import irisia.event.system.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;

@Getter
@Setter
@AllArgsConstructor
public final class EventPacketReceive extends Event<Event> {
    private Packet packet;
}
