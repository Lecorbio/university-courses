package skiresort.model;

public final class ActivityChoice {
    public enum Kind {
        ROUTE,
        LIFT
    }

    private final Kind kind;
    private final Route route;
    private final Lift lift;

    private ActivityChoice(Route route) {
        if (route == null) {
            throw new IllegalArgumentException("Route cannot be null.");
        }
        kind = Kind.ROUTE;
        this.route = route;
        lift = null;
    }

    private ActivityChoice(Lift lift) {
        if (lift == null) {
            throw new IllegalArgumentException("Lift cannot be null.");
        }
        kind = Kind.LIFT;
        route = null;
        this.lift = lift;
    }

    public static ActivityChoice forRoute(Route route) {
        return new ActivityChoice(route);
    }

    public static ActivityChoice forLift(Lift lift) {
        return new ActivityChoice(lift);
    }

    public Kind getKind() {
        return kind;
    }

    public Route getRoute() {
        if (kind != Kind.ROUTE) {
            throw new IllegalStateException("Activity choice is not a route.");
        }
        return route;
    }

    public Lift getLift() {
        if (kind != Kind.LIFT) {
            throw new IllegalStateException("Activity choice is not a lift.");
        }
        return lift;
    }
}
