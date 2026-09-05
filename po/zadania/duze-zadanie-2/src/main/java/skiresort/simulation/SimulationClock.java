package skiresort.simulation;

public final class SimulationClock {
    private static final int SECONDS_PER_HOUR = 60 * 60;

    public static final int OPENING_SECONDS = 9 * SECONDS_PER_HOUR;
    public static final int DECISION_CUTOFF_SECONDS = 15 * SECONDS_PER_HOUR;

    private SimulationClock() {
    }
}
