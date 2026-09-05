package skiresort.simulation;

public final class DifficultyMatchCriterion {
    // Utility class for the formula from the assignment statement.
    private DifficultyMatchCriterion() {
    }

    public static double score(int routeDifficulty, int athleteSkillLevel) {
        if (routeDifficulty >= athleteSkillLevel + 5) {
            return 0.0;
        }
        if (routeDifficulty >= athleteSkillLevel) {
            return 1.0 - (routeDifficulty - athleteSkillLevel) / 5.0;
        }
        return Math.max(0.2, 1.0 - (athleteSkillLevel - routeDifficulty) / 7.0);
    }
}
