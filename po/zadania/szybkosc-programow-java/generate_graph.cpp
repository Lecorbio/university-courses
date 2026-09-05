#include <cstdint>
#include <fstream>
#include <iostream>
#include <random>
#include <string>
using namespace std;

int n = 50000;
int m = 300000;
int k = 20;
uint32_t seed = 123456789U;
mt19937 rng;

int rnd(int x, int y) {
    return uniform_int_distribution<int>(x, y)(rng);
}

int main(int argc, char **argv) {
    string file_name = "graph.txt";
    if (argc >= 2) file_name = argv[1];
    if (argc >= 3) n = atoi(argv[2]);
    if (argc >= 4) m = atoi(argv[3]);
    if (argc >= 5) k = atoi(argv[4]);
    if (argc >= 6) seed = (uint32_t) strtoul(argv[5], nullptr, 10);

    if (n < 2 || m < n - 1 || k <= 0) {
        cout << "Bad parameters\n";
        return 0;
    }

    rng.seed(seed);
    ofstream fout(file_name);
    if (!fout) {
        cout << "Cannot open output file\n";
        return 0;
    }

    fout << n << ' ' << m << ' ' << k << ' ' << seed << '\n';

    for (int i = 0; i < k; i++) {
        if (i) fout << ' ';
        fout << rnd(0, n - 1);
    }
    fout << '\n';

    for (int i = 1; i < n; i++) {
        fout << i - 1 << ' ' << i << ' ' << rnd(1, 1000) << '\n';
    }

    for (int i = n - 1; i < m; i++) {
        int u = rnd(0, n - 1);
        int v = rnd(0, n - 2);
        if (v >= u) v++;
        fout << u << ' ' << v << ' ' << rnd(1, 1000) << '\n';
    }

    return 0;
}
