CC ?= cc
CXX ?= c++
CFLAGS ?= -std=c17 -O2 -Wall -Wextra -pedantic
CXXFLAGS ?= -std=c++23 -O2 -Wall -Wextra -pedantic

BUILD := build
TASK1 := Zadanie-1-Zbiory-arytmetyczne
TASK2 := Zadanie-2-Trzy-różne
TASK3 := Zadanie-3-Origami
TASK4 := Zadanie-4-Worki
TASK5 := Zadanie-5-Ścisłe-przedziały
TASK6 := Zadanie-6-Przelewanka

.PHONY: all task-1 task-2 task-3 task-4 task-5 task-6 clean

all: task-1 task-2 task-3 task-4 task-5 task-6

task-1: $(BUILD)/ary
task-2: $(BUILD)/trz
task-3: $(BUILD)/ori
task-4: $(BUILD)/worki.o
task-5: $(BUILD)/scisle-przedzialy
task-6: $(BUILD)/przelewanka

$(BUILD):
	mkdir -p $@

$(BUILD)/ary: $(TASK1)/ocen.c $(TASK1)/zbior_ary.c $(TASK1)/zbior_ary.h | $(BUILD)
	$(CC) $(CPPFLAGS) $(CFLAGS) $(TASK1)/ocen.c $(TASK1)/zbior_ary.c $(LDFLAGS) -o $@ $(LDLIBS) -lm

$(BUILD)/trz: $(TASK2)/trz.c | $(BUILD)
	$(CC) $(CPPFLAGS) $(CFLAGS) $< $(LDFLAGS) -o $@ $(LDLIBS)

$(BUILD)/ori: $(TASK3)/ori.c | $(BUILD)
	$(CC) $(CPPFLAGS) $(CFLAGS) $< $(LDFLAGS) -o $@ $(LDLIBS) -lm

$(BUILD)/worki.o: $(TASK4)/worki.cpp $(TASK4)/worki.h | $(BUILD)
	$(CXX) $(CPPFLAGS) $(CXXFLAGS) -c $< -o $@

$(BUILD)/scisle-przedzialy: $(TASK5)/prz.cpp | $(BUILD)
	$(CXX) $(CPPFLAGS) $(CXXFLAGS) $< $(LDFLAGS) -o $@ $(LDLIBS) -lm

$(BUILD)/przelewanka: $(TASK6)/prz.cpp | $(BUILD)
	$(CXX) $(CPPFLAGS) $(CXXFLAGS) $< $(LDFLAGS) -o $@ $(LDLIBS)

clean:
	rm -rf $(BUILD)
