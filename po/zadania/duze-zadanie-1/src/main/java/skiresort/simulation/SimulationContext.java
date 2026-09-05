package skiresort.simulation;

import java.util.Random;

import skiresort.collections.EventQueue;
import skiresort.model.ActivityChoice;
import skiresort.model.Athlete;
import skiresort.model.Lift;
import skiresort.model.Route;
import skiresort.simulation.events.Event;
import skiresort.simulation.events.LiftArrivalEvent;
import skiresort.simulation.events.LiftDispatchEvent;
import skiresort.simulation.events.RouteFinishEvent;

public final class SimulationContext {
    private final EventQueue eventQueue;
    private final Random random;
    private final RouteAttractivenessModel attractivenessModel;

    public SimulationContext(
            EventQueue eventQueue,
            Random random,
            RouteAttractivenessModel attractivenessModel
    ) {
        this.eventQueue = eventQueue;
        this.random = random;
        this.attractivenessModel = attractivenessModel;
    }

    public void schedule(Event event) {
        eventQueue.add(event);
    }

    public void athleteArrived(Athlete athlete, int timeSeconds) {
        athlete.arriveAtStart();
        handleAthleteReady(athlete, timeSeconds);
    }

    public void finishRoute(Athlete athlete, Route route, int timeSeconds) {
        athlete.finishRoute(route, timeSeconds);
        handleAthleteReady(athlete, timeSeconds);
    }

    public void dispatchLift(Lift lift, int timeSeconds) {
        int nextDispatchTime = lift.nextDispatchTimeAfter(timeSeconds);
        if (nextDispatchTime < SimulationClock.DECISION_CUTOFF_SECONDS) {
            schedule(new LiftDispatchEvent(nextDispatchTime, lift));
        }

        Athlete[] passengers = lift.dispatch(timeSeconds);
        if (passengers.length == 0) {
            return;
        }
        schedule(
                new LiftArrivalEvent(
                        timeSeconds + lift.getTravelTimeSeconds(),
                        lift,
                        passengers
                )
        );
    }

    public void finishLiftRide(
            Lift lift,
            Athlete[] passengers,
            int timeSeconds
    ) {
        for (int i = 0; i < passengers.length; i++) {
            Athlete athlete = passengers[i];
            lift.finishRide(athlete, timeSeconds);
            handleAthleteReady(athlete, timeSeconds);
        }
    }

    public void handleAthleteReady(Athlete athlete, int timeSeconds) {
        // Started activities may finish after 15:00, but no new choices start.
        if (timeSeconds >= SimulationClock.DECISION_CUTOFF_SECONDS) {
            return;
        }

        ActivityChoice choice = athlete.chooseActivity(
                random,
                attractivenessModel
        );
        executeChoice(athlete, choice, timeSeconds);
    }

    private void executeChoice(
            Athlete athlete,
            ActivityChoice choice,
            int timeSeconds
    ) {
        switch (choice.getKind()) {
            case ROUTE:
                startRoute(athlete, choice.getRoute(), timeSeconds);
                break;
            case LIFT:
                choice.getLift().joinQueue(athlete, timeSeconds);
                break;
        }
    }

    private void startRoute(Athlete athlete, Route route, int timeSeconds) {
        athlete.startRoute(route, timeSeconds);
        schedule(
                new RouteFinishEvent(
                        timeSeconds + route.getTravelTimeSeconds(),
                        athlete,
                        route
                )
        );
    }
}
