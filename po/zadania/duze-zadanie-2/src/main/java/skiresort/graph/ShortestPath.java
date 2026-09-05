package skiresort.graph;

import java.util.List;

import skiresort.model.ActivityChoice;
import skiresort.model.Node;

public final class ShortestPath {
    private final boolean reachable;
    private final List<Node> nodes;
    private final List<ActivityChoice> activities;

    private ShortestPath(
            boolean reachable,
            List<Node> nodes,
            List<ActivityChoice> activities
    ) {
        this.reachable = reachable;
        this.nodes = List.copyOf(nodes);
        this.activities = List.copyOf(activities);
    }

    public static ShortestPath reachable(
            List<Node> nodes,
            List<ActivityChoice> activities
    ) {
        return new ShortestPath(true, nodes, activities);
    }

    public static ShortestPath unreachable() {
        return new ShortestPath(false, List.of(), List.of());
    }

    public boolean isReachable() {
        return reachable;
    }

    public int distance() {
        return activities.size();
    }

    public List<Node> nodes() {
        return nodes;
    }

    public List<ActivityChoice> activities() {
        return activities;
    }
}
