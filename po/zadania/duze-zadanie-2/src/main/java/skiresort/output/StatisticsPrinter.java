package skiresort.output;

import java.io.PrintStream;
import java.util.Locale;

import skiresort.model.Lift;
import skiresort.model.ResortMap;
import skiresort.model.Route;
import skiresort.simulation.GroomingCriterion;

public final class StatisticsPrinter {
    private final PrintStream output;

    public StatisticsPrinter(PrintStream output) {
        this.output = output;
    }

    public void print(ResortMap resortMap) {
        for (int id = 0; id < resortMap.getRouteCount(); id++) {
            Route route = resortMap.getRoute(id);
            output.printf(
                    Locale.US,
                    "Route %d rides: %d, final snow: %.2f%n",
                    id,
                    route.getRideCount(),
                    GroomingCriterion.score(
                            route.getBaseAttractiveness(),
                            route.getResistance(),
                            route.getRideCount()
                    )
            );
        }
        for (int id = 0; id < resortMap.getLiftCount(); id++) {
            Lift lift = resortMap.getLift(id);
            output.printf(
                    Locale.US,
                    "Lift %d queue avg: %.0f, queue max: %d, rides: %d / %d (%.0f%%)%n",
                    id,
                    lift.getAverageQueueLength(),
                    lift.getMaximumQueueLength(),
                    lift.getRideCount(),
                    lift.getMaximumPossibleRideCount(),
                    lift.getOccupancyPercent()
            );
        }
    }
}
