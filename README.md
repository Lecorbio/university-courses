# University coursework

Programming coursework from Computer Science studies at the **University of Warsaw**, academic year **2025/26**.

## Courses

| Course | Focus | Languages | Contents |
| --- | --- | --- | --- |
| [WDP · Wstęp do programowania](wdp/) | Algorithms, data structures, geometry and state-space search | C, C++ | Six programming assignments |
| [PO · Programowanie obiektowe](po/) | Object-oriented design, collections and simulation | Java | Homework, two larger projects and colloquium solutions |
| [AKSO · Architektura komputerów i systemy operacyjne](akso/) | Memory management, assembly and Linux system calls | C, x86-64 assembly | Three programming assignments |

Each course has an index linking to its assignments. Each assignment includes the selected solution and instructions for building or running it. Original task descriptions are in **Polish**; navigation and build instructions are in English. Documents originally authored as PDFs are linked from the relevant README.

## Running the code

Open an assignment's README for its commands and required tools. Assignments are independent: there is no single application to build at the repository root.

- **WDP:** a C17 or C++23 compiler and Make.
- **PO:** a Java Development Kit; individual assignments document any test dependencies.
- **AKSO:** Linux on x86-64, GCC with C23 support, NASM and Make. Assembly programs target the Linux ABI and do not run natively on macOS.

## Collection status

See [missing materials](docs/MISSING_MATERIALS.md) for original statements and attachments still needed to complete the collection.

Laboratory exercises, lecture notes, general exam preparation, earlier solution variants and generated files are excluded from Git. The PO colloquium solutions are included as a separate section of that course.
