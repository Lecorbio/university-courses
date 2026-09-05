package skiresort.model;

public final class ResortMap {
    private final Node[] nodes;
    private final Lift[] lifts;
    private final Route[] routes;

    public ResortMap(Node[] nodes, Lift[] lifts, Route[] routes) {
        this.nodes = nodes;
        this.lifts = lifts;
        this.routes = routes;
    }

    public int getNodeCount() {
        return nodes.length;
    }

    public Node getNode(int id) {
        return nodes[id];
    }

    public int getLiftCount() {
        return lifts.length;
    }

    public Lift getLift(int id) {
        return lifts[id];
    }

    public int getRouteCount() {
        return routes.length;
    }

    public Route getRoute(int id) {
        return routes[id];
    }
}
