package skiresort.io;

import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

import skiresort.model.Athlete;
import skiresort.model.Lift;
import skiresort.model.Node;
import skiresort.model.ResortMap;
import skiresort.model.Route;
import skiresort.output.TimeFormatter;

public final class SimulationInputReader {
    private static final int INITIAL_ATHLETE_CAPACITY = 16;

    // Utility class: all parsing entry points are static.
    private SimulationInputReader() {
    }

    public static SimulationInput read(InputStream inputStream) {
        Scanner inputScanner = new Scanner(inputStream);

        Node[] nodes = readNodes(inputScanner);
        Lift[] lifts = readLifts(inputScanner, nodes);
        Route[] routes = readRoutes(inputScanner, nodes);
        Athlete[] athletes = readAthletes(inputScanner, nodes);

        return new SimulationInput(
                new ResortMap(nodes, lifts, routes),
                athletes
        );
    }

    private static Node[] readNodes(Scanner inputScanner) {
        int nodeCount = readSingleInt(inputScanner);
        Node[] nodes = new Node[nodeCount];
        for (int id = 0; id < nodeCount; id++) {
            Scanner lineScanner = scannerForNextDataLine(inputScanner);
            int height = lineScanner.nextInt();
            int x = lineScanner.nextInt();
            int y = lineScanner.nextInt();
            boolean connectedToTransport =
                    lineScanner.hasNext() && "s".equals(lineScanner.next());
            nodes[id] = new Node(id, height, x, y, connectedToTransport);
        }
        return nodes;
    }

    private static Lift[] readLifts(Scanner inputScanner, Node[] nodes) {
        int liftCount = readSingleInt(inputScanner);
        Lift[] lifts = new Lift[liftCount];
        for (int id = 0; id < liftCount; id++) {
            Scanner lineScanner = scannerForNextDataLine(inputScanner);
            Node startNode = nodes[lineScanner.nextInt()];
            Node endNode = nodes[lineScanner.nextInt()];
            int dispatchIntervalSeconds = lineScanner.nextInt();
            int capacity = lineScanner.nextInt();
            int travelTimeSeconds = lineScanner.nextInt();
            Lift lift = new Lift(
                    id,
                    startNode,
                    endNode,
                    dispatchIntervalSeconds,
                    capacity,
                    travelTimeSeconds
            );
            lifts[id] = lift;
            startNode.addOutgoingLift(lift);
        }
        return lifts;
    }

    private static Route[] readRoutes(Scanner inputScanner, Node[] nodes) {
        int routeCount = readSingleInt(inputScanner);
        Route[] routes = new Route[routeCount];
        for (int id = 0; id < routeCount; id++) {
            Scanner lineScanner = scannerForNextDataLine(inputScanner);
            Node startNode = nodes[lineScanner.nextInt()];
            Node endNode = nodes[lineScanner.nextInt()];
            int difficulty = lineScanner.nextInt();
            int travelTimeSeconds = lineScanner.nextInt();
            double baseAttractiveness = lineScanner.nextDouble();
            double resistance = lineScanner.nextDouble();
            Route route = new Route(
                    id,
                    startNode,
                    endNode,
                    difficulty,
                    travelTimeSeconds,
                    baseAttractiveness,
                    resistance
            );
            routes[id] = route;
            startNode.addOutgoingRoute(route);
        }
        return routes;
    }

    private static Athlete[] readAthletes(Scanner inputScanner, Node[] nodes) {
        int groupCount = readSingleInt(inputScanner);
        Athlete[] athletes = new Athlete[INITIAL_ATHLETE_CAPACITY];
        int athleteCount = 0;

        for (int group = 0; group < groupCount; group++) {
            Scanner groupScanner = scannerForNextDataLine(inputScanner);
            int athletesInGroup = groupScanner.nextInt();
            int skillLevel = groupScanner.nextInt();
            double spontaneity = groupScanner.nextDouble();
            boolean tracked =
                    groupScanner.hasNext() && "s".equals(groupScanner.next());

            Scanner weightScanner = scannerForNextDataLine(inputScanner);
            double difficultyWeight = weightScanner.nextDouble();
            double groomingWeight = weightScanner.nextDouble();

            Scanner startScanner = scannerForNextDataLine(inputScanner);
            Node startNode = nodes[startScanner.nextInt()];
            int firstArrivalTimeSeconds =
                    TimeFormatter.parse(startScanner.next());
            int arrivalIntervalSeconds =
                    startScanner.hasNextInt() ? startScanner.nextInt() : 0;

            for (int i = 0; i < athletesInGroup; i++) {
                if (athleteCount == athletes.length) {
                    Athlete[] expanded = new Athlete[athletes.length * 2];
                    System.arraycopy(athletes, 0, expanded, 0, athletes.length);
                    athletes = expanded;
                }
                athletes[athleteCount] = new Athlete(
                        athleteCount,
                        skillLevel,
                        spontaneity,
                        difficultyWeight,
                        groomingWeight,
                        startNode,
                        firstArrivalTimeSeconds + i * arrivalIntervalSeconds,
                        tracked
                );
                athleteCount++;
            }
        }

        Athlete[] trimmed = new Athlete[athleteCount];
        System.arraycopy(athletes, 0, trimmed, 0, athleteCount);
        return trimmed;
    }

    private static int readSingleInt(Scanner inputScanner) {
        Scanner lineScanner = scannerForNextDataLine(inputScanner);
        return lineScanner.nextInt();
    }

    private static Scanner scannerForNextDataLine(Scanner inputScanner) {
        while (inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine();
            if (!line.isBlank()) {
                Scanner lineScanner = new Scanner(line);
                lineScanner.useLocale(Locale.ENGLISH);
                return lineScanner;
            }
        }
        throw new IllegalStateException("Unexpected end of input.");
    }
}
