package skiresort.collections;

import skiresort.simulation.events.Event;

public interface EventQueue {
    void add(Event event);

    Event pollFirst();

    boolean isEmpty();
}
