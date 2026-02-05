package Project2_java.src.algo;

import java.util.*;

public class RabinKarp {
    public static List<Integer> search(String pat, String txt, long base, long mod) {
        int m = pat.length(), n = txt.length();
        List<Integer> pos = new ArrayList<>();
        if (m == 0 || m > n) return pos;
        long h = 1, pHash = 0, tHash = 0;
        for (int i = 0; i < m - 1; ++i) h = (h * base) % mod;
        for (int i = 0; i < m; ++i) {
            pHash = (pHash * base + pat.charAt(i)) % mod;
            tHash = (tHash * base + txt.charAt(i)) % mod;
        }
        for (int i = 0; i <= n - m; ++i) {
            if (pHash == tHash && txt.substring(i, i + m).equals(pat))
                pos.add(i);
            if (i < n - m) {
                tHash = (base * (tHash + mod - txt.charAt(i) * h % mod) + txt.charAt(i + m)) % mod;
            }
        }
        return pos;
    }
    public static List<Integer> search(String pat, String txt) {
        return search(pat, txt, 257L, 1_000_000_007L);
    }
}
