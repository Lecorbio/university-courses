package skiresort.model;

import skiresort.collections.AthleteQueue;

public final class Lift {
    private final int id;
    private final Node startStation;
    private final Node endStation;
    private final int dispatchIntervalSeconds;
    private final int capacity;
    private final int travelTimeSeconds;
    private final AthleteQueue waitingQueue;
    private int rideCount;

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

    public void joinQueue(Athlete athlete, int timeSeconds) {
        athlete.joinLiftQueue(this, timeSeconds);
        waitingQueue.enqueue(athlete);
    }

    public Athlete[] dispatch(int timeSeconds) {
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

    public int nextDispatchTimeAfter(int timeSeconds) {
        return timeSeconds + dispatchIntervalSeconds;
    }
}
