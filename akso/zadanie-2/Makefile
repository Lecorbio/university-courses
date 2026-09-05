CC = gcc
CXX = g++
NASM = nasm
CFLAGS = -Wall -Wextra -std=c23 -O2
CXXFLAGS = -Wall -Wextra -std=c++23 -O2
NASMFLAGS = -f elf64 -w+all -w+error

.PHONY: all check cpp clean

all: arithmetic_sequence_example_c

arithmetic_sequence.o: arithmetic_sequence.asm
	$(NASM) $(NASMFLAGS) -o $@ $<

arithmetic_sequence_example_c.o: arithmetic_sequence_example.c test_data.c
	$(CC) $(CFLAGS) -c -o $@ $<

arithmetic_sequence_example_c: arithmetic_sequence_example_c.o arithmetic_sequence.o
	$(CC) -z noexecstack -o $@ $^

check: arithmetic_sequence_example_c
	./arithmetic_sequence_example_c

cpp: arithmetic_sequence_example_cpp

arithmetic_sequence_example_cpp.o: arithmetic_sequence_example.cpp
	$(CXX) $(CXXFLAGS) -c -o $@ $<

arithmetic_sequence_example_cpp: arithmetic_sequence_example_cpp.o arithmetic_sequence.o
	$(CXX) -z noexecstack -o $@ $^ -lgmp

clean:
	rm -f arithmetic_sequence.o arithmetic_sequence_example_c.o arithmetic_sequence_example_cpp.o
	rm -f arithmetic_sequence_example_c arithmetic_sequence_example_cpp
