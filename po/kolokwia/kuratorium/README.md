# Colloquium 2023: School admissions

A school-admissions allocation procedure that manages ranked student preferences, class capacity, and reconsideration after a place is freed.

[All colloquia](../README.md) · [Original task PDF](<../Klasówka Kuratorium23.pdf>)

## Build and run

Requires JDK 21 or newer. Run from this directory:

```sh
javac --release 21 -encoding UTF-8 -d out *.java
java -ea -cp out Main
```

The included `Main` checks a reassignment scenario in which a student receives a preferred class and another student takes the newly available place.

## Task description

The original Polish statement is retained in the authored [task PDF](<../Klasówka Kuratorium23.pdf>).
