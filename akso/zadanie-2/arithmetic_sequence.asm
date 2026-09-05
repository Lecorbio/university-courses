global arithmetic_sequence

section .text

arithmetic_sequence:
    ; Liczymy A_k = A0 + k * (A1 - A0).
    ; Wejscie ABI: rdi=A0, rsi=A1, rdx=Ak, rcx=n, r8=k.
    ; Wynik struktury int128_t wraca w rax:rdx, czyli lo w rax, hi w rdx.
    ; Funkcja nie uzywa stosu ani rejestrow callee-saved.
    ;
    ; Uzycie rejestrow:
    ;   rdi - biezacy wskaznik A0, przesuwany przez SCASQ,
    ;   rsi - najpierw biezacy wskaznik A1, potem biezacy wskaznik Ak,
    ;   r8  - k, zachowane do konca,
    ;   r9  - podpisane przeniesienie z nizszych slow,
    ;   r10 - sign(r9), czyli 0 albo -1,
    ;   r11 - indeks w pierwszej petli, potem licznik drugiej petli.
    xor r11d, r11d

.sub_loop:
    ; Pierwsza petla zapisuje w Ak mlodsze n slow roznicy
    ; D = A1 - A0. Pozyczka przechodzi przez CF miedzy slowami.
    ; LODSQ, MOV, LEA i LOOP nie zmieniaja flag ustawionych przez SBB.
    lodsq                       ; rax = A1[i], rsi += 8.
    sbb rax, [rdi + r11 * 8]    ; rax = A1[i] - A0[i] - CF.
    mov [rdx + r11 * 8], rax    ; Ak[i] = D[i].
    lea r11, [r11 + 1]          ; i++ bez zmiany flag.
    loop .sub_loop              ; rcx-- bez zmiany flag.

    ; Po ostatnim SBB flagi opisuja porownanie signed A1 z A0.
    ; Poniewaz D jest roznica dwoch n-slowowych liczb signed, slowo nad Ak
    ; jest tylko 0 albo -1. Budujemy je w rcx:
    ;   A1 >= A0  -> cl=1, dec rcx daje 0,
    ;   A1 < A0   -> cl=0, dec rcx daje -1.
    setge cl
    dec rcx

    ; Druga petla liczy kolejne slowa wyniku:
    ;   Ak[i] = low64(D[i] * k + carry + A0[i]).
    ; Wysokie slowo tej sumy staje sie podpisanym carry dla nastepnego i.
    mov rsi, rdx                ; odtad LODSQ czyta slowa D z Ak.
    xor r9d, r9d                ; poczatkowe carry = 0.
    xor r10d, r10d              ; sign(carry) = 0.

.mul_loop:
    ; MUL daje unsigned(D[i]) * unsigned(k). Gdy k < 0, mamy
    ; unsigned(k) = signed(k) + 2^64, wiec trzeba od wysokiego slowa
    ; iloczynu odjac D[i]. Dostajemy unsigned(D[i]) * signed(k).
    lodsq                       ; rax = D[i], rsi += 8.
    mul r8                      ; rdx:rax = D[i] * unsigned(k).
    test r8, r8                 ; czy k jest ujemne?
    jns .mul_carry
    sub rdx, [rsi - 8]          ; poprawka wysokiego slowa dla k < 0.

.mul_carry:
    ; Dodajemy podpisane carry z poprzedniego slowa oraz A0[i].
    ; Po tych dodaniach rdx jest nowym carry, a rax gotowym slowem Ak[i].
    add rax, r9                 ; + low64(carry).
    adc rdx, r10                ; + sign(carry) i przeniesienie z low word.
    add rax, [rdi]              ; + A0[i].
    adc rdx, 0                  ; przeniesienie po dodaniu A0[i].
    mov [rsi - 8], rax          ; zapisz Ak[i].
    mov r9, rdx                 ; carry dla kolejnego slowa.
    mov r10, rdx
    sar r10, 63                 ; r10 = sign(carry).
    scasq                       ; rdi += 8, wskazuje nastepne A0.
    dec r11                     ; r11 startuje jako n po pierwszej petli.
    jnz .mul_loop

    ; Po n slowach zostaly wysokie 128 bitow:
    ;   D_hi * k + carry + sign(A0).
    ; IMUL mnozy D_hi (0 albo -1) przez signed k i zwraca wynik w rdx:rax.
    mov rax, rcx
    imul r8
    add rax, r9                 ; + carry.
    adc rdx, r10

    ; rdi wskazuje tu za ostatnie slowo A0, wiec [rdi-1] to najstarszy bajt.
    ; CMP ustawia CF=1 dla A0 >= 0 i CF=0 dla A0 < 0. Dwa ADC z -1 dodaja
    ; odpowiednio 0 albo -1 do 128-bitowego wyniku, czyli sign(A0).
    cmp byte [rdi - 1], 0x80
    adc rax, -1
    adc rdx, -1
    ret
