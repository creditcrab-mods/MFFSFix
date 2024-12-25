package mffs;

public abstract class DelayedEvent {
    public int ticks;
    protected IDelayedEventHandler handler;

    public DelayedEvent(final IDelayedEventHandler handler, final int ticks) {
        this.ticks = 0;
        this.handler = handler;
        this.ticks = ticks;
    }

    protected abstract void onEvent();

    public void update() {
        --this.ticks;
        if (this.ticks <= 0) {
            this.onEvent();
        }
    }

    public int getPriority() {
        return 0;
    }
}
