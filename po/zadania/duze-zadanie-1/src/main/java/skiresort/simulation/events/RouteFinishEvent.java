package skiresort.simulation.events;

import skiresort.model.Athlete;
import skiresort.model.Route;
import skiresort.simulation.SimulationContext;

public final class RouteFinishEvent implements Event {
    private final int timeSeconds;
    private final Athlete athlete;
    private final Route route;

    public RouteFinishEvent(int timeSeconds, Athlete athlete, Route route) {
        this.timeSeconds = timeSeconds;
        this.athlete = athlete;
        this.route = route;
    }

    @Override
    public int getTimeSeconds() {
        return timeSeconds;
    }

    @Override
    public void handle(SimulationContext context) {
        context.finishRoute(athlete, route, timeSeconds);
    }
}
