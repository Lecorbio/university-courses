# Large assignment 1: Ski resort simulator

A discrete-event simulation of athletes moving through ski routes and lifts. It models lift queues, route choice, changing route conditions, event reporting, and usage statistics.

[All homework](../README.md) · [Part 2](../duze-zadanie-2/) · [Original task PDF](po2526-osrodek-narciarski-1.pdf)

## Build and run

Requires JDK 21 or newer. Run these commands from this directory:

```sh
javac --release 21 -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -ea -cp out skiresort.app.Main 123 < przykladowe-dane.txt
```

The optional argument is the random seed. Omit `123` to use a new random seed on each run. Input is read from standard input; events and final statistics are printed to standard output.

## Tests

The existing test suite checks event ordering, queues, route attractiveness, input parsing, and simulation behavior.

```sh
javac --release 21 -encoding UTF-8 -d out-test $(find src/main/java src/test/java -name "*.java")
java -ea -cp out-test skiresort.tests.AutomatedTests
```

## Files

- `src/main/java/`: simulation, model, collections, input parsing, and output.
- `src/test/java/`: automated checks.
- [przykladowe-dane.txt](przykladowe-dane.txt): sample input.

## Task description

The original Polish assignment is included as the authored, nine-page [Ośrodek Narciarski – część 1 PDF](po2526-osrodek-narciarski-1.pdf), downloaded from the [Moodle assignment page](https://moodle.mimuw.edu.pl/mod/assign/view.php?id=177398). It defines the simulation model, input format, required output, and project requirements.
