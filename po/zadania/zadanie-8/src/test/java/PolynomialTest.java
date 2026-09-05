import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PolynomialTest {

    @Test
    void constructorShouldTrimLeadingZeroCoefficients() {
        Polynomial polynomial = new Polynomial(new double[]{1, 2, 3, 0, 0});

        assertEquals(new Polynomial(new double[]{1, 2, 3}), polynomial);
    }

    @Test
    void addShouldSumPolynomialsOfTheSameDegreeGreaterThanOne() {
        Polynomial first = new Polynomial(new double[]{1, 2, 3});
        Polynomial second = new Polynomial(new double[]{4, 5, 6});

        Polynomial sum = first.add(second);

        assertEquals(new Polynomial(new double[]{5, 7, 9}), sum);
    }

    @Test
    void addShouldSumPolynomialsOfDifferentDegreesGreaterThanOne() {
        Polynomial first = new Polynomial(new double[]{1, 2, 3, 4});
        Polynomial second = new Polynomial(new double[]{5, 6, 7});

        Polynomial sum = first.add(second);

        assertEquals(new Polynomial(new double[]{6, 8, 10, 4}), sum);
    }

    @Test
    void addShouldHandleConstantPolynomialAsLeftArgument() {
        Polynomial constant = new Polynomial(new double[]{5});
        Polynomial polynomial = new Polynomial(new double[]{1, 2, 3});

        Polynomial sum = constant.add(polynomial);

        assertEquals(new Polynomial(new double[]{6, 2, 3}), sum);
    }

    @Test
    void addShouldHandleConstantPolynomialAsRightArgument() {
        Polynomial polynomial = new Polynomial(new double[]{1, 2, 3});
        Polynomial constant = new Polynomial(new double[]{5});

        Polynomial sum = polynomial.add(constant);

        assertEquals(new Polynomial(new double[]{6, 2, 3}), sum);
    }

    @Test
    void addShouldHandleTwoConstantPolynomials() {
        Polynomial first = new Polynomial(new double[]{2});
        Polynomial second = new Polynomial(new double[]{5});

        Polynomial sum = first.add(second);

        assertEquals(new Polynomial(new double[]{7}), sum);
    }

    @Test
    void addShouldTrimLeadingZeroCoefficientsWhenHighestTermsCancel() {
        Polynomial first = new Polynomial(new double[]{1, 2, 3});
        Polynomial second = new Polynomial(new double[]{4, 5, -3});

        Polynomial sum = first.add(second);

        assertEquals(new Polynomial(new double[]{5, 7}), sum);
    }

    @Test
    void evaluateShouldCalculatePolynomialOfDegreeGreaterThanOne() {
        Polynomial polynomial = new Polynomial(new double[]{1, 2, 3});

        double value = polynomial.evaluate(2);

        assertEquals(17, value);
    }

    @Test
    void evaluateShouldCalculatePolynomialWithFractionalCoefficientsAtFractionalPoint() {
        Polynomial polynomial = new Polynomial(new double[]{0.5, -1.25, 2.5});

        double value = polynomial.evaluate(1.2);

        assertEquals(2.6, value, 0.000000001);
    }

    @Test
    void evaluateShouldHandleConstantPolynomial() {
        Polynomial polynomial = new Polynomial(new double[]{7});

        double value = polynomial.evaluate(100);

        assertEquals(7, value);
    }

    @Test
    void toStringShouldPrintRegularPolynomialOfDegreeGreaterThanOne() {
        Polynomial polynomial = new Polynomial(new double[]{1, 2, 3});

        assertEquals("3x^2 + 2x + 1", polynomial.toString());
    }

    @Test
    void toStringShouldSkipTermsWithZeroCoefficients() {
        Polynomial polynomial = new Polynomial(new double[]{1, 0, 3});

        assertEquals("3x^2 + 1", polynomial.toString());
    }

    @Test
    void toStringShouldSkipCoefficientsEqualToOneForNonConstantTerms() {
        Polynomial polynomial = new Polynomial(new double[]{1, 1, 1});

        assertEquals("x^2 + x + 1", polynomial.toString());
    }

    @Test
    void toStringShouldPrintZeroPolynomial() {
        Polynomial polynomial = new Polynomial(new double[]{0, 0, 0});

        assertEquals("0", polynomial.toString());
    }
}
