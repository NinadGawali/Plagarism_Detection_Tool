package Project2_java.src.algo;

import java.util.*;

public class Trie {
    private static class Node {
        boolean end = false;
        Map<Character, Node> nxt = new HashMap<>();
    }

    private Node root = new Node();

    public void insert(String word) {
        Node cur = root;
        for (char c : word.toCharArray()) {
            cur = cur.nxt.computeIfAbsent(c, k -> new Node());
        }
        cur.end = true;
    }

    public boolean contains(String word) {
        Node cur = root;
        for (char c : word.toCharArray()) {
            cur = cur.nxt.get(c);
            if (cur == null) return false;
        }
        return cur.end;
    }
}