package skiresort.output;

public final class TimeFormatter {
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_HOUR =
            SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    private TimeFormatter() {
    }

    public static String format(int totalSeconds) {
        int hours = totalSeconds / SECONDS_PER_HOUR;
        int minutes = (totalSeconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE;
        int seconds = totalSeconds % SECONDS_PER_MINUTE;
        return twoDigits(hours)
                + ":"
                + twoDigits(minutes)
                + ":"
                + twoDigits(seconds);
    }

    public static int parse(String value) {
        int hours = parseTwoDigits(value, 0);
        int minutes = parseTwoDigits(value, 3);
        int seconds = parseTwoDigits(value, 6);
        return hours * SECONDS_PER_HOUR + minutes * SECONDS_PER_MINUTE + seconds;
    }

    private static String twoDigits(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return Integer.toString(value);
    }

    private static int parseTwoDigits(String value, int offset) {
        return 10 * (value.charAt(offset) - '0')
                + (value.charAt(offset + 1) - '0');
    }
}
