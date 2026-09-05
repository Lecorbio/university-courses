package skiresort.model;

public enum AthleteKind {
    LOCAL,
    GREEDY,
    COLLECTOR;

    public static AthleteKind fromToken(String token) {
        return switch (token) {
            case "L" -> LOCAL;
            case "Z" -> GREEDY;
            case "K" -> COLLECTOR;
            default -> throw new IllegalArgumentException(
                    "Unknown athlete kind: " + token
            );
        };
    }
}
