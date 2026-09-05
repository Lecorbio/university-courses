package skiresort.app;

import java.util.Random;

import kadra.mapki.pliki.WyjatekSystemuPlikow;
import skiresort.collections.PriorityEventQueue;
import skiresort.io.SimulationInput;
import skiresort.io.SimulationInputReader;
import skiresort.output.ConsoleEventReporter;
import skiresort.output.ResortMapTexWriter;
import skiresort.output.StatisticsPrinter;
import skiresort.simulation.Simulator;
import skiresort.simulation.WeightedRouteAttractivenessModel;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println(
                    "Missing output directory for maps. "
                            + "Run: java -ea skiresort.app.Main "
                            + "\"path/to/maps\" < input.txt"
            );
            return;
        }

        try {
            runSimulation(args);
        } catch (WyjatekSystemuPlikow exception) {
            System.err.println(
                    "Could not create the map directory or write map files. "
                            + "Check the provided path and permissions."
            );
            exception.printStackTrace(System.err);
        } catch (Exception exception) {
            System.err.println(
                    "An unexpected program error occurred. "
                            + "Report it to the development team."
            );
            exception.printStackTrace(System.err);
        }
    }

    private static void runSimulation(String[] args)
            throws WyjatekSystemuPlikow {
        ResortMapTexWriter texWriter = new ResortMapTexWriter(args[0]);
        SimulationInput input = SimulationInputReader.read(System.in);
        Random random = createRandom(args);

        Simulator simulator = new Simulator(
                input.getResortMap(),
                input.getAthletes(),
                new PriorityEventQueue(),
                random,
                new WeightedRouteAttractivenessModel(),
                new ConsoleEventReporter(System.out)
        );

        simulator.run();
        new StatisticsPrinter(System.out).print(input.getResortMap());
        texWriter.writeAll(input.getResortMap(), input.getAthletes());
    }

    private static Random createRandom(String[] args) {
        if (args.length < 2) {
            return new Random();
        }
        return new Random(Long.parseLong(args[1]));
    }
}
