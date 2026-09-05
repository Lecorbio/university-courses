package skiresort.tests;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import skiresort.collections.EventQueue;
import skiresort.collections.PriorityEventQueue;
import skiresort.graph.ShortestPath;
import skiresort.graph.ShortestPathFinder;
import skiresort.io.SimulationInput;
import skiresort.io.SimulationInputReader;
import skiresort.model.Athlete;
import skiresort.model.AthleteKind;
import skiresort.model.Lift;
import skiresort.model.Node;
import skiresort.model.ResortMap;
import skiresort.model.Route;
import skiresort.output.TimeFormatter;
import skiresort.simulation.SimulationContext;
import skiresort.simulation.events.Event;

public final class AutomatedTests {
    private AutomatedTests() {
    }

    public static void main(String[] args) {
        testPriorityEventQueueOrdering();
        testLiftDispatchLeavesExcessPassenger();
        testLiftDispatchWithPartlyFilledChair();
        testLiftMaximumQueueLength();
        testBfsPathFromZeroToFour();
        testBfsDirectPath();
        testBfsPathToSameNode();
        testBfsAlternativeShortestPaths();
        testNewParserFormat();
        System.out.println("All automated tests passed.");
    }

    static void testPriorityEventQueueOrdering() {
        EventQueue queue = new PriorityEventQueue();
        TestEvent eventA = new TestEvent(10, 1);
        TestEvent eventB = new TestEvent(5, 2);
        TestEvent eventC = new TestEvent(10, 3);
        queue.add(eventA);
        queue.add(eventB);
        queue.add(eventC);

        Event[] expected = new Event[] { eventB, eventA, eventC };
        Event[] actual = new Event[] {
                queue.pollFirst(),
                queue.pollFirst(),
                queue.pollFirst()
        };
        check(
                Arrays.equals(actual, expected),
                "Priority queue should order by time and insertion order."
        );
    }

    static void testLiftDispatchLeavesExcessPassenger() {
        Lift lift = testLift(3);
        Node bottom = lift.getStartStation();
        for (int id = 0; id < 4; id++) {
            lift.joinQueue(athlete(id, bottom), TimeFormatter.parse("09:00:00"));
        }

        Athlete[] passengers = lift.dispatch(TimeFormatter.parse("09:01:00"));

        check(passengers.length == 3, "Lift should board only three athletes.");
        check(lift.getRideCount() == 3, "Lift ride counter should grow by 3.");
        check(
                lift.getWaitingAthleteCount() == 1,
                "One athlete should remain in queue."
        );
    }

    static void testLiftDispatchWithPartlyFilledChair() {
        Lift lift = testLift(3);
        Node bottom = lift.getStartStation();
        lift.joinQueue(athlete(0, bottom), TimeFormatter.parse("09:00:00"));
        lift.joinQueue(athlete(1, bottom), TimeFormatter.parse("09:00:00"));

        Athlete[] passengers = lift.dispatch(TimeFormatter.parse("09:01:00"));

        check(passengers.length == 2, "Both waiting athletes should depart.");
        check(lift.getRideCount() == 2, "Lift ride counter should grow by 2.");
        check(lift.getWaitingAthleteCount() == 0, "Queue should become empty.");
    }

    static void testLiftMaximumQueueLength() {
        Lift lift = testLift(3);
        Node bottom = lift.getStartStation();
        for (int id = 0; id < 4; id++) {
            lift.joinQueue(athlete(id, bottom), TimeFormatter.parse("09:00:00"));
        }
        lift.dispatch(TimeFormatter.parse("09:01:00"));
        lift.joinQueue(athlete(4, bottom), TimeFormatter.parse("09:02:00"));

        check(
                lift.getMaximumQueueLength() == 4,
                "Maximum queue length should remember the peak of 4."
        );
    }

    static void testBfsPathFromZeroToFour() {
        BfsFixture fixture = bfsFixture();
        ShortestPath path = fixture.finder.shortestPath(
                fixture.nodes[0],
                fixture.nodes[4]
        );

        check(path.distance() == 3, "Distance from 0 to 4 should be 3.");
        checkNodePath(path, 0, 1, 2, 4);
    }

    static void testBfsDirectPath() {
        BfsFixture fixture = bfsFixture();
        ShortestPath path = fixture.finder.shortestPath(
                fixture.nodes[3],
                fixture.nodes[1]
        );

        check(path.distance() == 1, "Distance from 3 to 1 should be 1.");
        checkNodePath(path, 3, 1);
    }

    static void testBfsPathToSameNode() {
        BfsFixture fixture = bfsFixture();
        ShortestPath path = fixture.finder.shortestPath(
                fixture.nodes[2],
                fixture.nodes[2]
        );

        check(path.distance() == 0, "Distance from a node to itself is 0.");
        check(
                path.activities().isEmpty(),
                "Path from a node to itself should have no activities."
        );
        checkNodePath(path, 2);
    }

    static void testBfsAlternativeShortestPaths() {
        BfsFixture fixture = bfsFixture();
        ShortestPath path = fixture.finder.shortestPath(
                fixture.nodes[4],
                fixture.nodes[3]
        );

        check(path.distance() == 2, "Distance from 4 to 3 should be 2.");
        check(path.nodes().get(0) == fixture.nodes[4], "Path should start at 4.");
        check(path.nodes().get(2) == fixture.nodes[3], "Path should end at 3.");
        int middleId = path.nodes().get(1).getId();
        check(
                middleId == 1 || middleId == 2,
                "Path from 4 to 3 should use one of the shortest alternatives."
        );
    }

    static void testNewParserFormat() {
        String input = """
                2
                1000 0 0 s
                900 8 0

                0

                1
                0 1 4 60 0.3 0.9

                1
                2 5 0.2 0.4 K s
                0.3 0.2 0.5
                0 09:00:00 10
                """;

        SimulationInput parsed = SimulationInputReader.read(
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))
        );

        check(parsed.getAthletes().length == 2, "Parser should expand groups.");
        check(
                parsed.getAthletes()[0].getKind() == AthleteKind.COLLECTOR,
                "Parser should read athlete kind."
        );
        checkClose(
                parsed.getAthletes()[0].getBoredomFactor(),
                0.4,
                "Parser should read beta."
        );
        checkClose(
                parsed.getAthletes()[0].getBoredomWeight(),
                0.5,
                "Parser should read boredom weight."
        );
        check(
                parsed.getAthletes()[1].getArrivalTimeSeconds()
                        == TimeFormatter.parse("09:00:10"),
                "Parser should apply arrival interval."
        );
        check(parsed.getAthletes()[0].isTracked(), "Parser should read s flag.");
    }

    private static Lift testLift(int capacity) {
        Node bottom = new Node(0, 900, 0, 0, true);
        Node top = new Node(1, 1000, 0, 8, true);
        return new Lift(0, bottom, top, 60, capacity, 120);
    }

    private static Athlete athlete(int id, Node node) {
        return new Athlete(
                id,
                5,
                0.0,
                0.0,
                AthleteKind.LOCAL,
                1.0,
                0.0,
                0.0,
                node,
                TimeFormatter.parse("09:00:00"),
                false,
                1
        );
    }

    private static BfsFixture bfsFixture() {
        Node[] nodes = new Node[6];
        for (int id = 0; id < nodes.length; id++) {
            nodes[id] = new Node(id, 1000 - id, id * 8, id, true);
        }

        Route[] routes = new Route[] {
                route(0, nodes[0], nodes[1]),
                route(1, nodes[1], nodes[2]),
                route(2, nodes[2], nodes[4]),
                route(3, nodes[3], nodes[1]),
                route(4, nodes[4], nodes[1]),
                route(5, nodes[1], nodes[3]),
                route(6, nodes[4], nodes[2]),
                route(7, nodes[2], nodes[3]),
                route(8, nodes[3], nodes[5])
        };
        for (Route route : routes) {
            route.getStartNode().addOutgoingRoute(route);
        }

        ResortMap resortMap = new ResortMap(nodes, new Lift[0], routes);
        return new BfsFixture(nodes, new ShortestPathFinder(resortMap));
    }

    private static Route route(int id, Node start, Node end) {
        return new Route(id, start, end, 5, 60, 0.3, 1.0);
    }

    private static void checkNodePath(ShortestPath path, int... nodeIds) {
        List<Node> nodes = path.nodes();
        check(nodes.size() == nodeIds.length, "Unexpected node path length.");
        for (int i = 0; i < nodeIds.length; i++) {
            check(
                    nodes.get(i).getId() == nodeIds[i],
                    "Unexpected node at path index " + i + "."
            );
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void checkClose(
            double actual,
            double expected,
            String message
    ) {
        if (Math.abs(actual - expected) > 1e-9) {
            throw new AssertionError(
                    message + " Expected " + expected + ", got " + actual + "."
            );
        }
    }

    private static final class BfsFixture {
        private final Node[] nodes;
        private final ShortestPathFinder finder;

        private BfsFixture(Node[] nodes, ShortestPathFinder finder) {
            this.nodes = nodes;
            this.finder = finder;
        }
    }

    private static final class TestEvent implements Event {
        private final int timeSeconds;
        private final int id;

        private TestEvent(int timeSeconds, int id) {
            this.timeSeconds = timeSeconds;
            this.id = id;
        }

        @Override
        public int getTimeSeconds() {
            return timeSeconds;
        }

        @Override
        public void handle(SimulationContext context) {
            throw new UnsupportedOperationException(
                    "Test event " + id + " should not be handled."
            );
        }
    }
}
