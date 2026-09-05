# Java and C++ performance comparison

A comparison of Dijkstra’s shortest-path algorithm in Java and C++, using the same generated weighted graph and source vertices. Both implementations report a checksum and measure algorithm execution time after reading the graph.

[All homework](../README.md)

## Build and run

Requires JDK 21 or newer and a C++17 compiler. Run from this directory:

```sh
mkdir -p out
g++ -O3 -std=c++17 generate_graph.cpp -o out/generate_graph
g++ -O3 -std=c++17 dijkstra_benchmark.cpp -o out/dijkstra_benchmark
javac --release 21 -encoding UTF-8 -d out DijkstraBenchmark.java

./out/dijkstra_benchmark graph-small.txt
java -cp out DijkstraBenchmark graph-small.txt
```

The included small graph is a quick runnable example, not a representative performance workload. Check that the two programs print the same `checksum` before comparing timings.

## Generate a workload

```sh
./out/generate_graph out/graph.txt 50000 300000 20 123456789
./out/dijkstra_benchmark out/graph.txt
java -cp out DijkstraBenchmark out/graph.txt
```

Generator arguments are the output filename, vertex count, undirected edge count, source count, and random seed. With no arguments it uses `graph.txt 50000 300000 20 123456789`.

A larger workload uses:

```sh
./out/generate_graph out/graph-large.txt 100000 600000 20 123456789
```

Generate one file and give it to both programs. Identical seeds are useful for repeatability within a compiler environment; standard-library random-distribution implementations can differ across C++ environments.

## JVM execution modes

The original experiment also compared the default JVM with forced compilation and interpreted execution:

```sh
java -Xcomp -Xdiag -cp out DijkstraBenchmark out/graph.txt
java -Xint -Xdiag -cp out DijkstraBenchmark out/graph.txt
```

Each run reports graph parameters, the input filename, `checksum`, and `elapsed_ms`. These are individual timed runs; the program does not implement a warm-up or statistical benchmarking framework. Record the operating system, compiler/JDK versions, JVM mode, and input parameters when reporting results. No measured results are included in the available materials.

## Task description

Read the complete original Polish [task description](TASK.md), recovered from the [Moodle assignment forum](https://moodle.mimuw.edu.pl/mod/forum/view.php?id=170644). It specifies the comparison, JVM modes, measurements, and information required to reproduce the experiment.
