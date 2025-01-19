package BaekJoon;

import java.util.*;

/**
 * 백준 9250번 문제 (문자열 집합 판별)
 * https://www.acmicpc.net/problem/9250
 */
public class 문자열집합판별  {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        int sCount = scan.nextInt();
        String[] S = new String[sCount];

        for (int i = 0; i < sCount; i++) {
            String text = scan.next();
            S[i] = text;
        }

        int qCount = scan.nextInt();
        String[] Q = new String[qCount];

        for (int j = 0; j < qCount; j++) {
            String text = scan.next();
            Q[j] = text;
        }

        AhoCorasick ahoCorasick = new AhoCorasick(S);
        ahoCorasick.failure();

        for (String text : Q) {
            String search = ahoCorasick.search(text);
            System.out.println(search);
        }

    }

}

class AhoCorasick {

   TrieNode root;

    public AhoCorasick(String[] patterns) {
        this.root = new TrieNode();

        // 입력 받은 패턴들로 된 트라이 자료구조 생성
        for (int i = 0; i < patterns.length; i++) {
            String pattern = patterns[i];

            TrieNode current = this.root;
            for (char c : pattern.toCharArray()) {
                if (current.children.get(c) == null) { // 하위에 해당 문자가 없는 경우 Node 생성
                    current.children.put(c, new TrieNode());

                }

                current = current.children.get(c);
            }

            current.output = true;  // 해당 패턴 문자열의 끝을 의미한다.
        }
    }

    public void failure() {
        LinkedList<TrieNode> queue = new LinkedList<>();
        root.faild = null;

        // root의 하위 값 (각 패턴의 시작 문자) fail = root
        for (Map.Entry<Character, TrieNode> child : root.children.entrySet()) {
            TrieNode node = child.getValue();
            node.faild = root;
            queue.offer(node);
        }

        while (!queue.isEmpty()) {
            TrieNode current = queue.poll();

            // 실패 노드 연결
            for (Map.Entry<Character, TrieNode> child : current.children.entrySet()) {
                char c = child.getKey();
                TrieNode node = child.getValue();

                TrieNode faildNode = current.faild;
                while (faildNode != null && !faildNode.children.containsKey(c)) {
                    faildNode = faildNode.faild;
                }

                if (faildNode == null) {
                    node.faild = root;
                } else {
                    node.faild = faildNode.children.get(c);
                    node.output |= node.faild.output;
                }

                queue.offer(node);
            }
        }

    }

    public String search(String text) {
        TrieNode current = root;

        for (Character c : text.toCharArray()) {
            while (current != null && !current.children.containsKey(c)) {
                current = current.faild;
            }

            if (current == null) {
                current = root;
                continue;
            }

            current = current.children.get(c);

            if (current.output) {
                return "YES";
            }
        }

        return "NO";
    }
}

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();

    TrieNode faild;

    boolean output;

    public TrieNode() {
        this.faild = null;
        this.output = false;
    }
}