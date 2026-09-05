# Colloquium 2024: Elections

A D’Hondt seat-allocation implementation with electoral thresholds, registration-order tie breaking, and candidate notifications.

[All colloquia](../README.md) · [Original task PDF](<../2024 PO Kolokwium - Wybory.pdf>)

## Build and run

Requires JDK 21 or newer. Run from this directory:

```sh
javac --release 21 -encoding UTF-8 -d out *.java
java -ea -cp out Main
```

The included `Main` verifies the statement’s example, electoral thresholds, and tie breaking.

## Task description

The original Polish statement is retained in the authored [task PDF](<../2024 PO Kolokwium - Wybory.pdf>).

## Implementation notes

`Kandydat` stores vote counts and notification status, `KomitetWyborczy` holds candidates and final winners, and `KomisjaWyborcza` runs the allocation procedure. Collections are represented with arrays, as required by the statement. Quotients are compared by cross multiplication to avoid floating-point rounding; equal quotients preserve committee registration order.
