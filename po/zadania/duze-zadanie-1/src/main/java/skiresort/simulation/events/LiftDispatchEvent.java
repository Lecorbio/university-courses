package skiresort.simulation.events;

import skiresort.model.Lift;
import skiresort.simulation.SimulationContext;

public final class LiftDispatchEvent implements Event {
    private final int timeSeconds;
    private final Lift lift;

    public LiftDispatchEvent(int timeSeconds, Lift lift) {
        this.timeSeconds = timeSeconds;
        this.lift = lift;
    }

    @Override
    public int getTimeSeconds() {
        return timeSeconds;
    }

    @Override
    public void handle(SimulationContext context) {
        context.dispatchLift(lift, timeSeconds);
    }
}
