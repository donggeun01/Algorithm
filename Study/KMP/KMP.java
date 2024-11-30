package Study.KMP;

import java.util.ArrayList;
import java.util.List;

/* KMP */
public class KMP {

    public static void main(String[] args) {
        String parent = "ababacabacaabacaaba";
        String pattern = "abacaaba";

        List<Integer> failureTable = failureFunction(pattern);

        int j = 0;
        for(int i = 0; i < parent.length(); i++) {
            char str1 = parent.charAt(i);
            char str2 = pattern.charAt(j);
            while (j > 0 && str1 != str2) {
                j = failureTable.get(j - 1);
                str2 = pattern.charAt(j);
            }

            if (str1 == str2) {
                if (j == pattern.length() - 1) {
                    System.out.println("매칭 index = " + (i - pattern.length() + 1));
                    j = failureTable.get(j);
                } else {
                    j++;
                }
            }
        }
    }

    /**
     * 실패 함수 만들기
     * @param pattern - 찾는 문자열
     * @return List<Integer> - failureFunction Table
     */
    public static List<Integer> failureFunction(String pattern) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0);

        int j = 0;
        for (int i = 1; i < pattern.length(); i++) {
            char str1 = pattern.charAt(j);
            char str2 = pattern.charAt(i);
            while(j > 0 && str1 != str2) {
                j = list.get(j - 1);
                str1 = pattern.charAt(j);
            }

            if (str1 == str2) {
                list.add(i, j + 1);
                j++;
            } else {
                list.add(0);
            }
        }

        return list;
    }

}