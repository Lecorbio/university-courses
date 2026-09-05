package skiresort.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class AthleteHistory {
    private final boolean tracked;
    private final Map<Integer, List<Integer>> routeTraversalNumbers =
            new HashMap<>();
    private final Map<Integer, List<Integer>> liftTraversalNumbers =
            new HashMap<>();
    private int traversalCount;

    AthleteHistory(boolean tracked) {
        this.tracked = tracked;
    }

    void recordRouteTraversal(Route route) {
        if (tracked) {
            recordTraversal(routeTraversalNumbers, route.getId());
        }
    }

    void recordLiftTraversal(Lift lift) {
        if (tracked) {
            recordTraversal(liftTraversalNumbers, lift.getId());
        }
    }

    List<Integer> routeTraversalNumbers(int routeId) {
        return routeTraversalNumbers.getOrDefault(routeId, List.of());
    }

    List<Integer> liftTraversalNumbers(int liftId) {
        return liftTraversalNumbers.getOrDefault(liftId, List.of());
    }

    private void recordTraversal(Map<Integer, List<Integer>> records, int id) {
        traversalCount++;
        records.computeIfAbsent(id, ignored -> new ArrayList<>())
                .add(traversalCount);
    }
}
