public class Polynomial {
    private final double[] coefficients;

    public Polynomial(double[] coefficients) {
        this.coefficients = normalise(coefficients);
    }

    public Polynomial add(Polynomial other) {
        double[] result = new double[Math.max(coefficients.length, other.coefficients.length)];

        for (int i = 0; i < coefficients.length; i++) {
            result[i] += coefficients[i];
        }

        for (int i = 0; i < other.coefficients.length; i++) {
            result[i] += other.coefficients[i];
        }

        return new Polynomial(result);
    }

    public double evaluate(double x) {
        double result = 0;

        for (int i = coefficients.length - 1; i >= 0; i--) {
            result = result * x + coefficients[i];
        }

        return result;
    }

    @Override
    public String toString() {
        if (isZero()) {
            return "0";
        }

        StringBuilder result = new StringBuilder();

        for (int degree = coefficients.length - 1; degree >= 0; degree--) {
            double coefficient = coefficients[degree];

            if (coefficient == 0) {
                continue;
            }

            appendSeparator(result, coefficient);
            appendTerm(result, Math.abs(coefficient), degree);
        }

        return result.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Polynomial polynomial)) {
            return false;
        }

        if (coefficients.length != polynomial.coefficients.length) {
            return false;
        }

        for (int i = 0; i < coefficients.length; i++) {
            if (coefficients[i] != polynomial.coefficients[i]) {
                return false;
            }
        }

        return true;
    }

    private boolean isZero() {
        return coefficients.length == 1 && coefficients[0] == 0;
    }

    private static double[] normalise(double[] source) {
        int lastNonZero = source.length - 1;

        while (lastNonZero >= 0 && source[lastNonZero] == 0) {
            lastNonZero--;
        }

        if (lastNonZero < 0) {
            return new double[]{0};
        }

        double[] result = new double[lastNonZero + 1];

        for (int i = 0; i < result.length; i++) {
            result[i] = source[i];
        }

        return result;
    }

    private static void appendSeparator(StringBuilder result, double coefficient) {
        if (result.length() == 0) {
            if (coefficient < 0) {
                result.append("-");
            }
            return;
        }

        result.append(coefficient < 0 ? " - " : " + ");
    }

    private static void appendTerm(StringBuilder result, double coefficient, int degree) {
        boolean shouldPrintCoefficient = degree == 0 || coefficient != 1;

        if (shouldPrintCoefficient) {
            result.append(format(coefficient));
        }

        if (degree > 0) {
            result.append("x");
        }

        if (degree > 1) {
            result.append("^").append(degree);
        }
    }

    private static String format(double value) {
        if (value == Math.rint(value)) {
            return Long.toString((long) value);
        }

        return Double.toString(value);
    }
}

