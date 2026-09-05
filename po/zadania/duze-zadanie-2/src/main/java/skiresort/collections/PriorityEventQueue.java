package skiresort.collections;

import java.util.Comparator;
import java.util.PriorityQueue;

import skiresort.simulation.events.Event;

public final class PriorityEventQueue implements EventQueue {
    private final PriorityQueue<Entry> entries = new PriorityQueue<>(
            Comparator
                    .comparingInt((Entry entry) -> entry.event.getTimeSeconds())
                    .thenComparingLong(entry -> entry.sequence)
    );
    private long nextSequence;

    @Override
    public void add(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }
        entries.add(new Entry(event, nextSequence));
        nextSequence++;
    }

    @Override
    public Event pollFirst() {
        Entry entry = entries.poll();
        if (entry == null) {
            throw new IllegalStateException(
                    "Cannot poll from an empty event queue."
            );
        }
        return entry.event;
    }

    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    private static final class Entry {
        private final Event event;
        private final long sequence;

        private Entry(Event event, long sequence) {
            this.event = event;
            this.sequence = sequence;
        }
    }
}
