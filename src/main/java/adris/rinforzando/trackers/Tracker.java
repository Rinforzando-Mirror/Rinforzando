package adris.rinforzando.trackers;

import adris.rinforzando.Rinforzando;

public abstract class Tracker {

    protected Rinforzando mod;
    // Needs to update
    private boolean dirty = true;

    public Tracker(TrackerManager manager) {
        manager.addTracker(this);
    }

    public void setDirty() {
        dirty = true;
    }

    // Virtual
    protected boolean isDirty() {
        return dirty;
    }

    protected void ensureUpdated() {
        if (isDirty()) {
            updateState();
            dirty = false;
        }
    }

    protected abstract void updateState();

    protected abstract void reset();
}
