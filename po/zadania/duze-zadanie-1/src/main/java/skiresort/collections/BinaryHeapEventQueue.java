package skiresort.collections;

import skiresort.simulation.events.Event;

public final class BinaryHeapEventQueue implements EventQueue {
    private static final int DEFAULT_CAPACITY = 16;

    private Entry[] heap;
    private int size;
    // Breaks same-second event ties in insertion order.
    private long nextSequence;

    public BinaryHeapEventQueue() {
        heap = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void add(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }
        ensureCapacity(size + 1);
        heap[size] = new Entry(event, nextSequence);
        nextSequence++;
        siftUp(size);
        size++;
    }

    @Override
    public Event pollFirst() {
        if (size == 0) {
            throw new IllegalStateException(
                    "Cannot poll from an empty event queue."
            );
        }

        Event result = heap[0].event;
        size--;
        heap[0] = heap[size];
        heap[size] = null;
        if (size > 0) {
            siftDown(0);
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity(int requiredCapacity) {
        if (requiredCapacity <= heap.length) {
            return;
        }

        Entry[] expanded = new Entry[heap.length * 2];
        System.arraycopy(heap, 0, expanded, 0, heap.length);
        heap = expanded;
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compare(heap[parent], heap[index]) <= 0) {
                return;
            }
            swap(parent, index);
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = left + 1;
            int smallest = index;

            if (left < size && compare(heap[left], heap[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && compare(heap[right], heap[smallest]) < 0) {
                smallest = right;
            }
            if (smallest == index) {
                return;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    private int compare(Entry first, Entry second) {
        int firstTime = first.event.getTimeSeconds();
        int secondTime = second.event.getTimeSeconds();
        if (firstTime != secondTime) {
            return Integer.compare(firstTime, secondTime);
        }
        return Long.compare(first.sequence, second.sequence);
    }

    private void swap(int first, int second) {
        Entry temporary = heap[first];
        heap[first] = heap[second];
        heap[second] = temporary;
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
