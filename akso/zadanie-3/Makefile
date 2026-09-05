NASM = nasm
LD = ld
NASMFLAGS = -f elf64 -w+all -w+error -w+unknown-warning -w+reloc-rel
LDFLAGS = -pie -I /lib64/ld-linux-x86-64.so.2 --fatal-warnings

.PHONY: all clean

all: discrete_fractal

discrete_fractal.o: discrete_fractal.asm
	$(NASM) $(NASMFLAGS) -o $@ $<

discrete_fractal: discrete_fractal.o
	$(LD) $(LDFLAGS) -o $@ $<

clean:
	rm -f discrete_fractal.o discrete_fractal
