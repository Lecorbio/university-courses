package skiresort.simulation;

import skiresort.model.Athlete;
import skiresort.model.Route;

public interface RouteAttractivenessModel {
    double attractiveness(Route route, Athlete athlete);
}
