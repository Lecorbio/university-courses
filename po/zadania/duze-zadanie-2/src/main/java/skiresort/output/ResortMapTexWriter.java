package skiresort.output;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import kadra.mapki.styl.GruboscKonturu;
import kadra.mapki.styl.StylKrawedzi;
import kadra.mapki.styl.StylLinii;
import kadra.mapki.styl.StylWezla;
import skiresort.model.Athlete;
import skiresort.model.Lift;
import skiresort.model.Node;
import skiresort.model.ResortMap;
import skiresort.model.Route;
import skiresort.simulation.GroomingCriterion;

public final class ResortMapTexWriter {
    private final GeneratorMapek generatorMapek;

    public ResortMapTexWriter(String outputDirectory)
            throws WyjatekSystemuPlikow {
        generatorMapek = new GeneratorMapek(outputDirectory);
    }

    public void writeAll(ResortMap resortMap, Athlete[] athletes)
            throws WyjatekSystemuPlikow {
        writeParameterMap(resortMap);
        writeStatisticsMap(resortMap);
        for (Athlete athlete : athletes) {
            if (athlete.isTracked()) {
                writeAthleteHistoryMap(resortMap, athlete);
            }
        }
    }

    private void writeParameterMap(ResortMap resortMap)
            throws WyjatekSystemuPlikow {
        startMap(resortMap);
        addRouteEdges(resortMap, this::routeParameterLines);
        addLiftEdges(resortMap, this::liftParameterLines);
        generatorMapek.tworzMapke("parametry.tex");
    }

    private void writeStatisticsMap(ResortMap resortMap)
            throws WyjatekSystemuPlikow {
        startMap(resortMap);
        addRouteEdges(resortMap, this::routeStatisticLines);
        addLiftEdges(resortMap, this::liftStatisticLines);
        generatorMapek.tworzMapke("statystyki.tex");
    }

    private void writeAthleteHistoryMap(ResortMap resortMap, Athlete athlete)
            throws WyjatekSystemuPlikow {
        startMap(resortMap);
        for (int id = 0; id < resortMap.getRouteCount(); id++) {
            Route route = resortMap.getRoute(id);
            generatorMapek.dodajKrawedz(
                    route.getStartNode().getId(),
                    route.getEndNode().getId(),
                    routeStyle(),
                    historyText("t", id, athlete.getRouteTraversalNumbers(id))
            );
        }
        for (int id = 0; id < resortMap.getLiftCount(); id++) {
            Lift lift = resortMap.getLift(id);
            generatorMapek.dodajKrawedz(
                    lift.getStartStation().getId(),
                    lift.getEndStation().getId(),
                    liftStyle(),
                    historyText("w", id, athlete.getLiftTraversalNumbers(id))
            );
        }
        generatorMapek.tworzMapke(
                "historia-sportowiec-" + athlete.getId() + ".tex"
        );
    }

    private void startMap(ResortMap resortMap) {
        generatorMapek.zeruj();
        for (int id = 0; id < resortMap.getNodeCount(); id++) {
            Node node = resortMap.getNode(id);
            GruboscKonturu outline = node.isConnectedToTransport()
                    ? GruboscKonturu.POGRUBIONY
                    : GruboscKonturu.ZWYKLY;
            generatorMapek.dodajWezel(
                    node.getId(),
                    node.getX(),
                    node.getY(),
                    new StylWezla(outline)
            );
        }
    }

    private void addRouteEdges(ResortMap resortMap, RouteTextFactory factory) {
        for (int id = 0; id < resortMap.getRouteCount(); id++) {
            Route route = resortMap.getRoute(id);
            generatorMapek.dodajKrawedz(
                    route.getStartNode().getId(),
                    route.getEndNode().getId(),
                    routeStyle(),
                    factory.lines(route)
            );
        }
    }

    private void addLiftEdges(ResortMap resortMap, LiftTextFactory factory) {
        for (int id = 0; id < resortMap.getLiftCount(); id++) {
            Lift lift = resortMap.getLift(id);
            generatorMapek.dodajKrawedz(
                    lift.getStartStation().getId(),
                    lift.getEndStation().getId(),
                    liftStyle(),
                    factory.lines(lift)
            );
        }
    }

    private List<String> routeParameterLines(Route route) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(
                "t%d: poziom: %d, czas: %ds".formatted(
                        route.getId(),
                        route.getDifficulty(),
                        route.getTravelTimeSeconds()
                )
        );
        lines.add(
                "odporność: %s, %s".formatted(
                        format2(route.getBaseAttractiveness()),
                        format5(route.getResistance())
                )
        );
        return lines;
    }

    private List<String> liftParameterLines(Lift lift) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(
                "w%d: %d os. co %ds".formatted(
                        lift.getId(),
                        lift.getCapacity(),
                        lift.getDispatchIntervalSeconds()
                )
        );
        lines.add("czas: %ds".formatted(lift.getTravelTimeSeconds()));
        return lines;
    }

    private List<String> routeStatisticLines(Route route) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(
                "t%d: śnieg: %s".formatted(
                        route.getId(),
                        format2(
                                GroomingCriterion.score(
                                        route.getBaseAttractiveness(),
                                        route.getResistance(),
                                        route.getRideCount()
                                )
                        )
                )
        );
        lines.add("zjazdy: %d".formatted(route.getRideCount()));
        return lines;
    }

    private List<String> liftStatisticLines(Lift lift) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(
                "w%d: kol: %.0f(śr), %d(maks)".formatted(
                        lift.getId(),
                        lift.getAverageQueueLength(),
                        lift.getMaximumQueueLength()
                )
        );
        lines.add(
                "wjazdy: %d / %d (%.0f%%)".formatted(
                        lift.getRideCount(),
                        lift.getMaximumPossibleRideCount(),
                        lift.getOccupancyPercent()
                )
        );
        return lines;
    }

    private static String historyText(
            String prefix,
            int edgeId,
            List<Integer> traversalNumbers
    ) {
        StringBuilder text = new StringBuilder();
        text.append(prefix)
                .append(edgeId)
                .append("(")
                .append(traversalNumbers.size())
                .append("):");
        if (!traversalNumbers.isEmpty()) {
            text.append(" ");
            for (int i = 0; i < traversalNumbers.size(); i++) {
                if (i > 0) {
                    text.append(",");
                }
                text.append(traversalNumbers.get(i));
            }
        }
        return text.toString();
    }

    private static StylKrawedzi routeStyle() {
        return new StylKrawedzi(StylLinii.CIAGLA);
    }

    private static StylKrawedzi liftStyle() {
        return new StylKrawedzi(StylLinii.PRZERYWANA);
    }

    private static String format2(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    private static String format5(double value) {
        return String.format(Locale.US, "%.5f", value);
    }

    private interface RouteTextFactory {
        List<String> lines(Route route);
    }

    private interface LiftTextFactory {
        List<String> lines(Lift lift);
    }
}
