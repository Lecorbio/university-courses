package skiresort.decision;

import skiresort.graph.ShortestPathFinder;
import skiresort.model.Athlete;
import skiresort.model.ResortMap;
import skiresort.model.Route;
import skiresort.simulation.RouteAttractivenessModel;

final class GreedyDecisionStrategy extends PlannedRouteDecisionStrategy {
    @Override
    protected Route chooseTargetRoute(
            Athlete athlete,
            RouteAttractivenessModel attractivenessModel,
            ResortMap resortMap,
            ShortestPathFinder.SearchResult search
    ) {
        Route bestRoute = null;
        double bestScore = -1.0;
        for (int id = 0; id < resortMap.getRouteCount(); id++) {
            Route route = resortMap.getRoute(id);
            if (!search.canReach(route.getStartNode())) {
                continue;
            }

            double score = attractivenessModel.attractiveness(route, athlete);
            if (score > bestScore) {
                bestScore = score;
                bestRoute = route;
            }
        }
        return bestRoute;
    }
}
