package skiresort.output;

import skiresort.model.Athlete;
import skiresort.model.Lift;
import skiresort.model.Route;

public interface EventReporter {
    void routeStarted(int timeSeconds, Athlete athlete, Route route);

    void routeFinished(int timeSeconds, Athlete athlete, Route route);

    void liftQueueJoined(int timeSeconds, Athlete athlete, Lift lift);

    void liftStarted(int timeSeconds, Athlete athlete, Lift lift);

    void liftLeft(int timeSeconds, Athlete athlete, Lift lift);
}
