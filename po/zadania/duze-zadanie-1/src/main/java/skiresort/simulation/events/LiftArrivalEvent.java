package skiresort.simulation.events;

import skiresort.model.Athlete;
import skiresort.model.Lift;
import skiresort.simulation.SimulationContext;

public final class LiftArrivalEvent implements Event {
    private final int timeSeconds;
    private final Lift lift;
    private final Athlete[] passengers;

    public LiftArrivalEvent(int timeSeconds, Lift lift, Athlete[] passengers) {
        this.timeSeconds = timeSeconds;
        this.lift = lift;
        this.passengers = passengers;
    }

    @Override
    public int getTimeSeconds() {
        return timeSeconds;
    }

    @Override
    public void handle(SimulationContext context) {
        context.finishLiftRide(lift, passengers, timeSeconds);
    }
}
