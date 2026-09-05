# AKSO — Architektura komputerów i systemy operacyjne

**Computer Architecture and Operating Systems** · University of Warsaw · 2025/26

Three homework assignments covering dynamic libraries in C, multiprecision arithmetic, and Linux programming in x86-64 assembly. Each task contains one selected implementation, its build instructions, and the original task specification in Polish.

| # | Homework | Language | Focus |
| --- | --- | --- | --- |
| 01 | [Recursive stacks](zadanie-1/) | C23 | Shared references, cycles, memory management, file I/O |
| 02 | [Arithmetic sequence](zadanie-2/) | NASM x86-64 | Multiprecision signed arithmetic, C ABI |
| 03 | [Discrete fractals](zadanie-3/) | NASM x86-64 | String rewriting, Linux system calls, buffered I/O |

## Build and run

Use Linux with GNU Make and GCC supporting C23. The assembly tasks additionally require an **x86-64** system, NASM, and GNU binutils. They use the System V AMD64 ABI and Linux ELF binaries; macOS is not a native execution target.

```sh
make -C zadanie-1
make -C zadanie-1 check
make -C zadanie-2 check
make -C zadanie-3
printf 'A\nAAB\nBA\n' | zadanie-3/discrete_fractal 4
```

The optional C++ example in homework 02 also needs a C++23 standard library with `<print>`, Boost headers, and GMP. See the individual task pages for details.

## About the files

Assignment-provided headers, example programs, and fixtures accompany the implementations where available. The Markdown task descriptions were transcribed from saved course pages; website navigation and submission-status information were omitted.

[Back to all courses](../README.md)
