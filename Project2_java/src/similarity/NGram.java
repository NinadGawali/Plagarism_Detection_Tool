package Project2_java.src.algo;

import java.util.*;

public class NGram {
    public static Set<Integer> build(String text, int n) {
        Set<Integer> set = new HashSet<>();
        if (text.length() < n) return set;
        for (int i = 0; i + n <= text.length(); ++i) {
            set.add(text.substring(i, i + n).hashCode());
        }
        return set;
    }

    public static double jaccard(Set<Integer> a, Set<Integer> b) {
        if (a.isEmpty() && b.isEmpty()) return 1.0;
        Set<Integer> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        Set<Integer> union = new HashSet<>(a);
        union.addAll(b);
        return union.isEmpty() ? 1.0 :
            ((double) intersection.size()) / union.size();
    }
}