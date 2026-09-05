#include <algorithm>
#include <chrono>
#include <iostream>
#include <numeric>
#include <queue>
#include <unordered_set>
#include <vector>
using namespace std;

struct custom_hash {
    static unsigned long long splitmix64(unsigned long long x) {
        // http://xorshift.di.unimi.it/splitmix64.c
        x += 0x9e3779b97f4a7c15;
        x = (x ^ (x >> 30)) * 0xbf58476d1ce4e5b9;
        x = (x ^ (x >> 27)) * 0x94d049bb133111eb;
        return x ^ (x >> 31);
    }

    size_t operator()(const vector<int>& v) const {
        static const unsigned long long FIXED_RANDOM =
            (unsigned long long)(chrono::steady_clock::now().time_since_epoch().count());
        unsigned long long h = FIXED_RANDOM;
        for (int x : v) h = splitmix64(h ^ (unsigned long long)(x));
        return (size_t)h;
    }
};

bool all_zero(const vector<int>& v) {
    for (int x : v) if (x != 0) return false;
    return true;
}

bool all_full(const vector<int>& x, const vector<int>& y) {
    for (int i = 0; i < (int)x.size(); i++) {
        if (y[i] != x[i]) return false;
    }
    return true;
}

int full_steps(const vector<int>& x) {
    int steps = 0;
    for (int c : x) if (c > 0) steps++;
    return steps;
}

bool gcd_ok(const vector<int>& x, const vector<int>& y) {
    int g = 0;
    for (int i : x) g = gcd(g, i);
    if (g == 0) return all_zero(y);
    for (int i : y) if (i % g != 0) return false;
    return true;
}

bool has_zero_or_full(const vector<int>& x, const vector<int>& y) {
    for (int i = 0; i < (int)x.size(); i++) {
        if (y[i] == 0 || x[i] == y[i]) return true;
    }
    return false;
}

int bfs_min_steps(const vector<int>& x, const vector<int>& y) {
    int n = (int)x.size();
    unordered_set<vector<int>, custom_hash> visited;
    visited.reserve(1 << 20);
    visited.max_load_factor(0.7f);
    queue<pair<vector<int>, int>> q;

    vector<int> start(n, 0);
    visited.emplace(start);
    q.push({start, 0});

    while (!q.empty()) {
        int d = q.front().second;
        vector<int> cur = q.front().first;
        q.pop();

        if (cur == y) return d;

        auto push_state = [&](vector<int> nxt) {
            if (!visited.emplace(nxt).second) return;
            q.push({nxt, d + 1});
        };

        for (int i = 0; i < n; i++) {
            if (cur[i] == x[i]) continue;
            vector<int> nxt = cur;
            nxt[i] = x[i];
            push_state(nxt);
        }

        for (int i = 0; i < n; i++) {
            if (cur[i] == 0) continue;
            vector<int> nxt = cur;
            nxt[i] = 0;
            push_state(nxt);
        }

        for (int i = 0; i < n; i++) {
            if (cur[i] == 0) continue;
            for (int j = 0; j < n; j++) {
                if (i == j || cur[j] == x[j]) continue;
                int t = min(cur[i], x[j] - cur[j]);
                vector<int> nxt = cur;
                nxt[i] -= t;
                nxt[j] += t;
                push_state(nxt);
            }
        }
    }
    return -1;
}

int solve(const vector<int>& x, const vector<int>& y) {
    int n = (int)x.size();
    if (n == 0 || all_zero(y)) return 0;
    if (all_full(x, y)) return full_steps(x);
    if (!gcd_ok(x, y) || !has_zero_or_full(x, y)) return -1;
    return bfs_min_steps(x, y);
}

int main() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);

    int n; cin >> n;
    vector<int> x(n), y(n);
    for (int i = 0; i < n; i++) {
        cin >> x[i] >> y[i];
    }

    cout << solve(x, y) << '\n';
    return 0;
}
