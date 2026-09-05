package skiresort.model;

public final class Route {
    private final int id;
    private final Node startNode;
    private final Node endNode;
    private final int difficulty;
    private final int travelTimeSeconds;
    private final double baseAttractiveness;
    private final double resistance;
    private int rideCount;

    public Route(
            int id,
            Node startNode,
            Node endNode,
            int difficulty,
            int travelTimeSeconds,
            double baseAttractiveness,
            double resistance
    ) {
        this.id = id;
        this.startNode = startNode;
        this.endNode = endNode;
        this.difficulty = difficulty;
        this.travelTimeSeconds = travelTimeSeconds;
        this.baseAttractiveness = baseAttractiveness;
        this.resistance = resistance;
    }

    public int getId() {
        return id;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getTravelTimeSeconds() {
        return travelTimeSeconds;
    }

    public double getBaseAttractiveness() {
        return baseAttractiveness;
    }

    public double getResistance() {
        return resistance;
    }

    public int getRideCount() {
        return rideCount;
    }

    public void recordRideStart() {
        rideCount++;
    }
}
