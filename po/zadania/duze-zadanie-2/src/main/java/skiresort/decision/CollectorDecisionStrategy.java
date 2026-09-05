package skiresort.decision;

import skiresort.graph.ShortestPathFinder;
import skiresort.model.Athlete;
import skiresort.model.ResortMap;
import skiresort.model.Route;
import skiresort.simulation.RouteAttractivenessModel;

final class CollectorDecisionStrategy extends PlannedRouteDecisionStrategy {
    @Override
    protected Route chooseTargetRoute(
            Athlete athlete,
            RouteAttractivenessModel attractivenessModel,
            ResortMap resortMap,
            ShortestPathFinder.SearchResult search
    ) {
        Route bestRoute = null;
        int bestRideCount = Integer.MAX_VALUE;
        int bestDistance = Integer.MAX_VALUE;
        double bestAttractiveness = -1.0;

        for (int id = 0; id < resortMap.getRouteCount(); id++) {
            Route route = resortMap.getRoute(id);
            if (!search.canReach(route.getStartNode())) {
                continue;
            }

            int rideCount = athlete.getPersonalRouteRideCount(route);
            int distance = search.distanceTo(route.getStartNode()) + 1;
            double attractiveness =
                    attractivenessModel.attractiveness(route, athlete);

            if (rideCount < bestRideCount
                    || rideCount == bestRideCount
                            && distance < bestDistance
                    || rideCount == bestRideCount
                            && distance == bestDistance
                            && attractiveness > bestAttractiveness) {
                bestRoute = route;
                bestRideCount = rideCount;
                bestDistance = distance;
                bestAttractiveness = attractiveness;
            }
        }

        return bestRoute;
    }
}
