package mffs;

import java.util.List;

public interface IDelayedEventHandler {
    List<DelayedEvent> getDelayedEvents();

    List<DelayedEvent> getQuedDelayedEvents();
}
