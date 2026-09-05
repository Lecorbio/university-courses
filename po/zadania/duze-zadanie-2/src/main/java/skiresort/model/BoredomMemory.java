package skiresort.model;

final class BoredomMemory {
    private final double boredomFactor;
    private final double[] boredomValues;
    private final int[] boredomUpdateRideNumbers;
    private final int[] routeRideCounts;
    private int completedRouteRideCount;

    BoredomMemory(double boredomFactor, int routeCount) {
        this.boredomFactor = boredomFactor;
        boredomValues = new double[routeCount];
        boredomUpdateRideNumbers = new int[routeCount];
        routeRideCounts = new int[routeCount];
    }

    double getBoredomFactor() {
        return boredomFactor;
    }

    double boredom(Route route) {
        int routeId = route.getId();
        double decay = Math.pow(
                1.0 - boredomFactor,
                completedRouteRideCount - boredomUpdateRideNumbers[routeId]
        );
        return boredomValues[routeId] * decay;
    }

    int personalRouteRideCount(Route route) {
        return routeRideCounts[route.getId()];
    }

    void recordRouteRide(Route route) {
        int routeId = route.getId();
        double previousBoredom = boredom(route);
        completedRouteRideCount++;
        boredomValues[routeId] =
                boredomFactor + (1.0 - boredomFactor) * previousBoredom;
        boredomUpdateRideNumbers[routeId] = completedRouteRideCount;
        routeRideCounts[routeId]++;
    }
}
