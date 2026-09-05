package skiresort.model;

import java.util.Random;

import skiresort.output.DiscardEventReporter;
import skiresort.output.EventReporter;
import skiresort.simulation.RouteAttractivenessModel;

public final class Athlete {
    private final int id;
    private final int skillLevel;
    private final double spontaneity;
    private final double difficultyWeight;
    private final double groomingWeight;
    private final Node startNode;
    private final int arrivalTimeSeconds;
    private final boolean tracked;

    private Node currentNode;
    private EventReporter reporter;

    public Athlete(
            int id,
            int skillLevel,
            double spontaneity,
            double difficultyWeight,
            double groomingWeight,
            Node startNode,
            int arrivalTimeSeconds,
            boolean tracked
    ) {
        this.id = id;
        this.skillLevel = skillLevel;
        this.spontaneity = spontaneity;
        this.difficultyWeight = difficultyWeight;
        this.groomingWeight = groomingWeight;
        this.startNode = startNode;
        this.arrivalTimeSeconds = arrivalTimeSeconds;
        this.tracked = tracked;
        currentNode = startNode;
        reporter = DiscardEventReporter.INSTANCE;
    }

    public int getId() {
        return id;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public double getSpontaneity() {
        return spontaneity;
    }

    public double getDifficultyWeight() {
        return difficultyWeight;
    }

    public double getGroomingWeight() {
        return groomingWeight;
    }

    public Node getStartNode() {
        return startNode;
    }

    public int getArrivalTimeSeconds() {
        return arrivalTimeSeconds;
    }

    public boolean isTracked() {
        return tracked;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setEventReporter(EventReporter reporter) {
        if (reporter == null) {
            throw new IllegalArgumentException("Event reporter cannot be null.");
        }
        this.reporter = reporter;
    }

    public void arriveAtStart() {
        currentNode = startNode;
    }

    public void startRoute(Route route, int timeSeconds) {
        reporter.routeStarted(timeSeconds, this, route);
        route.recordRideStart();
    }

    public void finishRoute(Route route, int timeSeconds) {
        currentNode = route.getEndNode();
        reporter.routeFinished(timeSeconds, this, route);
    }

    public void joinLiftQueue(Lift lift, int timeSeconds) {
        reporter.liftQueueJoined(timeSeconds, this, lift);
    }

    public void startLift(Lift lift, int timeSeconds) {
        reporter.liftStarted(timeSeconds, this, lift);
    }

    public void leaveLift(Lift lift, int timeSeconds) {
        currentNode = lift.getEndStation();
        reporter.liftLeft(timeSeconds, this, lift);
    }

    public ActivityChoice chooseActivity(
            Random random,
            RouteAttractivenessModel attractivenessModel
    ) {
        ActivityChoice choice;
        if (random.nextDouble(0.0, 1.0) < spontaneity) {
            choice = chooseRandomOutgoing(random, currentNode);
        } else {
            choice = chooseBestActivity(attractivenessModel);
            if (choice == null) {
                choice = chooseRandomOutgoing(random, currentNode);
            }
        }

        if (choice == null) {
            throw new IllegalStateException(
                    "Athlete "
                            + id
                            + " has no available activity at node "
                            + currentNode.getId()
                            + "."
            );
        }
        return choice;
    }

    private static ActivityChoice chooseRandomOutgoing(Random random, Node node) {
        int routeCount = node.getOutgoingRouteCount();
        int liftCount = node.getOutgoingLiftCount();
        int total = routeCount + liftCount;
        if (total == 0) {
            return null;
        }

        int selected = random.nextInt(total);
        if (selected < routeCount) {
            return ActivityChoice.forRoute(node.getOutgoingRoute(selected));
        }
        return ActivityChoice.forLift(
                node.getOutgoingLift(selected - routeCount)
        );
    }

    private ActivityChoice chooseBestActivity(
            RouteAttractivenessModel attractivenessModel
    ) {
        ActivityChoice bestChoice = null;
        double bestScore = -1.0;

        for (int i = 0; i < currentNode.getOutgoingRouteCount(); i++) {
            Route route = currentNode.getOutgoingRoute(i);
            double score = attractivenessModel.attractiveness(route, this);
            if (score > bestScore) {
                bestScore = score;
                bestChoice = ActivityChoice.forRoute(route);
            }
        }

        for (int i = 0; i < currentNode.getOutgoingLiftCount(); i++) {
            Lift lift = currentNode.getOutgoingLift(i);
            Node liftEnd = lift.getEndStation();
            for (
                    int routeIndex = 0;
                    routeIndex < liftEnd.getOutgoingRouteCount();
                    routeIndex++
            ) {
                Route route = liftEnd.getOutgoingRoute(routeIndex);
                double score = attractivenessModel.attractiveness(route, this);
                if (score > bestScore) {
                    bestScore = score;
                    bestChoice = ActivityChoice.forLift(lift);
                }
            }
        }

        return bestChoice;
    }
}
