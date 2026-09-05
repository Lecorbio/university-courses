package skiresort.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import skiresort.graph.ShortestPath;
import skiresort.graph.ShortestPathFinder;
import skiresort.model.Athlete;
import skiresort.model.AthleteKind;
import skiresort.model.Lift;
import skiresort.model.Node;
import skiresort.model.ResortMap;
import skiresort.model.Route;
import skiresort.output.TimeFormatter;

public final class LiftAndBfsJUnitTests {
    @Test
    public void liftLeavesExcessPassengerWhenCapacityIsFull() {
        Lift lift = testLift(3);
        Node bottom = lift.getStartStation();
        for (int id = 0; id < 4; id++) {
            lift.joinQueue(athlete(id, bottom), TimeFormatter.parse("09:00:00"));
        }

        Athlete[] passengers = lift.dispatch(TimeFormatter.parse("09:01:00"));

        assertEquals(3, passengers.length);
        assertEquals(3, lift.getRideCount());
        assertEquals(1, lift.getWaitingAthleteCount());
    }

    @Test
    public void liftTakesAllPassengersWhenQueueIsShorterThanCapacity() {
        Lift lift = testLift(3);
        Node bottom = lift.getStartStation();
        lift.joinQueue(athlete(0, bottom), TimeFormatter.parse("09:00:00"));
        lift.joinQueue(athlete(1, bottom), TimeFormatter.parse("09:00:00"));

        Athlete[] passengers = lift.dispatch(TimeFormatter.parse("09:01:00"));

        assertEquals(2, passengers.length);
        assertEquals(2, lift.getRideCount());
        assertEquals(0, lift.getWaitingAthleteCount());
    }

    @Test
    public void liftTracksMaximumQueueLength() {
        Lift lift = testLift(3);
        Node bottom = lift.getStartStation();
        for (int id = 0; id < 4; id++) {
            lift.joinQueue(athlete(id, bottom), TimeFormatter.parse("09:00:00"));
        }
        lift.dispatch(TimeFormatter.parse("09:01:00"));
        lift.joinQueue(athlete(4, bottom), TimeFormatter.parse("09:02:00"));

        assertEquals(4, lift.getMaximumQueueLength());
    }

    @Test
    public void bfsFindsPathFromZeroToFour() {
        BfsFixture fixture = bfsFixture();

        ShortestPath path = fixture.finder.shortestPath(
                fixture.nodes[0],
                fixture.nodes[4]
        );

        assertEquals(3, path.distance());
        assertNodePath(path, 0, 1, 2, 4);
    }

    @Test
    public void bfsFindsDirectPath() {
        BfsFixture fixture = bfsFixture();

        ShortestPath path = fixture.finder.shortestPath(
                fixture.nodes[3],
                fixture.nodes[1]
        );

        assertEquals(1, path.distance());
        assertNodePath(path, 3, 1);
    }

    @Test
    public void bfsReturnsEmptyPathToSameNode() {
        BfsFixture fixture = bfsFixture();

        ShortestPath path = fixture.finder.shortestPath(
                fixture.nodes[2],
                fixture.nodes[2]
        );

        assertEquals(0, path.distance());
        assertTrue(path.activities().isEmpty());
        assertNodePath(path, 2);
    }

    @Test
    public void bfsAcceptsAnyShortestAlternative() {
        BfsFixture fixture = bfsFixture();

        ShortestPath path = fixture.finder.shortestPath(
                fixture.nodes[4],
                fixture.nodes[3]
        );

        assertEquals(2, path.distance());
        assertSame(fixture.nodes[4], path.nodes().get(0));
        assertSame(fixture.nodes[3], path.nodes().get(2));
        int middleId = path.nodes().get(1).getId();
        assertTrue(middleId == 1 || middleId == 2);
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

    private static void assertNodePath(ShortestPath path, int... nodeIds) {
        List<Node> nodes = path.nodes();
        assertEquals(nodeIds.length, nodes.size());
        for (int i = 0; i < nodeIds.length; i++) {
            assertEquals(nodeIds[i], nodes.get(i).getId());
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
}
