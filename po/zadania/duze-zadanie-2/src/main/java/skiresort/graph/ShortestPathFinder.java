package skiresort.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import skiresort.model.ActivityChoice;
import skiresort.model.Lift;
import skiresort.model.Node;
import skiresort.model.ResortMap;
import skiresort.model.Route;

public final class ShortestPathFinder {
    private final ResortMap resortMap;

    public ShortestPathFinder(ResortMap resortMap) {
        this.resortMap = resortMap;
    }

    public SearchResult searchFrom(Node start) {
        if (start == null) {
            throw new IllegalArgumentException("Start node cannot be null.");
        }

        int nodeCount = resortMap.getNodeCount();
        boolean[] visited = new boolean[nodeCount];
        int[] distance = new int[nodeCount];
        Node[] predecessor = new Node[nodeCount];
        ActivityChoice[] incomingActivity = new ActivityChoice[nodeCount];
        ArrayDeque<Node> queue = new ArrayDeque<>();

        visited[start.getId()] = true;
        queue.addLast(start);

        while (!queue.isEmpty()) {
            Node node = queue.removeFirst();
            int nextDistance = distance[node.getId()] + 1;

            for (int i = 0; i < node.getOutgoingRouteCount(); i++) {
                Route route = node.getOutgoingRoute(i);
                Node nextNode = route.getEndNode();
                if (!visited[nextNode.getId()]) {
                    visited[nextNode.getId()] = true;
                    distance[nextNode.getId()] = nextDistance;
                    predecessor[nextNode.getId()] = node;
                    incomingActivity[nextNode.getId()] =
                            ActivityChoice.forRoute(route);
                    queue.addLast(nextNode);
                }
            }

            for (int i = 0; i < node.getOutgoingLiftCount(); i++) {
                Lift lift = node.getOutgoingLift(i);
                Node nextNode = lift.getEndStation();
                if (!visited[nextNode.getId()]) {
                    visited[nextNode.getId()] = true;
                    distance[nextNode.getId()] = nextDistance;
                    predecessor[nextNode.getId()] = node;
                    incomingActivity[nextNode.getId()] =
                            ActivityChoice.forLift(lift);
                    queue.addLast(nextNode);
                }
            }
        }

        return new SearchResult(
                start,
                visited,
                distance,
                predecessor,
                incomingActivity
        );
    }

    public ShortestPath shortestPath(Node start, Node target) {
        return searchFrom(start).pathTo(target);
    }

    public ShortestPath shortestPathToRoute(Node start, Route targetRoute) {
        return searchFrom(start).pathToRoute(targetRoute);
    }

    public static final class SearchResult {
        private final Node start;
        private final boolean[] visited;
        private final int[] distance;
        private final Node[] predecessor;
        private final ActivityChoice[] incomingActivity;

        private SearchResult(
                Node start,
                boolean[] visited,
                int[] distance,
                Node[] predecessor,
                ActivityChoice[] incomingActivity
        ) {
            this.start = start;
            this.visited = visited;
            this.distance = distance;
            this.predecessor = predecessor;
            this.incomingActivity = incomingActivity;
        }

        public boolean canReach(Node target) {
            return target != null && visited[target.getId()];
        }

        public int distanceTo(Node target) {
            if (!canReach(target)) {
                return Integer.MAX_VALUE;
            }
            return distance[target.getId()];
        }

        public ShortestPath pathToRoute(Route targetRoute) {
            if (targetRoute == null) {
                throw new IllegalArgumentException("Target route cannot be null.");
            }

            ShortestPath pathToStart = pathTo(targetRoute.getStartNode());
            if (!pathToStart.isReachable()) {
                return ShortestPath.unreachable();
            }

            ArrayList<Node> nodes = new ArrayList<>(pathToStart.nodes());
            nodes.add(targetRoute.getEndNode());
            ArrayList<ActivityChoice> activities =
                    new ArrayList<>(pathToStart.activities());
            activities.add(ActivityChoice.forRoute(targetRoute));
            return ShortestPath.reachable(nodes, activities);
        }

        public ShortestPath pathTo(Node target) {
            if (target == null || !visited[target.getId()]) {
                return ShortestPath.unreachable();
            }
            if (target == start) {
                return ShortestPath.reachable(List.of(start), List.of());
            }

            ArrayList<Node> nodes = new ArrayList<>();
            ArrayList<ActivityChoice> activities = new ArrayList<>();
            Node current = target;
            while (current != start) {
                nodes.add(current);
                activities.add(incomingActivity[current.getId()]);
                current = predecessor[current.getId()];
            }
            nodes.add(start);
            Collections.reverse(nodes);
            Collections.reverse(activities);
            return ShortestPath.reachable(nodes, activities);
        }
    }
}
