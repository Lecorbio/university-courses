#include <algorithm>
#include <deque>
#include <iostream>
#include <vector>

// Uciszamy -pedantic przez __extension__.
__extension__ typedef __int128 int128;

struct Segment {
    int left, right;
};

int main() {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    int n = 0, U = 0;
    std::cin >> n >> U;

    std::vector<int> x(n), y(n);
    for (int i = 0; i < n; ++i) {
        std::cin >> x[i] >> y[i];
    }

    // Porownanie jakosci bez pierwiastka: (dx^2 / len).
    auto isBetter = [&](Segment& a, Segment& b) -> bool {
        long long dxA = x[a.right] - x[a.left];
        long long dxB = x[b.right] - x[b.left];
        long long lenA = a.right - a.left + 1;
        long long lenB = b.right - b.left + 1;

        int128 lhs = static_cast<int128>(dxA) * dxA * lenB;
        int128 rhs = static_cast<int128>(dxB) * dxB * lenA;
        if (lhs != rhs) {
            return lhs > rhs;
        }
        return a.left < b.left;
    };

    // Kolejki monotoniczne do utrzymania min/max w aktualnym oknie.
    std::deque<std::pair<int, int>> min_queue, max_queue;
    // Kandydaci: maksymalne U-scisle przedzialy z lewej strony okna.
    std::deque<Segment> best_segments;

    auto pushMin = [&](int value, int index) {
        while (!min_queue.empty() && min_queue.back().first > value) {
            min_queue.pop_back();
        }
        min_queue.push_back({value, index});
    };

    auto pushMax = [&](int value, int index) {
        while (!max_queue.empty() && max_queue.back().first < value) {
            max_queue.pop_back();
        }
        max_queue.push_back({value, index});
    };

    auto popExpiredMinMax = [&](int left) {
        while (!min_queue.empty() && min_queue.front().second < left) {
            min_queue.pop_front();
        }
        while (!max_queue.empty() && max_queue.front().second < left) {
            max_queue.pop_front();
        }
    };

    auto pushSegment = [&](Segment seg) {
        while (!best_segments.empty() && isBetter(seg, best_segments.back())) {
            best_segments.pop_back();
        }
        best_segments.push_back(seg);
    };

    auto popExpiredSegments = [&](int index) {
        while (!best_segments.empty() && best_segments.front().right < index) {
            best_segments.pop_front();
        }
    };

    std::vector<Segment> answer(n);
    int left = 0, right = -1;

    // Dwa wskazniki; kazdy dodany segment jest maksymalny U-scisly.
    while (right + 1 < n) {
        ++right;
        pushMin(y[right], right);
        pushMax(y[right], right);

        while (!min_queue.empty() && !max_queue.empty() &&
               max_queue.front().first - min_queue.front().first > U) {
            popExpiredSegments(left);
            answer[left] = best_segments.front();
            ++left;
            popExpiredMinMax(left);
        }

        while (right + 1 < n) {
            int nextValue = y[right + 1];
            int newMin = std::min(min_queue.front().first, nextValue);
            int newMax = std::max(max_queue.front().first, nextValue);
            if (newMax - newMin > U) {
                break;
            }
            ++right;
            pushMin(nextValue, right);
            pushMax(nextValue, right);
        }

        pushSegment({left, right});
        popExpiredSegments(left);
    }

    // Uzupelnienie odpowiedzi dla pozostalych indeksow.
    for (int i = left; i < n; ++i) {
        popExpiredSegments(i);
        answer[i] = best_segments.front();
    }

    for (auto seg : answer) {
        std::cout << seg.left + 1 << ' ' << seg.right + 1 << '\n';
    }
    return 0;
}
