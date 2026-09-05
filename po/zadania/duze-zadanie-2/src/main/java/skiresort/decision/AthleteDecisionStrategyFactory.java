package skiresort.decision;

import skiresort.model.AthleteKind;

public final class AthleteDecisionStrategyFactory {
    private AthleteDecisionStrategyFactory() {
    }

    public static AthleteDecisionStrategy create(AthleteKind athleteKind) {
        return switch (athleteKind) {
            case LOCAL -> new LocalDecisionStrategy();
            case GREEDY -> new GreedyDecisionStrategy();
            case COLLECTOR -> new CollectorDecisionStrategy();
        };
    }
}
