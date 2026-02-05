package Project2_java.src.algo;

import java.util.*;

public class SuffixArray {
    private String s;
    private int[] sa;

    public SuffixArray(String text) {
        s = text;
        buildSuffixArray();
    }

    private void buildSuffixArray() {
        int n = s.length();
        sa = new int[n];
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(i -> s.charAt(i)));
        int[] rank = new int[n], tmp = new int[n];
        for (int i = 0; i < n; ++i) rank[i] = s.charAt(i);
        for (int k = 1;; k <<= 1) {
            int finalK = k;
            Arrays.sort(order, (i, j) -> {
                if (rank[i] != rank[j]) return Integer.compare(rank[i], rank[j]);
                int ri = i + finalK < n ? rank[i + finalK] : -1;
                int rj = j + finalK < n ? rank[j + finalK] : -1;
                return Integer.compare(ri, rj);
            });
            tmp[order[0]] = 0;
            for (int i = 1; i < n; ++i)
                tmp[order[i]] = tmp[order[i - 1]] +
                        ((rank[order[i - 1]] != rank[order[i]])
                                || ((order[i - 1] + k < n ? rank[order[i - 1] + k] : -1) != (order[i] + k < n ? rank[order[i] + k] : -1)) ? 1 : 0);
            System.arraycopy(tmp, 0, rank, 0, n);
            if (rank[order[n - 1]] == n - 1) break;
        }
        for (int i = 0; i < n; i++) sa[i] = order[i];
    }

    public List<Integer> substringSearch(String pat) {
        int l = 0, r = sa.length, n = s.length(), m = pat.length();
        // Left boundary
        while (l < r) {
            int mid = (l + r) / 2;
            int cmp = s.substring(sa[mid], Math.min(n, sa[mid] + m)).compareTo(pat);
            if (cmp < 0) l = mid + 1; else r = mid;
        }
        int left = l;
        r = sa.length;
        while (l < r) {
            int mid = (l + r) / 2;
            int cmp = s.substring(sa[mid], Math.min(n, sa[mid] + m)).compareTo(pat);
            if (cmp <= 0) l = mid + 1; else r = mid;
        }
        int right = l;
        List<Integer> res = new ArrayList<>();
        for (int i = left; i < right; ++i) res.add(sa[i]);
        return res;
    }

    public int[] sa() { return sa; }
}