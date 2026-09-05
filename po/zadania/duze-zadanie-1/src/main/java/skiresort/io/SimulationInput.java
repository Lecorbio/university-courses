package skiresort.io;

import skiresort.model.Athlete;
import skiresort.model.ResortMap;

public final class SimulationInput {
    private final ResortMap resortMap;
    private final Athlete[] athletes;

    public SimulationInput(ResortMap resortMap, Athlete[] athletes) {
        this.resortMap = resortMap;
        this.athletes = athletes;
    }

    public ResortMap getResortMap() {
        return resortMap;
    }

    public Athlete[] getAthletes() {
        return athletes;
    }
}
