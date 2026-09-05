package skiresort.simulation;

public final class GroomingCriterion {
    // Utility class for the formula from the assignment statement.
    private GroomingCriterion() {
    }

    public static double score(
            double baseAttractiveness,
            double resistance,
            int previousRideCount
    ) {
        return baseAttractiveness
                + (1.0 - baseAttractiveness)
                * Math.pow(resistance, previousRideCount);
    }
}
