package Project2_java.src.algo;

import java.util.*;

public class KMP {
    private static int[] buildLps(String pat) {
        int m = pat.length();
        int[] lps = new int[m];
        for (int i = 1, len = 0; i < m;) {
            if (pat.charAt(i) == pat.charAt(len)) lps[i++] = ++len;
            else if (len != 0) len = lps[len - 1];
            else lps[i++] = 0;
        }
        return lps;
    }

    public static List<Integer> search(String pat, String txt) {
        int m = pat.length(), n = txt.length();
        List<Integer> pos = new ArrayList<>();
        if (m == 0) return pos;
        int[] lps = buildLps(pat);
        for (int i = 0, j = 0; i < n;) {
            if (pat.charAt(j) == txt.charAt(i)) { i++; j++; }
            if (j == m) {
                pos.add(i - j);
                j = lps[j - 1];
            } else if (i < n && pat.charAt(j) != txt.charAt(i)) {
                if (j != 0) j = lps[j - 1];
                else i++;
            }
        }
        return pos;
    }
}