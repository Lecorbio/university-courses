package skiresort.model;

import skiresort.collections.AthleteQueue;
import skiresort.simulation.SimulationClock;

public final class Lift {
    private final int id;
    private final Node startStation;
    private final Node endStation;
    private final int dispatchIntervalSeconds;
    private final int capacity;
    private final int travelTimeSeconds;
    private final AthleteQueue waitingQueue;
    private int rideCount;
    private int dispatchCount;
    private int maximumQueueLength;
    private long queueLengthSeconds;
    private int lastQueueStatisticTime;

    public Lift(
            int id,
            Node startStation,
            Node endStation,
            int dispatchIntervalSeconds,
            int capacity,
            int travelTimeSeconds
    ) {
        this.id = id;
        this.startStation = startStation;
        this.endStation = endStation;
        this.dispatchIntervalSeconds = dispatchIntervalSeconds;
        this.capacity = capacity;
        this.travelTimeSeconds = travelTimeSeconds;
        waitingQueue = new AthleteQueue();
        lastQueueStatisticTime = SimulationClock.OPENING_SECONDS;
    }

    public int getId() {
        return id;
    }

    public Node getStartStation() {
        return startStation;
    }

    public Node getEndStation() {
        return endStation;
    }

    public int getDispatchIntervalSeconds() {
        return dispatchIntervalSeconds;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getTravelTimeSeconds() {
        return travelTimeSeconds;
    }

    public int getRideCount() {
        return rideCount;
    }

    public int getDispatchCount() {
        return dispatchCount;
    }

    public int getMaximumQueueLength() {
        return maximumQueueLength;
    }

    public double getAverageQueueLength() {
        int duration =
                SimulationClock.DECISION_CUTOFF_SECONDS
                        - SimulationClock.OPENING_SECONDS;
        if (duration <= 0) {
            return 0.0;
        }
        return queueLengthSeconds / (double) duration;
    }

    public int getMaximumPossibleRideCount() {
        return dispatchCount * capacity;
    }

    public double getOccupancyPercent() {
        int maximumPossibleRideCount = getMaximumPossibleRideCount();
        if (maximumPossibleRideCount == 0) {
            return 0.0;
        }
        return 100.0 * rideCount / maximumPossibleRideCount;
    }

    public void joinQueue(Athlete athlete, int timeSeconds) {
        updateQueueLengthStatistic(timeSeconds);
        athlete.joinLiftQueue(this, timeSeconds);
        waitingQueue.enqueue(athlete);
        maximumQueueLength = Math.max(maximumQueueLength, waitingQueue.size());
    }

    public Athlete[] dispatch(int timeSeconds) {
        updateQueueLengthStatistic(timeSeconds);
        if (timeSeconds >= SimulationClock.OPENING_SECONDS
                && timeSeconds < SimulationClock.DECISION_CUTOFF_SECONDS) {
            dispatchCount++;
        }
        int passengerCount = Math.min(capacity, waitingQueue.size());
        Athlete[] passengers = new Athlete[passengerCount];
        for (int i = 0; i < passengerCount; i++) {
            Athlete passenger = waitingQueue.dequeue();
            passengers[i] = passenger;
            passenger.startLift(this, timeSeconds);
        }
        rideCount += passengerCount;
        return passengers;
    }

    public void finishRide(Athlete athlete, int timeSeconds) {
        athlete.leaveLift(this, timeSeconds);
    }

    public int getWaitingAthleteCount() {
        return waitingQueue.size();
    }

    public void finishStatistics() {
        updateQueueLengthStatistic(SimulationClock.DECISION_CUTOFF_SECONDS);
    }

    public int nextDispatchTimeAfter(int timeSeconds) {
        return timeSeconds + dispatchIntervalSeconds;
    }

    private void updateQueueLengthStatistic(int timeSeconds) {
        int boundedTime = Math.max(
                SimulationClock.OPENING_SECONDS,
                Math.min(timeSeconds, SimulationClock.DECISION_CUTOFF_SECONDS)
        );
        if (boundedTime <= lastQueueStatisticTime) {
            return;
        }
        queueLengthSeconds +=
                (long) waitingQueue.size()
                        * (boundedTime - lastQueueStatisticTime);
        lastQueueStatisticTime = boundedTime;
    }
}
