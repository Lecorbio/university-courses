public class Glosowanie {
    public static int wyznaczZwyciezce(int[] glosy) {
        int n = glosy.length, candidate = glosy[0], cnt = 1;
        for (int i = 1; i < n; i++) {
            if (glosy[i] == candidate) cnt++;
            else {
                cnt--;
                if (cnt == 0) {
                    candidate = glosy[i];
                    cnt = 1;
                }
            }
        }

        int votes = 0;
        for (int i = 0; i < n; i++) if (glosy[i] == candidate) votes++;
        if (votes > n / 2) return candidate;
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(wyznaczZwyciezce(new int[]{5, 2, 1, 3}));
        System.out.println(wyznaczZwyciezce(new int[]{1, 1, 2, 1, 3, 1, 1}));
        System.out.println(wyznaczZwyciezce(new int[]{2, 2, 1, 1, 2, 2}));
        System.out.println(wyznaczZwyciezce(new int[]{7, 3, 7, 2, 7, 4, 7}));
        System.out.println(wyznaczZwyciezce(new int[]{9, 1, 9, 2, 9, 3, 9, 4}));
    }
}
