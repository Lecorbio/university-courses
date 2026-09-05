package skiresort.output;

import java.io.PrintStream;

import skiresort.model.Athlete;
import skiresort.model.Lift;
import skiresort.model.Route;

public final class ConsoleEventReporter implements EventReporter {
    private final PrintStream output;

    public ConsoleEventReporter(PrintStream output) {
        this.output = output;
    }

    @Override
    public void routeStarted(int timeSeconds, Athlete athlete, Route route) {
        output.println(
                prefix(timeSeconds)
                        + "Athlete "
                        + athlete.getId()
                        + " started route "
                        + route.getId()
                        + "."
        );
    }

    @Override
    public void routeFinished(int timeSeconds, Athlete athlete, Route route) {
        output.println(
                prefix(timeSeconds)
                        + "Athlete "
                        + athlete.getId()
                        + " finished route "
                        + route.getId()
                        + "."
        );
    }

    @Override
    public void liftQueueJoined(int timeSeconds, Athlete athlete, Lift lift) {
        output.println(
                prefix(timeSeconds)
                        + "Athlete "
                        + athlete.getId()
                        + " joined the queue for lift "
                        + lift.getId()
                        + "."
        );
    }

    @Override
    public void liftStarted(int timeSeconds, Athlete athlete, Lift lift) {
        output.println(
                prefix(timeSeconds)
                        + "Athlete "
                        + athlete.getId()
                        + " started lift "
                        + lift.getId()
                        + "."
        );
    }

    @Override
    public void liftLeft(int timeSeconds, Athlete athlete, Lift lift) {
        output.println(
                prefix(timeSeconds)
                        + "Athlete "
                        + athlete.getId()
                        + " left lift "
                        + lift.getId()
                        + "."
        );
    }

    private String prefix(int timeSeconds) {
        return TimeFormatter.format(timeSeconds) + ": ";
    }
}
