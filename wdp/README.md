# WDP — Wstęp do programowania

Six individual C and C++ homework assignments from **Wstęp do programowania z \*** at the **University of Warsaw**, winter semester **2025/26**.

The collection covers set operations, sliding windows, computational geometry, linked structures, and graph search. Each assignment includes its implementation, build instructions, and the original Polish task description.

## Assignments

| # | Assignment | Topic | Language |
| --- | --- | --- | --- |
| 01 | [Zbiory arytmetyczne](Zadanie-1-Zbiory-arytmetyczne/) | Set representation, union, intersection, and difference | C17 |
| 02 | [Trzy różne](Zadanie-2-Trzy-różne/) | Finding closest and farthest triples along a highway | C17 |
| 03 | [Origami](Zadanie-3-Origami/) | Geometric reflection and recursive layer counting | C17 |
| 04 | [Worki](Zadanie-4-Worki/) | Linked structures and dynamic container operations | C++23 |
| 05 | [Ścisłe przedziały](Zadanie-5-Ścisłe-przedziały/) | Sliding windows, monotonic queues, and interval selection | C++23 |
| 06 | [Przelewanka](Zadanie-6-Przelewanka/) | Breadth-first search over states and reachability checks | C++23 |

## Build and run

Requirements: **Make**, a **C17 compiler**, and a **C++23 compiler**. GCC and Clang are supported; assignment 5 also uses their `__int128` extension.

From this directory:

```sh
make                 # Build all six assignments into build/
make task-3          # Or build one assignment
./build/ori < input.txt
make clean           # Remove generated build files
```

The input file must follow the format in the corresponding task description. Assignments 2, 3, 5, and 6 read standard input and write standard output. Assignment 1 is a library with an included assertion-based example (`build/ary`); assignment 4 builds a library object (`build/worki.o`) to link with a caller.

To choose compilers explicitly:

```sh
make CC=gcc CXX=g++
```

On macOS, `gcc` and `g++` commonly invoke Apple Clang. The Makefile uses portable warning options; the preserved `opcje` and `opcjeCpp` files contain the original **GCC-specific** course flags and are used by the compilation commands in the Polish statements.

## Contents

Each assignment directory contains one implementation and the files needed to build it. The English introduction provides navigation and usage; the Polish task description preserves the coursework specification. Generated binaries and local metadata are ignored by Git.

The original `main.cpp` example attachment for **Worki** is currently missing. Its full task description, interface, and implementation are included.
