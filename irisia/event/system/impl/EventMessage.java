package irisia.event.system.impl;

import irisia.event.system.Event;

public final class EventMessage extends Event {
    private String content;

    public EventMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
