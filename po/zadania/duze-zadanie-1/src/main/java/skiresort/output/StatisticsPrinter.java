package skiresort.output;

import java.io.PrintStream;

import skiresort.model.ResortMap;

public final class StatisticsPrinter {
    private final PrintStream output;

    public StatisticsPrinter(PrintStream output) {
        this.output = output;
    }

    public void print(ResortMap resortMap) {
        for (int id = 0; id < resortMap.getRouteCount(); id++) {
            output.println(
                    "Route " + id + " rides: "
                            + resortMap.getRoute(id).getRideCount()
            );
        }
        for (int id = 0; id < resortMap.getLiftCount(); id++) {
            output.println(
                    "Lift " + id + " rides: "
                            + resortMap.getLift(id).getRideCount()
            );
        }
    }
}
