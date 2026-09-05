#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <limits.h>

/* Struktura zawierajaca infomacje o motelu z tresci zadania. */
typedef struct {
    int type;
    int pos;
} Motel;

/* Struktura zawierajaca dane 3 moteli roznych sieci w aktualnym przedziale gasienicy. */
typedef struct {
    struct {
        int type;
        int cnt;
    } slots[3];
    int distinct_cnt;
} Window;

/* Pomocnicze funkcje matematyczne. */
static int min(int a, int b) { return (a < b) ? a : b; }
static int max(int a, int b) { return (a > b) ? a : b; }

static int get_max_dist(const Motel *motels, int l, int mid, int r) {
    int d1 = motels[mid].pos - motels[l].pos, d2 = motels[r].pos - motels[mid].pos;
    return max(d1, d2);
}

static int get_min_dist(const Motel *motels, int l, int mid, int r) {
    int d1 = motels[mid].pos - motels[l].pos, d2 = motels[r].pos - motels[mid].pos;
    return min(d1, d2);
}

/* Funkcje od przesuwania gasienicy. */
static void window_init(Window *w) {
    w->distinct_cnt = 0;
    for (int i = 0; i < 3; i++) {
        w->slots[i].type = -1;
        w->slots[i].cnt = 0;
    }
}

static void window_add(Window *w, int type) {
    for (int i = 0; i < 3; i++) {
        if (w->slots[i].type == type) {
            w->slots[i].cnt++;
            return;
        }
    }
    for (int i = 0; i < 3; i++) {
        if (w->slots[i].cnt == 0) {
            w->slots[i].type = type;
            w->slots[i].cnt = 1;
            w->distinct_cnt++;
            return;
        }
    }
}

static void window_remove(Window *w, int type) {
    for (int i = 0; i < 3; i++) {
        if (w->slots[i].type == type) {
            w->slots[i].cnt--;
            if (w->slots[i].cnt == 0) {
                w->slots[i].type = -1;
                w->distinct_cnt--;
            }
            return;
        }
    }
}


/** Pierwsza czesc zadania - rozwiazanie metoda gasienicy. **/

/* Przesun srodek przedzialu gasienicy, dopoki jest to oplacalne. */
static bool should_advance_mid(const Motel *motels, int l, int mid, int r) {
    if (mid + 1 >= r) return false;
    if (mid <= l) return true;
    if (motels[mid].type == motels[l].type || motels[mid].type == motels[r].type) return true;
    return get_max_dist(motels, l, mid + 1, r) <= get_max_dist(motels, l, mid, r);
}

static int solve_closest(int n, const Motel *motels) {
    if (n < 3) return 0;
    Window win;
    window_init(&win);

    int l = 0, r = -1, mid = 0, min_max_res = INT_MAX;
    bool found_any = false;
    while (r + 1 < n) {
		/* Przesuwaj lewy koniec, dopoki nie ma 3 roznych sieci w przedziale. */
        while (r + 1 < n && win.distinct_cnt < 3) {
			r++;
			window_add(&win, motels[r].type);
		}
		/* Przesuwaj prawy koniec, tak aby zostaly dokladnie 3 sieci 
		 * w maksymalnie malym przedziale. */
        bool moved = false;
        while (win.distinct_cnt >= 3) {
			window_remove(&win, motels[l].type);
			l++;
			if (!moved) moved = true;
		}
		if (moved) { 
			l--;
			window_add(&win, motels[l].type);
		}
		/* Zbierz wynik z potencjalnych oplacalnych srodkow przedzialu. */
		if (win.distinct_cnt >= 3) {
			while (should_advance_mid(motels, l, mid, r)) {
				mid++;
				if (motels[mid].type != motels[l].type && motels[mid].type != motels[r].type) {
					min_max_res = min(min_max_res, get_max_dist(motels, l, mid, r));
					found_any = true;
				}
			}
		}
		window_remove(&win, motels[l].type);
		l++;
    }
    return found_any ? min_max_res : 0;
}


/** Druga czesc zadania - sprawdz przedzialy o koncach w 3 najblizszych 
 *  i 3 najdalszych motelach roznych sieci. **/

static int find_candidates(int n, const Motel *motels, int *out_indices, bool reverse) {
    int cnt = 0, visited_types[3] = {-1, -1, -1};
    int s = reverse ? n - 1 : 0, e = reverse ? -1 : n, step = reverse ? -1 : 1;
    
    for (int i = s; i != e && cnt < 3; i += step) {
        bool seen = false;
        for (int j = 0; j < cnt; j++) {
            if (visited_types[j] == motels[i].type) {
                seen = true;
                break;
            }
        }
        if (!seen) {
            visited_types[cnt] = motels[i].type;
            out_indices[cnt] = i;
            cnt++;
        }
    }
    return cnt;
}

static int solve_farthest(int n, const Motel *motels) {
    int left_candidates[3], right_candidates[3];
    
    int l_cnt = find_candidates(n, motels, left_candidates, false);
    int r_cnt = find_candidates(n, motels, right_candidates, true);
    
    int max_min_res = 0;

    /* Sprawdz wszystkie pary kandydatow. */
    for (int i = 0; i < l_cnt; i++) {
        for (int j = 0; j < r_cnt; j++) {
            int l = left_candidates[i], r = right_candidates[j];
            if (l >= r) continue;
            if (motels[l].type == motels[r].type) continue;

            for (int k = l + 1; k < r; k++) {
                if (motels[k].type == motels[l].type || motels[k].type == motels[r].type) {
                    continue;
                }
                max_min_res = max(max_min_res, get_min_dist(motels, l, k, r));
            }
        }
    }
    return max_min_res;
}

int main() {
    int n;
    if (scanf("%d", &n) != 1) {
        fprintf(stderr, "Blad przy wczytywaniu n.\n");
        return 1;
    }
    Motel *motels = (Motel*)malloc((size_t)n * sizeof(Motel));
    for (int i = 0; i < n; i++) {
        if (scanf("%d %d", &motels[i].type, &motels[i].pos) != 2) {
            fprintf(stderr, "Blad przy wczytywaniu danych motelu.\n");
            free(motels);
            return 1;
        }
    }
    printf("%d %d\n", solve_closest(n, motels), solve_farthest(n, motels));
    free(motels);
    return 0;
}
/*

5
2 2
4 4
1 4
4 4
3 5

9
1 2
2 6
2 9
1 13
1 17
3 20
1 26
3 27
1 30

toster --io trzy-rozne-tests/tiny trz.e
toster --io trzy-rozne-tests/small trz.e
toster --io trzy-rozne-tests/medium trz.e
toster --io trzy-rozne-tests/big trz.e

gcc @opcje trz.c -o trz.e

*/
