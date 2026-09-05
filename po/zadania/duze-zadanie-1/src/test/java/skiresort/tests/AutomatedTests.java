package skiresort.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

import skiresort.collections.AthleteQueue;
import skiresort.collections.BinaryHeapEventQueue;
import skiresort.collections.EventQueue;
import skiresort.io.SimulationInput;
import skiresort.io.SimulationInputReader;
import skiresort.model.ActivityChoice;
import skiresort.model.Athlete;
import skiresort.model.Lift;
import skiresort.model.Node;
import skiresort.model.ResortMap;
import skiresort.model.Route;
import skiresort.output.ConsoleEventReporter;
import skiresort.output.StatisticsPrinter;
import skiresort.output.TimeFormatter;
import skiresort.simulation.DifficultyMatchCriterion;
import skiresort.simulation.GroomingCriterion;
import skiresort.simulation.SimulationContext;
import skiresort.simulation.Simulator;
import skiresort.simulation.WeightedRouteAttractivenessModel;
import skiresort.simulation.events.Event;

public final class AutomatedTests {
    private AutomatedTests() {
    }

    public static void main(String[] args) {
        testEventQueueOrdering();
        testEmptyEventQueueThrows();
        testAthleteQueue();
        testLiftDispatch();
        testDifficultyMatchCriterion();
        testGroomingCriterion();
        testWeightedRouteAttractiveness();
        testAthleteChoosesBestActivity();
        testAthleteRejectsDeadEnd();
        testParser();
        testSimulatorReportsOnlyTrackedAthletes();
        testCutoffCompletion();
        System.out.println("All automated tests passed.");
    }

    private static void testEventQueueOrdering() {
        EventQueue queue = new BinaryHeapEventQueue();
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
                "Event queue should order by time and then insertion order."
        );
        check(queue.isEmpty(), "Queue should be empty.");
    }

    private static void testEmptyEventQueueThrows() {
        EventQueue queue = new BinaryHeapEventQueue();
        checkThrows(
                () -> queue.pollFirst(),
                "Polling an empty event queue should throw."
        );
    }

    private static void testAthleteQueue() {
        Node node = new Node(0, 1000, 0, 0, true);
        AthleteQueue queue = new AthleteQueue(2);
        Athlete first = athlete(0, node, 9 * 3600);
        Athlete second = athlete(1, node, 9 * 3600);
        Athlete third = athlete(2, node, 9 * 3600);

        queue.enqueue(first);
        queue.enqueue(second);
        check(queue.dequeue() == first, "Athlete queue should be FIFO.");
        queue.enqueue(third);
        check(
                queue.dequeue() == second,
                "Circular queue should keep FIFO after wrap."
        );
        check(
                queue.dequeue() == third,
                "Circular queue should keep FIFO after resize/wrap."
        );
        check(queue.isEmpty(), "Athlete queue should be empty.");
    }

    private static void testLiftDispatch() {
        Node bottom = new Node(0, 900, 0, 0, true);
        Node top = new Node(1, 1000, 0, 1, true);
        Lift lift = new Lift(0, bottom, top, 60, 2, 120);
        Athlete first = athlete(0, bottom, 9 * 3600);
        Athlete second = athlete(1, bottom, 9 * 3600);
        Athlete third = athlete(2, bottom, 9 * 3600);

        lift.joinQueue(first, 9 * 3600);
        lift.joinQueue(second, 9 * 3600);
        lift.joinQueue(third, 9 * 3600);
        Athlete[] passengers = lift.dispatch(9 * 3600 + 60);

        check(
                Arrays.equals(passengers, new Athlete[] { first, second }),
                "Lift should board waiting athletes in FIFO order up to capacity."
        );
        check(lift.getRideCount() == 2, "Lift should count boarded passengers.");
        check(
                lift.getWaitingAthleteCount() == 1,
                "Lift should leave excess athletes in the queue."
        );
        lift.finishRide(first, 9 * 3600 + 180);
        check(first.getCurrentNode() == top, "Lift should update passenger node.");
    }

    private static void testDifficultyMatchCriterion() {
        checkClose(
                DifficultyMatchCriterion.score(5, 5),
                1.0,
                "Exact skill match should score 1."
        );
        checkClose(
                DifficultyMatchCriterion.score(10, 5),
                0.0,
                "Too difficult route should score 0."
        );
        checkClose(
                DifficultyMatchCriterion.score(0, 10),
                0.2,
                "Too easy route should keep 0.2 floor."
        );
    }

    private static void testGroomingCriterion() {
        checkClose(
                GroomingCriterion.score(0.3, 1.0, 100),
                1.0,
                "Resistance 1 should keep perfect grooming."
        );
        check(
                GroomingCriterion.score(0.3, 0.5, 2)
                        < GroomingCriterion.score(0.3, 0.5, 1),
                "Grooming should decay when resistance is below 1."
        );
    }

    private static void testWeightedRouteAttractiveness() {
        Node first = new Node(0, 1000, 0, 0, true);
        Node second = new Node(1, 900, 0, 1, false);
        Route route = new Route(0, first, second, 5, 60, 0.3, 1.0);
        Athlete athlete = new Athlete(
                0,
                5,
                0.0,
                0.8,
                0.2,
                first,
                9 * 3600,
                false
        );
        checkClose(
                new WeightedRouteAttractivenessModel()
                        .attractiveness(route, athlete),
                1.0,
                "Weighted score should combine criteria."
        );
    }

    private static void testAthleteChoosesBestActivity() {
        Node start = new Node(0, 1000, 0, 0, true);
        Node firstEnd = new Node(1, 900, 0, 1, false);
        Node secondEnd = new Node(2, 850, 0, 2, false);
        Route weakRoute = new Route(0, start, firstEnd, 10, 60, 0.3, 1.0);
        Route strongRoute = new Route(1, start, secondEnd, 5, 60, 0.3, 1.0);
        start.addOutgoingRoute(weakRoute);
        start.addOutgoingRoute(strongRoute);
        Athlete athlete = athlete(0, start, 9 * 3600);

        ActivityChoice choice = athlete.chooseActivity(
                new Random(1),
                new WeightedRouteAttractivenessModel()
        );
        check(
                choice.getKind() == ActivityChoice.Kind.ROUTE
                        && choice.getRoute() == strongRoute,
                "Athlete should choose the most attractive route."
        );
    }

    private static void testAthleteRejectsDeadEnd() {
        Node isolated = new Node(0, 1000, 0, 0, true);
        Athlete athlete = athlete(0, isolated, 9 * 3600);
        checkThrows(
                () -> athlete.chooseActivity(
                        new Random(1),
                        new WeightedRouteAttractivenessModel()
                ),
                "Athlete should reject nodes without available activities."
        );
    }

    private static void testParser() {
        String input = """
                2
                1000 0 0 s
                900 0 1 s

                1
                1 0 30 2 120

                1
                0 1 4 60 0.3 0.9

                1
                2 4 0 s
                0.6 0.4
                0 09:00:00 10
                """;

        SimulationInput parsed = SimulationInputReader.read(
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))
        );
        check(
                parsed.getResortMap().getNodeCount() == 2,
                "Parser should read nodes."
        );
        check(
                parsed.getResortMap().getLiftCount() == 1,
                "Parser should read lifts."
        );
        check(
                parsed.getResortMap().getRouteCount() == 1,
                "Parser should read routes."
        );
        check(
                parsed.getAthletes().length == 2,
                "Parser should expand athlete groups."
        );
        check(
                parsed.getAthletes()[1].getArrivalTimeSeconds()
                        == TimeFormatter.parse("09:00:10"),
                "Parser should apply group arrival interval."
        );
        check(
                parsed.getAthletes()[0].isTracked(),
                "Parser should read tracked groups."
        );
    }

    private static void testSimulatorReportsOnlyTrackedAthletes() {
        Node top = new Node(0, 1000, 0, 1, true);
        Node bottom = new Node(1, 900, 0, 0, true);
        Route route = new Route(0, top, bottom, 5, 30, 1.0, 1.0);
        top.addOutgoingRoute(route);
        ResortMap resortMap = new ResortMap(
                new Node[] { top, bottom },
                new Lift[0],
                new Route[] { route }
        );
        Athlete[] athletes = new Athlete[] {
                new Athlete(0, 5, 0.0, 1.0, 0.0, top,
                        TimeFormatter.parse("14:59:59"), true),
                new Athlete(1, 5, 0.0, 1.0, 0.0, top,
                        TimeFormatter.parse("14:59:59"), false)
        };

        String outputText = runSimulator(resortMap, athletes);
        check(
                outputText.contains("Athlete 0 started route 0."),
                "Tracked athlete should be reported."
        );
        check(
                !outputText.contains("Athlete 1"),
                "Untracked athlete should use discard reporter."
        );
        check(route.getRideCount() == 2, "Untracked athlete should still ski.");
    }

    private static void testCutoffCompletion() {
        Node top = new Node(0, 1000, 0, 1, true);
        Node bottom = new Node(1, 900, 0, 0, true);
        Route route = new Route(0, top, bottom, 5, 30, 1.0, 1.0);
        top.addOutgoingRoute(route);
        Lift lift = new Lift(0, bottom, top, 60, 1, 60);
        bottom.addOutgoingLift(lift);

        ResortMap resortMap = new ResortMap(
                new Node[] { top, bottom },
                new Lift[] { lift },
                new Route[] { route }
        );
        Athlete[] athletes = new Athlete[] {
                new Athlete(
                        0,
                        5,
                        0.0,
                        1.0,
                        0.0,
                        top,
                        TimeFormatter.parse("14:59:59"),
                        true
                )
        };

        String outputText = runSimulator(resortMap, athletes);
        check(
                outputText.contains("14:59:59: Athlete 0 started route 0."),
                "Athlete should start before cutoff."
        );
        check(
                outputText.contains("15:00:29: Athlete 0 finished route 0."),
                "Already-started route should finish after cutoff."
        );
        check(
                !outputText.contains("15:00:29: Athlete 0 joined"),
                "Athlete should not start a new decision after cutoff."
        );
    }

    private static String runSimulator(ResortMap resortMap, Athlete[] athletes) {
        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(
                outputBytes,
                true,
                StandardCharsets.UTF_8
        );
        Simulator simulator = new Simulator(
                resortMap,
                athletes,
                new BinaryHeapEventQueue(),
                new Random(1),
                new WeightedRouteAttractivenessModel(),
                new ConsoleEventReporter(output)
        );
        simulator.run();
        new StatisticsPrinter(output).print(resortMap);
        return outputBytes.toString(StandardCharsets.UTF_8);
    }

    private static Athlete athlete(int id, Node node, int arrivalTimeSeconds) {
        return new Athlete(
                id,
                5,
                0.0,
                1.0,
                0.0,
                node,
                arrivalTimeSeconds,
                false
        );
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void checkThrows(Runnable operation, String message) {
        try {
            operation.run();
        } catch (IllegalStateException exception) {
            return;
        }
        throw new AssertionError(message);
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
