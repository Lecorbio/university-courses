#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <assert.h>
#include "zbior_ary.h"
typedef long long ll;

static int Q = 0;

///FUNKCJE POMOCNICZE
//Operacja modulo dzialajaca takze dla liczb ujemnych 
static int modulo(int x, int mod){
	int rem = x % mod;
	return rem >= 0 ? rem : rem + mod;
}

//Stworzenie pustego zbioru
static zbior_ary empty_set(){
	zbior_ary S; S.n = S.cap = 0; S.buckets = NULL;
	return S;
}
//Stworzenie pustego wiadra
static zbior_ary_bucket empty_bucket(){
	zbior_ary_bucket B; B.rem = B.len = B.cap = 0; B.segs = NULL;
	return B;
}
//Rezerwacja pamieci dla tablicy wiader
static void ensure_cap_buckets(zbior_ary *S, int need){
	if (S->cap >= need) return;
	int sz = S->cap > 0 ? S->cap : 1;
	while (sz < need) sz *= 2;
	S->buckets = (zbior_ary_bucket*)realloc(S->buckets, (size_t)sz * sizeof(zbior_ary_bucket));
	S->cap = sz;
}
//Rezerwacja pamieci dla tablicy segmentow
static void ensure_cap_segs(zbior_ary_bucket *B, int need){
	if (B->cap >= need) return;
	int sz = B->cap > 0 ? B->cap : 1;
	while (sz < need) sz *= 2;
	B->segs = (zbior_ary_seg*)realloc(B->segs, (size_t)sz * sizeof(zbior_ary_seg));
	B->cap = sz;
}

//Dodaj segment [l, r] do wiadra B, utrzymujac odpowiednia kolejnosc
//(posortowane po l oraz scalanie nachodzacych na siebie segmentow)
static void push_seg(zbior_ary_bucket *B, int l, int r){
	if (l > r) return;
	if (B->len > 0){
		zbior_ary_seg *lst = &B->segs[B->len - 1];
		if ((ll)lst->r + 1 >= (ll)l){
			if (r > lst->r) lst->r = r;
			return;
		}
	}
	ensure_cap_segs(B, B->len + 1);
	B->segs[B->len].l = l;
	B->segs[B->len].r = r;
	B->len++;
}

//lower_bound po reszcie 'r' w posortowanej talbicy wiader
//zwraca indeks jesli znaleziono pole, wpp zwraca (~indeks) gdzie trzeba dodac wiadro
static int lower_bound_bucket(zbior_ary *S, int rem){
	int lo = 0, hi = S->n;
	while (lo < hi){
		int mid = (lo + hi) / 2;
		if (S->buckets[mid].rem < rem) lo = mid + 1;
		else hi = mid;
	}
	if (lo < S->n && S->buckets[lo].rem == rem) return lo;
	return ~lo;
}

//Znajdz lub wstaw wiadro na dana reszte
static zbior_ary_bucket *insert_bucket(zbior_ary *S, int rem){
	//Tak jak ustalilismy, ten binsearch tutaj pewnie nie jest konieczny
	//ale trudno to poprawic w poprawkach na ostatnia chwile
	int idx = lower_bound_bucket(S, rem);
	if (idx >= 0) return &S->buckets[idx];
	idx = ~idx;
	ensure_cap_buckets(S, S->n + 1);
	//analogicznie z tym przepisywaniem
	for (int i = S->n; i > idx; i--) S->buckets[i] = S->buckets[i - 1];
	S->buckets[idx].rem = rem;
	S->buckets[idx].len = S->buckets[idx].cap = 0;
	S->buckets[idx].segs = NULL;
	S->n++;
	return &S->buckets[idx];
}

//Teoriomnogosciowa suma dwoch wiader (z zachowana kolejnoscia po l)
static void merge_union(zbior_ary_bucket *A, zbior_ary_bucket *B, zbior_ary_bucket *res){
	int i = 0, j = 0;
	while (i < A->len || j < B->len){
		zbior_ary_seg cur;
		if (j == B->len || (i < A->len && A->segs[i].l <= B->segs[j].l)){
			cur = A->segs[i++];
		}else{
			cur = B->segs[j++];
		}
		push_seg(res, cur.l, cur.r);
	}
}

//Czesc wspolna dwoch wiader (z zachowana kolejnoscia po l)
static void merge_intersection(zbior_ary_bucket *A, zbior_ary_bucket *B, zbior_ary_bucket *res){
	int i = 0, j = 0;
	while (i < A->len && j < B->len){
		int l = (A->segs[i].l > B->segs[j].l) ? A->segs[i].l : B->segs[j].l;
		int r = (A->segs[i].r < B->segs[j].r) ? A->segs[i].r : B->segs[j].r;
		if (l <= r) push_seg(res, l, r);
		if (A->segs[i].r < B->segs[j].r) i++; else j++;
	}
}

//Roznica dwoch wiader (z zachowana kolejnoscia po l)
static void merge_difference(zbior_ary_bucket *A, zbior_ary_bucket *B, zbior_ary_bucket *res){
	int i = 0, j = 0;
	while (i < A->len){
		int l = A->segs[i].l, r = A->segs[i].r;
		while (j < B->len && B->segs[j].r < l) j++;
		
		ll cur = l;
		while (j < B->len && B->segs[j].l <= r){
			if (cur < B->segs[j].l) push_seg(res, (int)cur, B->segs[j].l - 1);
			if ((ll)B->segs[j].r + 1 > cur) cur = (ll)B->segs[j].r + 1;
			if (B->segs[j].r > r) break; else j++;
		}
		if (cur <= r) push_seg(res, (int)cur, r);
		i++;
	}
}


///FUNKCJE WLASCIWE Z TRESCI ZADANIA
zbior_ary ciag_arytmetyczny(int a, int q, int b){
	if (Q == 0) Q = q;
	zbior_ary S = empty_set();
	ll rem = modulo(a, Q), l = ((ll)a - (ll)rem) / (ll)Q, r = ((ll)b - (ll)rem) / (ll)Q;
	zbior_ary_bucket *B = insert_bucket(&S, (int)rem);
	push_seg(B, (int)l, (int)r);
	return S;
}

zbior_ary singleton(int a){
	assert(Q > 0);
	return ciag_arytmetyczny(a, 0, a);
}

zbior_ary suma(zbior_ary A, zbior_ary B){
	zbior_ary C = empty_set();
	int i = 0, j = 0;
	while (i < A.n || j < B.n){
		if (j == B.n || (i < A.n && A.buckets[i].rem < B.buckets[j].rem)){
			zbior_ary_bucket *res = insert_bucket(&C, A.buckets[i].rem);
			for (int k = 0; k < A.buckets[i].len; k++){
				push_seg(res, A.buckets[i].segs[k].l, A.buckets[i].segs[k].r);
			}
			i++;
		}else if (i == A.n || B.buckets[j].rem < A.buckets[i].rem){
			zbior_ary_bucket *res = insert_bucket(&C, B.buckets[j].rem);
			for (int k = 0; k < B.buckets[j].len; k++){
				push_seg(res, B.buckets[j].segs[k].l, B.buckets[j].segs[k].r);
			}
			j++;
		}else{
			zbior_ary_bucket *res = insert_bucket(&C, A.buckets[i].rem);
			merge_union(&A.buckets[i], &B.buckets[j], res);
			i++; j++;
		}
	}
	return C;
}

zbior_ary iloczyn(zbior_ary A, zbior_ary B){
	zbior_ary C = empty_set();
	int i = 0, j = 0;
	while (i < A.n && j < B.n){
		if (A.buckets[i].rem < B.buckets[j].rem) i++;
		else if (B.buckets[j].rem < A.buckets[i].rem) j++;
		else{
			zbior_ary_bucket tmp = empty_bucket();
			tmp.rem = A.buckets[i].rem;
			merge_intersection(&A.buckets[i], &B.buckets[j], &tmp);
			if (tmp.len > 0){
				zbior_ary_bucket *res = insert_bucket(&C, tmp.rem);
				res->len = tmp.len;
				res->cap = tmp.cap;
				res->segs = tmp.segs;
				tmp.len = tmp.cap = 0; tmp.segs = NULL;
			}else{
				free(tmp.segs);
			}
			i++; j++;
		}
	}
	return C;
}

zbior_ary roznica(zbior_ary A, zbior_ary B){
	zbior_ary C = empty_set();
	int i = 0, j = 0;
	while (i < A.n){
		while (j < B.n && B.buckets[j].rem < A.buckets[i].rem) ++j;
		if (j == B.n || (i < A.n && A.buckets[i].rem < B.buckets[j].rem)){
			zbior_ary_bucket *res = insert_bucket(&C, A.buckets[i].rem);
			for (int k = 0; k < A.buckets[i].len; k++){
				push_seg(res, A.buckets[i].segs[k].l, A.buckets[i].segs[k].r);
			}
			i++;
		}else{
			zbior_ary_bucket tmp = empty_bucket();
			tmp.rem = A.buckets[i].rem;
			merge_difference(&A.buckets[i], &B.buckets[j], &tmp);
			if (tmp.len > 0){
				zbior_ary_bucket *res = insert_bucket(&C, tmp.rem);
				res->len = tmp.len;
				res->cap = tmp.cap;
				res->segs = tmp.segs;
				tmp.len = tmp.cap = 0; tmp.segs = NULL;
			}else{
				free(tmp.segs);
			}
			i++; j++;
		}
	}
	return C;
}

bool nalezy(zbior_ary A, int b){
	if (A.n == 0) return false;
	int rem = modulo(b, Q), idx = lower_bound_bucket(&A, rem);
	if (idx < 0) return false;
	zbior_ary_bucket *B = &A.buckets[idx];
	if (B->len == 0) return false;
	ll val = ((ll)b - (ll)rem) / (ll)Q; int lo = 0, hi = B->len - 1;
	while (lo < hi){
		int mid = (lo + hi) / 2;
		if (B->segs[mid].r < val) lo = mid + 1;
		else hi = mid;
	}
	return B->segs[lo].l <= val && B->segs[lo].r >= val;
}

unsigned moc(zbior_ary A){
	ll res = 0;
	for (int i = 0; i < A.n; i++){
		zbior_ary_bucket *B = &A.buckets[i];
		for (int j = 0; j < B->len; j++){
			res += (ll)((ll)B->segs[j].r - (ll)B->segs[j].l + 1);
		}
	}
	return (unsigned)res;
}

unsigned ary(zbior_ary A){
	unsigned res = 0;
	for (int i = 0; i < A.n; i++) res += (unsigned)A.buckets[i].len;
	return res;
}

//#ifdef __cplusplus
//#include <bits/stdc++.h>
//using namespace std;
//extern "C" void print(zbior_ary A) {
    //vector<int> v;
    //for (int i = 0; i < A.n; ++i) {
        //const auto &B = A.buckets[i];
        //for (int j = 0; j < B.len; ++j) {
            //for (int k = B.segs[j].l; k <= B.segs[j].r; ++k) {
                //v.push_back(B.rem + k * Q);   // x = rem + k*q
            //}
        //}
    //}
    //sort(v.begin(), v.end());
    //for (size_t i = 0; i < v.size(); ++i) {
        //cout << v[i] << ' ';
    //}
    //cout << '\n';
//}
//#endif

//#ifdef ARY_LOCAL_MAIN
//int main(){
	//zbior_ary A = ciag_arytmetyczny(2, 3, 11);
	//printf("%d %d", A.buckets[0].segs[0].l, A.buckets[0].segs[0].r);
	//return 0;
//}
//#endif

/*

gcc @opcje \
    -DARY_LOCAL_MAIN -DDEBUG -UNDEBUG \
    -g -O0 -fsanitize=address,undefined \
    zbior_ary.c -o zbior_ary -lm

g++ -o ary interactor.cpp zbior_ary.c

toster --io tiny ./ary
toster --io small ./ary
toster --io medium ./ary
toster --io big ./ary
toster --io overflow ./ary


gcc @opcje \
    -DDEBUG -UNDEBUG \
    -g -O0 -fsanitize=address,undefined \
    ocen.c zbior_ary.c -o ary -lm
ASAN_OPTIONS=detect_leaks=0 ./ary

*/
