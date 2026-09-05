#include <chrono>
#include <cstdint>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <queue>
#include <utility>
#include <vector>
using namespace std;

const int64_t INF = 4'000'000'000'000'000'000LL;

int n, m, k;
uint32_t seed;

vector<vector<pair<int, int>>> g;
vector<int> sources;

void add_edge(int u, int v, int w) {
    g[u].push_back({v, w});
    g[v].push_back({u, w});
}

bool read_graph(const string &file_name) {
    ifstream fin(file_name);
    if (!fin) return false;

    fin >> n >> m >> k >> seed;
    if (!fin || n < 2 || m < n - 1 || k <= 0) return false;

    g.assign(n, {});
    sources.resize(k);

    for (int i = 0; i < k; i++) fin >> sources[i];
    for (int i = 0; i < m; i++) {
        int u, v, w;
        fin >> u >> v >> w;
        add_edge(u, v, w);
    }

    return (bool) fin;
}

int64_t dijkstra(int s) {
    vector<int64_t> dist(n, INF);
    priority_queue<pair<int64_t, int>, vector<pair<int64_t, int>>, greater<pair<int64_t, int>>> pq;

    dist[s] = 0;
    pq.push({0, s});

    while (!pq.empty()) {
        auto [du, u] = pq.top();
        pq.pop();

        if (du != dist[u]) continue;

        for (auto [v, w] : g[u]) {
            if (dist[v] > du + w) {
                dist[v] = du + w;
                pq.push({dist[v], v});
            }
        }
    }

    int64_t sum = 0;
    for (int64_t x : dist) sum += x;
    return sum;
}

int main(int argc, char **argv) {
    string file_name = "graph.txt";
    if (argc >= 2) file_name = argv[1];

    if (!read_graph(file_name)) {
        cout << "Bad input file\n";
        return 0;
    }

    auto start = chrono::steady_clock::now();

    int64_t checksum = 0;
    for (int s : sources) checksum += dijkstra(s);

    auto finish = chrono::steady_clock::now();
    double elapsed_ms = chrono::duration<double, milli>(finish - start).count();

    cout << "algorithm=dijkstra\n";
    cout << "graph=undirected_sparse\n";
    cout << "input_file=" << file_name << '\n';
    cout << "vertices=" << n << '\n';
    cout << "undirected_edges=" << m << '\n';
    cout << "sources=" << k << '\n';
    cout << "seed=" << seed << '\n';
    cout << "checksum=" << checksum << '\n';
    cout << fixed << setprecision(3);
    cout << "elapsed_ms=" << elapsed_ms << '\n';

    return 0;
}
