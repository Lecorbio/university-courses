package skiresort.simulation.events;

import skiresort.model.Athlete;
import skiresort.simulation.SimulationContext;

public final class AthleteArrivalEvent implements Event {
    private final int timeSeconds;
    private final Athlete athlete;

    public AthleteArrivalEvent(int timeSeconds, Athlete athlete) {
        this.timeSeconds = timeSeconds;
        this.athlete = athlete;
    }

    @Override
    public int getTimeSeconds() {
        return timeSeconds;
    }

    @Override
    public void handle(SimulationContext context) {
        context.athleteArrived(athlete, timeSeconds);
    }
}
