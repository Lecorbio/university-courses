package skiresort.output;

import skiresort.model.Athlete;
import skiresort.model.Lift;
import skiresort.model.Route;

public final class DiscardEventReporter implements EventReporter {
    public static final DiscardEventReporter INSTANCE =
            new DiscardEventReporter();

    private DiscardEventReporter() {
    }

    @Override
    public void routeStarted(int timeSeconds, Athlete athlete, Route route) {
    }

    @Override
    public void routeFinished(int timeSeconds, Athlete athlete, Route route) {
    }

    @Override
    public void liftQueueJoined(int timeSeconds, Athlete athlete, Lift lift) {
    }

    @Override
    public void liftStarted(int timeSeconds, Athlete athlete, Lift lift) {
    }

    @Override
    public void liftLeft(int timeSeconds, Athlete athlete, Lift lift) {
    }
}
