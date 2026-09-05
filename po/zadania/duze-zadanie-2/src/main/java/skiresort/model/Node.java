package skiresort.model;

public final class Node {
    private static final int INITIAL_OUTGOING_CAPACITY = 2;

    private final int id;
    private final int height;
    private final int x;
    private final int y;
    private final boolean connectedToTransport;

    private Route[] outgoingRoutes;
    private int outgoingRouteCount;
    private Lift[] outgoingLifts;
    private int outgoingLiftCount;

    public Node(
            int id,
            int height,
            int x,
            int y,
            boolean connectedToTransport
    ) {
        this.id = id;
        this.height = height;
        this.x = x;
        this.y = y;
        this.connectedToTransport = connectedToTransport;
        outgoingRoutes = new Route[INITIAL_OUTGOING_CAPACITY];
        outgoingLifts = new Lift[INITIAL_OUTGOING_CAPACITY];
    }

    public int getId() {
        return id;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isConnectedToTransport() {
        return connectedToTransport;
    }

    public void addOutgoingRoute(Route route) {
        if (outgoingRouteCount == outgoingRoutes.length) {
            Route[] expanded = new Route[outgoingRoutes.length * 2];
            System.arraycopy(
                    outgoingRoutes,
                    0,
                    expanded,
                    0,
                    outgoingRoutes.length
            );
            outgoingRoutes = expanded;
        }
        outgoingRoutes[outgoingRouteCount] = route;
        outgoingRouteCount++;
    }

    public void addOutgoingLift(Lift lift) {
        if (outgoingLiftCount == outgoingLifts.length) {
            Lift[] expanded = new Lift[outgoingLifts.length * 2];
            System.arraycopy(
                    outgoingLifts,
                    0,
                    expanded,
                    0,
                    outgoingLifts.length
            );
            outgoingLifts = expanded;
        }
        outgoingLifts[outgoingLiftCount] = lift;
        outgoingLiftCount++;
    }

    public int getOutgoingRouteCount() {
        return outgoingRouteCount;
    }

    public Route getOutgoingRoute(int index) {
        if (index < 0 || index >= outgoingRouteCount) {
            throw new IndexOutOfBoundsException(
                    "Route index out of bounds: " + index
            );
        }
        return outgoingRoutes[index];
    }

    public int getOutgoingLiftCount() {
        return outgoingLiftCount;
    }

    public Lift getOutgoingLift(int index) {
        if (index < 0 || index >= outgoingLiftCount) {
            throw new IndexOutOfBoundsException(
                    "Lift index out of bounds: " + index
            );
        }
        return outgoingLifts[index];
    }
}
