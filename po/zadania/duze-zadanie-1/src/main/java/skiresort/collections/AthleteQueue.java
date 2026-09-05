package skiresort.collections;

import skiresort.model.Athlete;

public final class AthleteQueue {
    private static final int DEFAULT_CAPACITY = 8;

    private Athlete[] elements;
    // Circular-buffer index of the first queued athlete.
    private int head;
    private int size;

    public AthleteQueue() {
        this(DEFAULT_CAPACITY);
    }

    public AthleteQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(
                    "Initial capacity must be positive."
            );
        }
        elements = new Athlete[initialCapacity];
    }

    public void enqueue(Athlete athlete) {
        if (athlete == null) {
            throw new IllegalArgumentException("Athlete cannot be null.");
        }
        ensureCapacity(size + 1);
        int index = physicalIndex(size);
        elements[index] = athlete;
        size++;
    }

    public Athlete dequeue() {
        if (size == 0) {
            throw new IllegalStateException(
                    "Cannot dequeue from an empty athlete queue."
            );
        }
        Athlete athlete = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        if (size == 0) {
            head = 0;
        }
        return athlete;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int physicalIndex(int logicalOffset) {
        return (head + logicalOffset) % elements.length;
    }

    private void ensureCapacity(int requiredCapacity) {
        if (requiredCapacity <= elements.length) {
            return;
        }

        Athlete[] expanded = new Athlete[elements.length * 2];
        for (int i = 0; i < size; i++) {
            expanded[i] = elements[physicalIndex(i)];
        }
        elements = expanded;
        head = 0;
    }
}
