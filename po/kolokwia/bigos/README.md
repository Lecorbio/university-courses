# Colloquium 2022: BigOS

An in-memory model of files, directories, access permissions, and links, with tree traversal and deletion behavior.

[All colloquia](../README.md) · [Original task PDF](<../Klasówka - BigOS.pdf>)

## Build and run

Requires JDK 21 or newer. Run from this directory:

```sh
javac --release 21 -encoding UTF-8 -d out *.java
java -ea -cp out Main
```

The included `Main` runs assertions for paths, links, moves, and deletion, then prints a small directory tree.

## Task description

The original Polish statement is retained in the authored [task PDF](<../Klasówka - BigOS.pdf>).
