package skiresort.decision;

import java.util.Random;

import skiresort.graph.ShortestPathFinder;
import skiresort.model.ActivityChoice;
import skiresort.model.Athlete;
import skiresort.model.ResortMap;
import skiresort.simulation.RouteAttractivenessModel;

final class LocalDecisionStrategy implements AthleteDecisionStrategy {
    @Override
    public ActivityChoice chooseActivity(
            Athlete athlete,
            Random random,
            RouteAttractivenessModel attractivenessModel,
            ResortMap resortMap,
            ShortestPathFinder pathFinder
    ) {
        if (random.nextDouble(0.0, 1.0) < athlete.getSpontaneity()) {
            return DecisionSupport.chooseRandomOutgoing(
                    random,
                    athlete.getCurrentNode()
            );
        }

        ActivityChoice choice = DecisionSupport.chooseBestLocalActivity(
                athlete,
                attractivenessModel
        );
        if (choice != null) {
            return choice;
        }
        return DecisionSupport.chooseRandomOutgoing(
                random,
                athlete.getCurrentNode()
        );
    }
}
