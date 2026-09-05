package skiresort.decision;

import java.util.Random;

import skiresort.graph.ShortestPathFinder;
import skiresort.model.ActivityChoice;
import skiresort.model.Athlete;
import skiresort.model.ResortMap;
import skiresort.simulation.RouteAttractivenessModel;

public interface AthleteDecisionStrategy {
    ActivityChoice chooseActivity(
            Athlete athlete,
            Random random,
            RouteAttractivenessModel attractivenessModel,
            ResortMap resortMap,
            ShortestPathFinder pathFinder
    );
}
