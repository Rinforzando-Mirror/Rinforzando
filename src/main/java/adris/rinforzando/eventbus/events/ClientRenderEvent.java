package adris.rinforzando.eventbus.events;

import adris.rinforzando.multiversion.DrawContextWrapper;

public class ClientRenderEvent {
    public DrawContextWrapper context;
    public float tickDelta;

    public ClientRenderEvent(DrawContextWrapper context, float tickDelta) {
        this.context = context;
        this.tickDelta = tickDelta;
    }
}
