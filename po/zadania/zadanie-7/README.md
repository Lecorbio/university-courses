# Homework 7: Reverse Polish notation calculator

A stack-based calculator for whitespace-separated arithmetic expressions in reverse Polish notation.

[All homework](../README.md)

## Build and run

Requires JDK 21 or newer. Run these commands from this directory:

```sh
javac --release 21 -encoding UTF-8 -d out $(find src -name "*.java")
java -ea -cp out prezentacja.Main
```

Enter one expression per line, for example `3 4 + 2 *` (result: `14`). A blank line or end of input exits the program. Invalid expressions are reported without ending the session.

## Task description

The original Polish assignment statement is missing. The summary above describes the available implementation; it does not replace the assignment requirements.
