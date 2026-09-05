default rel

global _start

; AKSO, zadanie 3 - discrete_fractal
;
; Program czyta z stdin napis poczatkowy i kolejne linie z regulami.
; Argument argv[1] okresla liczbe iteracji.
; Po wykonaniu zadanej liczby iteracji program wypisuje wynik i znak '\n'.
;
; Wazne rejestry w wiekszosci programu:
;   r15 - adres bloku stanu programu zaalokowanego przez mmap,
;   rax - wynik syscalla albo wartosc pomocnicza,
;   rbx - najczesciej aktualny znak, jako indeks do tablic regul,
;   rsi - zwykle wskaznik zrodla,
;   rdi - zwykle wskaznik celu albo pierwszy argument syscalla,
;   rdx - zwykle dlugosc bufora,
;   r8, r9 - kursory po aktualnie przetwarzanym napisie,
;   r10, r11, r12 - pomocnicze adresy tablic.

%define SYS_READ        0
%define SYS_WRITE       1
%define SYS_MMAP        9
%define SYS_MUNMAP      11
%define SYS_EXIT        60

%define PROT_RW         3
%define MAP_PRIVATE_ANON 0x22
%define INITIAL_CAP     65536
%define MAX_ARG         4294967295

; Offsety pol w bloku stanu wskazywanym przez r15.
%define INPUT_PTR       0
%define INPUT_LEN       8
%define INPUT_CAP       16
%define CUR_PTR         24
%define CUR_LEN         32
%define CUR_CAP         40
%define NEXT_PTR        48
%define NEXT_LEN        56
%define NEXT_CAP        64
%define OUT_PTR         72
%define OUT_LEN         80
%define OUT_CAP         88
%define ITERATIONS_LEFT 96
%define CHANGED_FLAG    104
%define RULES_PTR       112
%define RULES_LEN       1136
%define RULES_SET       2160
%define STATE_SIZE      2288

section .text

; Glowne pola stanu:
;   input_* - bufor z calym stdin,
;   cur_*   - aktualny napis,
;   next_*  - bufor kolejnej iteracji,
;   out_*   - bufor stdout,
;   rules_* - tablice regul indeksowane kodem ASCII.
; R15 przez caly czas wskazuje blok stanu zaalokowany przez mmap.

_start:
        ; Blok stanu zawiera wskazniki, dlugosci buforow i tablice regul.
        call    init_state                 ; Wyzerowany blok stanu programu.

        cmp     qword [rsp], 2             ; Program ma miec dokladnie jeden parametr.
        jne     fail

        mov     rsi, [rsp + 16]            ; argv[1].
        call    parse_argument             ; RAX = liczba iteracji.
        mov     [r15 + ITERATIONS_LEFT], rax

        call    read_input                 ; Wczytaj cale stdin do bufora.
        call    parse_input                ; Sprawdz format i zapamietaj reguly.

        ; Dla n = 0 aktualny napis jest gotowym wynikiem.
        cmp     qword [r15 + ITERATIONS_LEFT], 0 ; Dla n = 0 wypisujemy napis poczatkowy.
        je      output_current

        ; Po dekrementacji petla buduje tylko iteracje przed ostatnia.
        dec     qword [r15 + ITERATIONS_LEFT]    ; Jedna iteracja zostaje na wypisywanie.

main_loop:
        cmp     qword [r15 + ITERATIONS_LEFT], 0
        je      output_last_iteration

        call    build_next_iteration       ; Wykonaj jedna materializowana iteracje.
        test    al, al                     ; AL = 0, jesli napis juz sie nie zmienia.
        jz      output_current

        dec     qword [r15 + ITERATIONS_LEFT]
        jmp     main_loop

output_last_iteration:
        ; W cur jest napis po n-1 iteracjach; wypisywane jest h(cur).
        call    init_output_buffer
        call    emit_last_iteration
        call    emit_newline
        call    flush_output
        jmp     exit_success

; init_state() tworzy blok na zmienne globalne i tablice regul.
init_state:
        ; mmap(NULL, STATE_SIZE, PROT_READ|PROT_WRITE,
        ;      MAP_PRIVATE|MAP_ANONYMOUS, -1, 0)
        mov     rsi, STATE_SIZE
        xor     edi, edi
        mov     edx, PROT_RW
        mov     r10d, MAP_PRIVATE_ANON
        mov     r8, -1
        xor     r9d, r9d
        mov     eax, SYS_MMAP
        syscall

        cmp     rax, -4095
        jae     exit_failure_without_state

        mov     r15, rax
        ret

exit_failure_without_state:
        mov     edi, 1
        mov     eax, SYS_EXIT
        syscall

output_current:
        ; Wypisuje dokladnie aktualny napis, bez wykonywania kolejnej reguly.
        call    init_output_buffer
        mov     rsi, [r15 + CUR_PTR]
        mov     rdx, [r15 + CUR_LEN]
        call    emit_bytes
        call    emit_newline
        call    flush_output
        jmp     exit_success

; parse_argument(RSI = argv[1]) -> RAX = wartosc z zakresu 0..2^32-1.
parse_argument:
        xor     eax, eax                   ; RAX trzyma aktualna wartosc.
        xor     r8d, r8d                   ; R8B mowi, czy byl co najmniej jeden znak.

.digit_loop:
        movzx   ecx, byte [rsi]
        test    cl, cl
        jz      .done

        cmp     cl, '0'
        jb      fail
        cmp     cl, '9'
        ja      fail

        cmp     rax, MAX_ARG / 10          ; Chroni mnozenie przed przekroczeniem limitu.
        ja      fail
        imul    rax, rax, 10

        sub     ecx, '0'
        add     rax, rcx
        mov     rdx, MAX_ARG
        cmp     rax, rdx
        ja      fail

        mov     r8b, 1
        inc     rsi
        jmp     .digit_loop

.done:
        test    r8b, r8b                   ; Pusty parametr nie jest liczba.
        jz      fail
        ret

; read_input() wypelnia input_ptr/input_len/input_cap.
read_input:
        ; INPUT_PTR wskazuje bufor z calym stdin.
        ; INPUT_LEN oznacza liczbe wczytanych bajtow.
        mov     rdi, INITIAL_CAP
        call    mmap_alloc
        mov     [r15 + INPUT_PTR], rax
        mov     qword [r15 + INPUT_LEN], 0
        mov     qword [r15 + INPUT_CAP], INITIAL_CAP

.read_loop:
        mov     rax, [r15 + INPUT_LEN]
        cmp     rax, [r15 + INPUT_CAP]
        jb      .have_space

        call    grow_input

.have_space:
        mov     rdx, [r15 + INPUT_CAP]     ; RDX = wolne miejsce w buforze.
        sub     rdx, [r15 + INPUT_LEN]
        mov     rsi, [r15 + INPUT_PTR]
        add     rsi, [r15 + INPUT_LEN]
        xor     edi, edi                   ; fd = stdin.
        mov     eax, SYS_READ
        syscall

        test    rax, rax
        js      fail
        jz      .eof

        add     [r15 + INPUT_LEN], rax
        jmp     .read_loop

.eof:
        ret

; parse_input() waliduje wejscie, ustawia cur_ptr/cur_len oraz tablice regul.
parse_input:
        ; Format wejscia: pierwsza linia to napis poczatkowy,
        ; nastepne linie to reguly. Wszystkie linie musza konczyc sie '\n'.
        mov     rax, [r15 + INPUT_LEN]
        test    rax, rax                   ; Musi istniec pierwsza linia.
        jz      fail

        mov     rsi, [r15 + INPUT_PTR]
        lea     rdi, [rsi + rax]           ; RDI = koniec danych.
        cmp     byte [rdi - 1], 10         ; Kazda linia konczy sie znakiem nowej linii.
        jne     fail

        mov     r8, rsi                    ; R8 = poczatek napisu poczatkowego.

.initial_loop:
        cmp     rsi, rdi
        jae     fail

        movzx   eax, byte [rsi]
        cmp     al, 10
        je      .initial_done

        call    require_symbol
        inc     rsi
        jmp     .initial_loop

.initial_done:
        ; cur wskazuje na pierwsza linie w buforze wejscia.
        ; cur_cap = 0 oznacza, ze cur nie ma osobnej alokacji.
        mov     [r15 + CUR_PTR], r8
        mov     rax, rsi
        sub     rax, r8
        mov     [r15 + CUR_LEN], rax
        mov     qword [r15 + CUR_CAP], 0   ; Napis poczatkowy lezy w buforze wejscia.
        inc     rsi

        lea     r10, [r15 + RULES_SET]
        lea     r11, [r15 + RULES_PTR]
        lea     r12, [r15 + RULES_LEN]

.rules_loop:
        cmp     rsi, rdi
        jae     .done

        ; Lewa strona reguly to pierwszy znak linii, a prawa strona to
        ; reszta linii do znaku nowej linii.
        movzx   ebx, byte [rsi]            ; Pierwszy znak linii to zastepowany symbol.
        cmp     bl, 10
        je      fail                       ; Pusta linia reguly jest niepoprawna.

        mov     eax, ebx
        call    require_symbol

        cmp     byte [r10 + rbx], 0
        jne     fail                       ; Symbol moze miec co najwyzej jedna regule.
        mov     byte [r10 + rbx], 1

        lea     rax, [rsi + 1]
        mov     [r11 + rbx * 8], rax       ; Tresc zastepujaca zaczyna sie za symbolem.
        inc     rsi
        mov     r9, rsi                    ; R9 = poczatek prawej strony reguly.

.rule_body_loop:
        cmp     rsi, rdi
        jae     fail

        movzx   eax, byte [rsi]
        cmp     al, 10
        je      .rule_done

        call    require_symbol
        inc     rsi
        jmp     .rule_body_loop

.rule_done:
        ; RULES_LEN[znak] dostaje dlugosc prawej strony reguly.
        mov     rax, rsi
        sub     rax, r9
        mov     [r12 + rbx * 8], rax
        inc     rsi
        jmp     .rules_loop

.done:
        ret

; require_symbol(AL) sprawdza, czy znak jest symbolem ASCII 33..126.
require_symbol:
        cmp     al, 33
        jb      fail
        cmp     al, 126
        ja      fail
        ret

; build_next_iteration() buduje h(cur) w next, zamienia bufory i zwraca AL = 1,
; jesli w tej iteracji faktycznie zmienil sie jakis znak.
build_next_iteration:
        ; NEXT_LEN = 0 oznacza pusty bufor nastepnej iteracji.
        mov     qword [r15 + NEXT_LEN], 0
        mov     byte [r15 + CHANGED_FLAG], 0

        mov     r8, [r15 + CUR_PTR]        ; R8 = aktualnie czytany znak.
        mov     r9, [r15 + CUR_LEN]
        add     r9, r8                     ; R9 = koniec aktualnego napisu.

.char_loop:
        cmp     r8, r9
        jae     .finish

        movzx   ebx, byte [r8]
        inc     r8

        lea     r10, [r15 + RULES_SET]
        cmp     byte [r10 + rbx], 0
        je      .copy_char

        lea     r10, [r15 + RULES_PTR]
        mov     rsi, [r10 + rbx * 8]
        lea     r10, [r15 + RULES_LEN]
        mov     rdx, [r10 + rbx * 8]

        cmp     rdx, 1                     ; Regula c -> c nie zmienia napisu.
        jne     .mark_changed
        cmp     byte [rsi], bl
        je      .append_rule

.mark_changed:
        mov     byte [r15 + CHANGED_FLAG], 1

.append_rule:
        ; R8 i R9 sa kursorami petli po aktualnym napisie.
        push    r8
        push    r9
        call    append_bytes
        pop     r9
        pop     r8
        jmp     .char_loop

.copy_char:
        ; Znak bez reguly przechodzi do nastepnego napisu bez zmian.
        mov     al, bl
        push    r8
        push    r9
        call    append_char
        pop     r9
        pop     r8
        jmp     .char_loop

.finish:
        ; cur zostaje zastapione buforem next.
        mov     rdi, [r15 + CUR_PTR]
        mov     rsi, [r15 + CUR_CAP]
        call    munmap_checked             ; Dla cur_cap = 0 nic nie robi.

        mov     rax, [r15 + NEXT_PTR]
        mov     [r15 + CUR_PTR], rax
        mov     rax, [r15 + NEXT_LEN]
        mov     [r15 + CUR_LEN], rax
        mov     rax, [r15 + NEXT_CAP]
        mov     [r15 + CUR_CAP], rax

        mov     qword [r15 + NEXT_PTR], 0
        mov     qword [r15 + NEXT_LEN], 0
        mov     qword [r15 + NEXT_CAP], 0

        movzx   eax, byte [r15 + CHANGED_FLAG]
        ret

; emit_last_iteration() wypisuje wynik podstawienia h(cur).
emit_last_iteration:
        ; Procedura czyta cur i dopisuje wynik podstawienia do bufora wyjscia.
        mov     r8, [r15 + CUR_PTR]
        mov     r9, [r15 + CUR_LEN]
        add     r9, r8

.char_loop:
        cmp     r8, r9
        jae     .done

        movzx   ebx, byte [r8]
        inc     r8

        lea     r10, [r15 + RULES_SET]
        cmp     byte [r10 + rbx], 0
        je      .emit_char

        lea     r10, [r15 + RULES_PTR]
        mov     rsi, [r10 + rbx * 8]
        lea     r10, [r15 + RULES_LEN]
        mov     rdx, [r10 + rbx * 8]

        push    r8
        push    r9
        call    emit_bytes
        pop     r9
        pop     r8
        jmp     .char_loop

.emit_char:
        mov     al, bl
        push    r8
        push    r9
        call    emit_char
        pop     r9
        pop     r8
        jmp     .char_loop

.done:
        ret

; append_char(AL) dopisuje jeden znak do bufora next.
append_char:
        ; AL zawiera dopisywany znak.
        push    rax
        mov     rax, [r15 + NEXT_LEN]
        cmp     rax, [r15 + NEXT_CAP]
        jb      .space_ready

        inc     rax
        mov     rdi, rax
        call    grow_next_to

.space_ready:
        pop     rax
        mov     rdi, [r15 + NEXT_PTR]
        add     rdi, [r15 + NEXT_LEN]
        mov     [rdi], al
        inc     qword [r15 + NEXT_LEN]
        ret

; append_bytes(RSI = adres, RDX = dlugosc) dopisuje bajty do bufora next.
append_bytes:
        ; Dla RDX = 0 nie ma bajtow do dopisania.
        test    rdx, rdx
        jz      .done

        ; RAX = nowa dlugosc next po dopisaniu fragmentu.
        mov     rax, [r15 + NEXT_LEN]
        add     rax, rdx
        jc      fail
        cmp     rax, [r15 + NEXT_CAP]
        jbe     .space_ready

        push    rsi
        push    rdx
        mov     rdi, rax
        call    grow_next_to
        pop     rdx
        pop     rsi

.space_ready:
        ; rep movsb kopiuje RCX bajtow z RSI do RDI.
        mov     rdi, [r15 + NEXT_PTR]
        add     rdi, [r15 + NEXT_LEN]
        mov     rcx, rdx
        rep     movsb
        add     [r15 + NEXT_LEN], rdx

.done:
        ret

; init_output_buffer() tworzy bufor wyjscia.
init_output_buffer:
        cmp     qword [r15 + OUT_CAP], 0
        jne     .done

        mov     rdi, INITIAL_CAP
        call    mmap_alloc
        mov     [r15 + OUT_PTR], rax
        mov     qword [r15 + OUT_LEN], 0
        mov     qword [r15 + OUT_CAP], INITIAL_CAP

.done:
        ret

; emit_char(AL) dopisuje znak do bufora wyjscia.
emit_char:
        ; AL zawiera znak dopisywany do bufora wyjscia.
        push    rax
        mov     rax, [r15 + OUT_LEN]
        cmp     rax, [r15 + OUT_CAP]
        jb      .space_ready

        call    flush_output

.space_ready:
        pop     rax
        mov     rdi, [r15 + OUT_PTR]
        add     rdi, [r15 + OUT_LEN]
        mov     [rdi], al
        inc     qword [r15 + OUT_LEN]
        ret

; emit_bytes(RSI = adres, RDX = dlugosc) kopiuje dane do bufora wyjscia.
emit_bytes:
        ; Petla kopiuje kolejne fragmenty miesczace sie w buforze wyjscia.
        test    rdx, rdx
        jz      .done

.copy_loop:
        test    rdx, rdx
        jz      .done

        mov     rax, [r15 + OUT_LEN]
        mov     rbx, [r15 + OUT_CAP]
        sub     rbx, rax                   ; RBX = wolne miejsce.
        jnz     .have_space

        push    rsi
        push    rdx
        call    flush_output
        pop     rdx
        pop     rsi
        jmp     .copy_loop

.have_space:
        mov     rcx, rdx                   ; RCX = ile skopiowac teraz.
        cmp     rcx, rbx
        jbe     .chunk_ready
        mov     rcx, rbx

.chunk_ready:
        mov     r8, rcx
        mov     rdi, [r15 + OUT_PTR]
        add     rdi, [r15 + OUT_LEN]
        rep     movsb
        add     [r15 + OUT_LEN], r8
        sub     rdx, r8
        jmp     .copy_loop

.done:
        ret

emit_newline:
        mov     al, 10
        jmp     emit_char

; flush_output() wypisuje cala zawartosc bufora wyjscia.
flush_output:
        mov     rdx, [r15 + OUT_LEN]
        test    rdx, rdx
        jz      .done

        mov     rsi, [r15 + OUT_PTR]

.write_loop:
        test    rdx, rdx
        jz      .flushed

        mov     eax, SYS_WRITE
        mov     edi, 1                     ; fd = stdout.
        syscall

        test    rax, rax
        jle     fail

        add     rsi, rax
        sub     rdx, rax
        jmp     .write_loop

.flushed:
        mov     qword [r15 + OUT_LEN], 0

.done:
        ret

; grow_input() podwaja bufor wejscia i kopiuje juz wczytane dane.
grow_input:
        ; RBX przechowuje nowa pojemnosc bufora wejscia.
        mov     rbx, [r15 + INPUT_CAP]
        shl     rbx, 1
        jc      fail

        mov     rdi, rbx
        call    mmap_alloc
        mov     r8, rax

        mov     rdi, r8
        mov     rsi, [r15 + INPUT_PTR]
        mov     rcx, [r15 + INPUT_LEN]
        rep     movsb

        push    r8
        push    rbx
        mov     rdi, [r15 + INPUT_PTR]
        mov     rsi, [r15 + INPUT_CAP]
        call    munmap_checked
        pop     rbx
        pop     r8

        mov     [r15 + INPUT_PTR], r8
        mov     [r15 + INPUT_CAP], rbx
        ret

; grow_next_to(RDI = wymagana dlugosc) powieksza next_cap do co najmniej RDI.
grow_next_to:
        ; RBX przechowuje nowa pojemnosc bufora next.
        mov     rbx, [r15 + NEXT_CAP]
        test    rbx, rbx
        jnz     .double

        mov     rbx, INITIAL_CAP
        jmp     .fit_loop

.double:
        shl     rbx, 1
        jc      fail

.fit_loop:
        cmp     rbx, rdi
        jae     .allocate

        shl     rbx, 1
        jc      fail
        jmp     .fit_loop

.allocate:
        mov     rdi, rbx
        call    mmap_alloc
        mov     r8, rax

        mov     rdi, r8
        mov     rsi, [r15 + NEXT_PTR]
        mov     rcx, [r15 + NEXT_LEN]
        rep     movsb

        push    r8
        push    rbx
        mov     rdi, [r15 + NEXT_PTR]
        mov     rsi, [r15 + NEXT_CAP]
        call    munmap_checked
        pop     rbx
        pop     r8

        mov     [r15 + NEXT_PTR], r8
        mov     [r15 + NEXT_CAP], rbx
        ret

; mmap_alloc(RDI = rozmiar) -> RAX = adres albo zakonczenie z bledem.
mmap_alloc:
        test    rdi, rdi
        jz      fail

        mov     rsi, rdi
        xor     edi, edi                   ; Adres wybiera jadro.
        mov     edx, PROT_RW
        mov     r10d, MAP_PRIVATE_ANON
        mov     r8, -1
        xor     r9d, r9d
        mov     eax, SYS_MMAP
        syscall

        cmp     rax, -4095                 ; Linux zwraca bledy jako -errno.
        jae     fail
        ret

; munmap_checked(RDI = adres, RSI = rozmiar) zwalnia zaalokowany obszar.
munmap_checked:
        test    rdi, rdi
        jz      .done
        test    rsi, rsi
        jz      .done

        mov     eax, SYS_MUNMAP
        syscall
        test    rax, rax
        jne     fail

.done:
        ret

exit_success:
        call    cleanup_checked
        xor     edi, edi
        mov     eax, SYS_EXIT
        syscall

fail:
        call    cleanup_unchecked
        mov     edi, 1
        mov     eax, SYS_EXIT
        syscall

; cleanup_checked() zwalnia bufory i sprawdza wyniki munmap.
cleanup_checked:
        ; OUT, NEXT, CUR, INPUT i blok stanu sa zwalniane w tej kolejnosci.
        mov     rdi, [r15 + OUT_PTR]
        mov     rsi, [r15 + OUT_CAP]
        call    munmap_checked

        mov     rdi, [r15 + NEXT_PTR]
        mov     rsi, [r15 + NEXT_CAP]
        call    munmap_checked

        mov     rdi, [r15 + CUR_PTR]
        mov     rsi, [r15 + CUR_CAP]
        call    munmap_checked

        mov     rdi, [r15 + INPUT_PTR]
        mov     rsi, [r15 + INPUT_CAP]
        call    munmap_checked

        mov     rdi, r15
        mov     rsi, STATE_SIZE
        call    munmap_checked
        ret

; cleanup_unchecked() zwalnia bufory na sciezce bledu.
cleanup_unchecked:
        ; raw_munmap nie sprawdza wyniku syscalla.
        mov     rdi, [r15 + OUT_PTR]
        mov     rsi, [r15 + OUT_CAP]
        call    raw_munmap

        mov     rdi, [r15 + NEXT_PTR]
        mov     rsi, [r15 + NEXT_CAP]
        call    raw_munmap

        mov     rdi, [r15 + CUR_PTR]
        mov     rsi, [r15 + CUR_CAP]
        call    raw_munmap

        mov     rdi, [r15 + INPUT_PTR]
        mov     rsi, [r15 + INPUT_CAP]
        call    raw_munmap

        mov     rdi, r15
        mov     rsi, STATE_SIZE
        call    raw_munmap
        ret

; raw_munmap wywoluje munmap bez sprawdzania kodu powrotu.
raw_munmap:
        test    rdi, rdi
        jz      .done
        test    rsi, rsi
        jz      .done

        mov     eax, SYS_MUNMAP
        syscall

.done:
        ret

section .note.GNU-stack noalloc noexec nowrite progbits
