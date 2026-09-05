package skiresort.decision;

import java.util.ArrayDeque;
import java.util.Random;

import skiresort.graph.ShortestPath;
import skiresort.graph.ShortestPathFinder;
import skiresort.model.ActivityChoice;
import skiresort.model.Athlete;
import skiresort.model.ResortMap;
import skiresort.model.Route;
import skiresort.simulation.RouteAttractivenessModel;

abstract class PlannedRouteDecisionStrategy implements AthleteDecisionStrategy {
    private final ArrayDeque<ActivityChoice> plannedActivities =
            new ArrayDeque<>();

    @Override
    public final ActivityChoice chooseActivity(
            Athlete athlete,
            Random random,
            RouteAttractivenessModel attractivenessModel,
            ResortMap resortMap,
            ShortestPathFinder pathFinder
    ) {
        if (!plannedActivities.isEmpty()) {
            return plannedActivities.removeFirst();
        }

        if (random.nextDouble(0.0, 1.0) < athlete.getSpontaneity()) {
            return DecisionSupport.chooseRandomOutgoing(
                    random,
                    athlete.getCurrentNode()
            );
        }

        ShortestPathFinder.SearchResult search =
                pathFinder.searchFrom(athlete.getCurrentNode());
        Route targetRoute = chooseTargetRoute(
                athlete,
                attractivenessModel,
                resortMap,
                search
        );
        if (targetRoute == null) {
            return DecisionSupport.chooseRandomOutgoing(
                    random,
                    athlete.getCurrentNode()
            );
        }

        ShortestPath plan = search.pathToRoute(targetRoute);
        plannedActivities.addAll(plan.activities());
        return plannedActivities.removeFirst();
    }

    protected abstract Route chooseTargetRoute(
            Athlete athlete,
            RouteAttractivenessModel attractivenessModel,
            ResortMap resortMap,
            ShortestPathFinder.SearchResult search
    );
}
