package skiresort.decision;

import java.util.Random;

import skiresort.model.ActivityChoice;
import skiresort.model.Athlete;
import skiresort.model.Lift;
import skiresort.model.Node;
import skiresort.model.Route;
import skiresort.simulation.RouteAttractivenessModel;

final class DecisionSupport {
    private DecisionSupport() {
    }

    static ActivityChoice chooseRandomOutgoing(Random random, Node node) {
        int routeCount = node.getOutgoingRouteCount();
        int liftCount = node.getOutgoingLiftCount();
        int total = routeCount + liftCount;
        if (total == 0) {
            return null;
        }

        int selected = random.nextInt(total);
        if (selected < routeCount) {
            return ActivityChoice.forRoute(node.getOutgoingRoute(selected));
        }
        return ActivityChoice.forLift(
                node.getOutgoingLift(selected - routeCount)
        );
    }

    static ActivityChoice chooseBestLocalActivity(
            Athlete athlete,
            RouteAttractivenessModel attractivenessModel
    ) {
        Node currentNode = athlete.getCurrentNode();
        ActivityChoice bestChoice = null;
        double bestScore = -1.0;

        for (int i = 0; i < currentNode.getOutgoingRouteCount(); i++) {
            Route route = currentNode.getOutgoingRoute(i);
            double score = attractivenessModel.attractiveness(route, athlete);
            if (score > bestScore) {
                bestScore = score;
                bestChoice = ActivityChoice.forRoute(route);
            }
        }

        for (int i = 0; i < currentNode.getOutgoingLiftCount(); i++) {
            Lift lift = currentNode.getOutgoingLift(i);
            Node liftEnd = lift.getEndStation();
            for (
                    int routeIndex = 0;
                    routeIndex < liftEnd.getOutgoingRouteCount();
                    routeIndex++
            ) {
                Route route = liftEnd.getOutgoingRoute(routeIndex);
                double score = attractivenessModel.attractiveness(route, athlete);
                if (score > bestScore) {
                    bestScore = score;
                    bestChoice = ActivityChoice.forLift(lift);
                }
            }
        }

        return bestChoice;
    }
}
