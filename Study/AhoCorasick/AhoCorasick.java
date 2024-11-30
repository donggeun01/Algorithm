package Study.AhoCorasick;

import java.util.*;

public class AhoCorasick {

    public static void main(String[] args) {
        Trie trie = new Trie(Arrays.asList("he", "she", "his", "hers"));

        String text = "ushers";
        ArrayList<Integer> matches = search(text, trie);

        System.out.println("Matches found at indices: " + matches);
    }

    public static ArrayList<Integer> search(String text, Trie trie) {
        TrieNode node = trie.root;
        ArrayList<Integer> matches = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // 현재 문자에 대해 실패 링크를 따라가며 트라이를 탐색
            while (node != null && !node.child.containsKey(c)) {
                node = node.fail;
            }

            if (node == null) {
                node = trie.root;
                continue;
            }

            node = node.child.get(c);

            // 현재 노드에서 매칭되는 패턴이 있으면 output 리스트에 저장된 패턴 인덱스 출력
            if (!node.output.isEmpty()) {
                for (int patternIndex : node.output) {
                    System.out.println("Pattern found: " + trie.patterns.get(patternIndex) + " at index " + (i - trie.patterns.get(patternIndex).length() + 1));
                    matches.add(patternIndex);
                }
            }
        }

        return matches;
    }

}

class Trie {

    TrieNode root;

    List<String> patterns;

    public Trie(List<String> patterns) {
        this.root = new TrieNode();
        this.patterns = patterns;
        for (String pattern : patterns) {
            addPattern(pattern);
        }
        failure();
    }

    public void addPattern(String pattern) {
        TrieNode current = root;

        for (char c : pattern.toCharArray()) {
            if (current.child.get(c) == null) {
                current.child.put(c, new TrieNode());
            }
            current = current.child.get(c);
        }

        current.output.add(patterns.indexOf(pattern));  // 해당 패턴 인덱스 저장
    }

    public void failure() {
        LinkedList<TrieNode> queue = new LinkedList<>();
        root.fail = null;

        // root children의 fail을 root로 설정
        for (Map.Entry<Character, TrieNode> entry : root.child.entrySet()) {
            entry.getValue().fail = root;
            queue.offer(entry.getValue());
        }


        while(!queue.isEmpty()) {
            TrieNode current = queue.poll();

            for (Map.Entry<Character, TrieNode> entry : current.child.entrySet()) {
                char c = entry.getKey();
                TrieNode childNode = entry.getValue();

                // 실패 노드에 다음 올 문자가 없으면 계속 찾음
                TrieNode failNode = current.fail;
                while (failNode != null && !failNode.child.containsKey(c)) {
                    failNode = failNode.fail;
                }

                if (failNode == null) {
                    childNode.fail = root;
                } else {
                    childNode.fail = failNode.child.get(c);
                    childNode.output.addAll(childNode.fail.output);
                }

                queue.offer(childNode);
            }
        }
    }
}

class TrieNode {

    Map<Character, TrieNode> child = new HashMap<>();
    TrieNode fail;
    List<Integer> output = new ArrayList<>();

    public TrieNode() {
        this.fail = null;
    }
}
