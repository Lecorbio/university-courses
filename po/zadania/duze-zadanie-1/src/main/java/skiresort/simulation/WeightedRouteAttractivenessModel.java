package skiresort.simulation;

import skiresort.model.Athlete;
import skiresort.model.Route;

public final class WeightedRouteAttractivenessModel
        implements RouteAttractivenessModel {
    @Override
    public double attractiveness(Route route, Athlete athlete) {
        double difficulty = DifficultyMatchCriterion.score(
                route.getDifficulty(),
                athlete.getSkillLevel()
        );
        double grooming = GroomingCriterion.score(
                route.getBaseAttractiveness(),
                route.getResistance(),
                route.getRideCount()
        );
        return athlete.getDifficultyWeight() * difficulty
                + athlete.getGroomingWeight() * grooming;
    }
}
