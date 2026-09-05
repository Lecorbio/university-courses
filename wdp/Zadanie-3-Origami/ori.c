#include <stdio.h>
#include <stdlib.h>

#define EPS 1e-8

typedef enum {
	RECTANGLE,
	CIRCLE,
	FOLD,
} Type;

typedef struct {
	double x, y;
} Point;

/* Struktura przechowywujaca dane o kartach.
 * (P) [RECTANGLE, p1, p2]
 * (K) [CIRCLE, p1, radius]
 * (Z) [FOLD, prev_sheet_idx, p1, p2] */
typedef struct {
	Type type;
	Point p1, p2;
	double radius;
	int prev_sheet_idx; /* indeks kartki, ktora zginamy */
} Sheet;

static Sheet *sheets = NULL;

/* Podniesie liczby do kwadratu, iloczyn wektorowy. */
static double sq(double a) {return a * a;}
static double cross_product(Point a, Point b, Point p){
	return (b.x - a.x) * (p.y - a.y) - (b.y - a.y) * (p.x - a.x);
}

/* Odbij punkt p wzgledem prostej ab, metoda wektorowa. */
static Point reflect(Point p, Point a, Point b) {
	double dx = b.x - a.x, dy = b.y - a.y;
	double t = ((p.x - a.x) * dx + (p.y - a.y) * dy) / (sq(dx) + sq(dy));
	Point s = {.x = a.x + t * dx, .y = a.y + t * dy};
	Point r = {.x = 2.0 * s.x - p.x, .y = 2.0 * s.y - p.y};
	return r;
}

/* Rekurencyjne zliczanie wartsw. */
static int count_layers(int sheet_idx, Point p){
	const Sheet *s = &sheets[sheet_idx];
	if (s->type == RECTANGLE) {
		/* Sprawdzamy czy punkt znajduje sie wewnatrz prostokata. */
		if (p.x >= s->p1.x - EPS && p.x <= s->p2.x + EPS &&
			p.y >= s->p1.y - EPS && p.y <= s->p2.y + EPS) return 1;
	} 
	else if (s->type == CIRCLE) {
		/* Sprawdzamy czy punkt znajduje sie wewnatrz okregu. */
		double d = sq(p.x - s->p1.x) + sq(p.y - s->p1.y);
		if (d <= sq(s->radius) + EPS) return 1;
	} 
	else if (s->type == FOLD) {
		double cp = cross_product(s->p1, s->p2, p);
		if (cp < 0 - EPS) { /* Prawa strona zgiecia, papier zostal zabrany. */ 
			return 0;
		}
		else if (cp > 0 + EPS) { /* Lewa strona zgiecia, mamy to co bylo + dochodzi papier z prawej. */
			Point r = reflect(p, s->p1, s->p2);
			return count_layers(s->prev_sheet_idx, p) + count_layers(s->prev_sheet_idx, r);
		}
		else { /* Jestesmy na linii zgiecia, mamy to co bylo. */
			return count_layers(s->prev_sheet_idx, p);
		}
	}
	return 0;
}

int main(){
	int n, q;
	if (scanf("%d %d", &n, &q) != 2) return 1;
	sheets = malloc((size_t)n * sizeof(Sheet));
	
	/* Wczytanie kartek. */
	for (int i = 0; i < n; i++) {
		char type;
		if (scanf(" %c", &type) != 1) return 1;
		
		if (type == 'P') {
			sheets[i].type = RECTANGLE;
			if (scanf("%lf %lf %lf %lf",
				&sheets[i].p1.x, &sheets[i].p1.y,
				&sheets[i].p2.x, &sheets[i].p2.y) != 4) return 1;
		} else if (type == 'K') {
			sheets[i].type = CIRCLE;
			if (scanf("%lf %lf %lf",
				&sheets[i].p1.x, &sheets[i].p1.y,
				&sheets[i].radius) != 3) return 1;
		} else if (type == 'Z'){
			sheets[i].type = FOLD;
			if (scanf("%d %lf %lf %lf %lf",
				&sheets[i].prev_sheet_idx,
				&sheets[i].p1.x, &sheets[i].p1.y,
				&sheets[i].p2.x, &sheets[i].p2.y) != 5) return 1;
			sheets[i].prev_sheet_idx--;
		}
	}
	
	/* Obsluga zapytan. */
	for (int i = 0; i < q; i++) {
		int k; Point p;
		if (scanf("%d %lf %lf", &k, &p.x, &p.y) != 3) return 1;
		printf("%d\n", count_layers(k - 1, p));
	}
	free(sheets);
	return 0;
}
/*

4 5
P 0 0 2.5 1
Z 1 0.5 0 0.5 1
K 0 1 5
Z 3 0 1 -1 0
1 1.5 0.5
2 1.5 0.5
2 0 0.5
3 4 4
4 4 4

toster --io origami-tests-master/manual ori.e
toster --io origami-tests-master/random/male ori.e
toster --io origami-tests-master/random/srednie ori.e
toster --io origami-tests-master/random/duze ori.e
toster --io origami-tests-master/random/float_test ori.e

*/
