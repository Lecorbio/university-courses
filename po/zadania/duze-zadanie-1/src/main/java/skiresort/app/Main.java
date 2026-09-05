package skiresort.app;

import java.util.Random;

import skiresort.collections.BinaryHeapEventQueue;
import skiresort.io.SimulationInput;
import skiresort.io.SimulationInputReader;
import skiresort.output.ConsoleEventReporter;
import skiresort.output.StatisticsPrinter;
import skiresort.simulation.Simulator;
import skiresort.simulation.WeightedRouteAttractivenessModel;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SimulationInput input = SimulationInputReader.read(System.in);
        Random random = createRandom(args);

        Simulator simulator = new Simulator(
                input.getResortMap(),
                input.getAthletes(),
                new BinaryHeapEventQueue(),
                random,
                new WeightedRouteAttractivenessModel(),
                new ConsoleEventReporter(System.out)
        );

        simulator.run();
        new StatisticsPrinter(System.out).print(input.getResortMap());
    }

    private static Random createRandom(String[] args) {
        if (args.length == 0) {
            return new Random();
        }
        return new Random(Long.parseLong(args[0]));
    }
}
