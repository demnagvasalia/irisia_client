package irisia.event.system.impl;

import irisia.event.system.Event;
import net.minecraft.network.Packet;

public class EventPacketSend extends Event {

    public Packet packet;

    public EventPacketSend(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

}