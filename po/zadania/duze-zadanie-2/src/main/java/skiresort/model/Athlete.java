package skiresort.model;

import java.util.List;
import java.util.Random;

import skiresort.decision.AthleteDecisionStrategy;
import skiresort.decision.AthleteDecisionStrategyFactory;
import skiresort.graph.ShortestPathFinder;
import skiresort.output.DiscardEventReporter;
import skiresort.output.EventReporter;
import skiresort.simulation.RouteAttractivenessModel;

public final class Athlete {
    private final int id;
    private final int skillLevel;
    private final double spontaneity;
    private final AthleteKind kind;
    private final double difficultyWeight;
    private final double groomingWeight;
    private final double boredomWeight;
    private final Node startNode;
    private final int arrivalTimeSeconds;
    private final boolean tracked;
    private final BoredomMemory boredomMemory;
    private final AthleteHistory history;
    private final AthleteDecisionStrategy decisionStrategy;

    private Node currentNode;
    private EventReporter reporter;

    public Athlete(
            int id,
            int skillLevel,
            double spontaneity,
            double boredomFactor,
            AthleteKind kind,
            double difficultyWeight,
            double groomingWeight,
            double boredomWeight,
            Node startNode,
            int arrivalTimeSeconds,
            boolean tracked,
            int routeCount
    ) {
        this.id = id;
        this.skillLevel = skillLevel;
        this.spontaneity = spontaneity;
        this.kind = kind;
        this.difficultyWeight = difficultyWeight;
        this.groomingWeight = groomingWeight;
        this.boredomWeight = boredomWeight;
        this.startNode = startNode;
        this.arrivalTimeSeconds = arrivalTimeSeconds;
        this.tracked = tracked;
        boredomMemory = new BoredomMemory(boredomFactor, routeCount);
        history = new AthleteHistory(tracked);
        decisionStrategy = AthleteDecisionStrategyFactory.create(kind);
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

    public double getBoredomFactor() {
        return boredomMemory.getBoredomFactor();
    }

    public AthleteKind getKind() {
        return kind;
    }

    public double getDifficultyWeight() {
        return difficultyWeight;
    }

    public double getGroomingWeight() {
        return groomingWeight;
    }

    public double getBoredomWeight() {
        return boredomWeight;
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
        history.recordRouteTraversal(route);
        reporter.routeStarted(timeSeconds, this, route);
        route.recordRideStart();
    }

    public void finishRoute(Route route, int timeSeconds) {
        currentNode = route.getEndNode();
        boredomMemory.recordRouteRide(route);
        reporter.routeFinished(timeSeconds, this, route);
    }

    public void joinLiftQueue(Lift lift, int timeSeconds) {
        reporter.liftQueueJoined(timeSeconds, this, lift);
    }

    public void startLift(Lift lift, int timeSeconds) {
        history.recordLiftTraversal(lift);
        reporter.liftStarted(timeSeconds, this, lift);
    }

    public void leaveLift(Lift lift, int timeSeconds) {
        currentNode = lift.getEndStation();
        reporter.liftLeft(timeSeconds, this, lift);
    }

    public ActivityChoice chooseActivity(
            Random random,
            RouteAttractivenessModel attractivenessModel,
            ResortMap resortMap,
            ShortestPathFinder pathFinder
    ) {
        ActivityChoice choice = decisionStrategy.chooseActivity(
                this,
                random,
                attractivenessModel,
                resortMap,
                pathFinder
        );
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

    public double getBoredom(Route route) {
        return boredomMemory.boredom(route);
    }

    public int getPersonalRouteRideCount(Route route) {
        return boredomMemory.personalRouteRideCount(route);
    }

    public List<Integer> getRouteTraversalNumbers(int routeId) {
        return history.routeTraversalNumbers(routeId);
    }

    public List<Integer> getLiftTraversalNumbers(int liftId) {
        return history.liftTraversalNumbers(liftId);
    }
}
