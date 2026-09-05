# Homework 8: Polynomials and unit tests

A polynomial class with addition, Horner evaluation, normalized coefficients, equality, and a readable string representation. The accompanying JUnit tests exercise its behavior.

[All homework](../README.md)

## Build and run

Requires JDK 21 or newer and Maven. Run from this directory:

```sh
mvn test
```

Maven downloads the declared test dependencies on the first run. The homework is a library with unit tests, so it has no `main` entry point.

## Files

- [Polynomial.java](src/main/java/Polynomial.java): implementation.
- [PolynomialTest.java](src/test/java/PolynomialTest.java): homework tests.
- [pom.xml](pom.xml): reproducible compiler and test configuration.

## Task description

[Read the original Polish assignment statement](TASK.md), recovered from the course's Moodle page. The statement links to the accompanying JUnit scenario; the final polynomial implementation and tests are included here.
