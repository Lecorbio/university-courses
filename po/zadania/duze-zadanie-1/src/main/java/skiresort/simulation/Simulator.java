package skiresort.simulation;

import java.util.Random;

import skiresort.collections.EventQueue;
import skiresort.model.Athlete;
import skiresort.model.ResortMap;
import skiresort.output.DiscardEventReporter;
import skiresort.output.EventReporter;
import skiresort.simulation.events.AthleteArrivalEvent;
import skiresort.simulation.events.Event;
import skiresort.simulation.events.LiftDispatchEvent;

public final class Simulator {
    private final ResortMap resortMap;
    private final Athlete[] athletes;
    private final EventQueue eventQueue;
    private final SimulationContext context;

    public Simulator(
            ResortMap resortMap,
            Athlete[] athletes,
            EventQueue eventQueue,
            Random random,
            RouteAttractivenessModel attractivenessModel,
            EventReporter reporter
    ) {
        this.resortMap = resortMap;
        this.athletes = athletes;
        this.eventQueue = eventQueue;
        assignEventReporters(reporter);
        context = new SimulationContext(
                eventQueue,
                random,
                attractivenessModel
        );
    }

    public void run() {
        scheduleInitialEvents();
        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.pollFirst();
            event.handle(context);
        }
    }

    private void scheduleInitialEvents() {
        for (int i = 0; i < athletes.length; i++) {
            context.schedule(
                    new AthleteArrivalEvent(
                            athletes[i].getArrivalTimeSeconds(),
                            athletes[i]
                    )
            );
        }
        for (int id = 0; id < resortMap.getLiftCount(); id++) {
            context.schedule(
                    new LiftDispatchEvent(
                            SimulationClock.OPENING_SECONDS,
                            resortMap.getLift(id)
                    )
            );
        }
    }

    private void assignEventReporters(EventReporter reporter) {
        for (int i = 0; i < athletes.length; i++) {
            if (athletes[i].isTracked()) {
                athletes[i].setEventReporter(reporter);
            } else {
                athletes[i].setEventReporter(DiscardEventReporter.INSTANCE);
            }
        }
    }
}
