CC = gcc
CFLAGS = -Wall -Wextra -Wno-implicit-fallthrough -std=gnu23 -fPIC -O2
WRAP_LDFLAGS = -Wl,--wrap=malloc -Wl,--wrap=calloc \
  -Wl,--wrap=realloc -Wl,--wrap=reallocarray -Wl,--wrap=free \
  -Wl,--wrap=strdup -Wl,--wrap=strndup
LDFLAGS = -shared $(WRAP_LDFLAGS)

RSTACK_HEADER = rstack.h
MEMORY_TESTS_C = memory_tests.c
MEMORY_TESTS_H = memory_tests.h
RSTACK_EXAMPLE_C = rstack_example.c

.PHONY: all clean example check

all: librstack.so

librstack.so: rstack.o memory_tests.o
	$(CC) $(LDFLAGS) -o $@ $^

rstack.o: rstack.c $(RSTACK_HEADER)
	$(CC) $(CFLAGS) -c $< -o $@

memory_tests.o: $(MEMORY_TESTS_C) $(MEMORY_TESTS_H)
	$(CC) $(CFLAGS) -c $< -o $@

example: rstack_example

rstack_example: $(RSTACK_EXAMPLE_C) rstack.o memory_tests.o $(RSTACK_HEADER) \
  $(MEMORY_TESTS_H)
	$(CC) $(CFLAGS) $(RSTACK_EXAMPLE_C) rstack.o memory_tests.o \
	  $(WRAP_LDFLAGS) -o $@

check: rstack_example
	sh run-examples.sh

clean:
	rm -f librstack.so rstack.o memory_tests.o rstack_example
