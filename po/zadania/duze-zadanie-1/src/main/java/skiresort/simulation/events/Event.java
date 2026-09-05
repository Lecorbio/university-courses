package skiresort.simulation.events;

import skiresort.simulation.SimulationContext;

public interface Event {
    int getTimeSeconds();

    void handle(SimulationContext context);
}
