package Programmers;

import java.util.ArrayList;
import java.util.List;

/**
 * 프로그래머스 17683번 문제 (방금그곡)
 */
public class 방금그곡 {

    public static void main(String[] args) {
//        "12:00,12:14,HELLO,CDEFGAB", "13:00,13:05,WORLD,ABCDEF"
//        "12:00,12:14,HELLO,C#DEFGAB", "13:00,13:05,WORLD,ABCDEF"
//        "03:00,03:30,FOO,CC#B", "04:00,04:08,BAR,CC#BCC#BCC#B"
        String[] list = new String[2];
        list[0] = "03:00,03:30,FOO,CC#B";
        list[1] = "04:00,04:08,BAR,CC#BCC#BCC#B";

        String result = solution("CC#BCC#BCC#BCC#B", list);

        System.out.println("result = " + result);
    }

    public static String solution(String m, String[] musicinfos) {

        String pattern = format(m);
        String name = "(None)";
        int playTime = 0;

        for (String music : musicinfos) {
            String[] musicInfo = music.split(",");
            String melody = format(musicInfo[3]);

            int time = getTime(musicInfo[1].split(":")) - getTime(musicInfo[0].split(":"));

            int j = 0;
            StringBuilder playMelody = new StringBuilder();
            String[] pitch = melody.split("");
            for (int i = 0; i < time; i++) {

                playMelody.append(pitch[j]);
                if (j == pitch.length - 1) {
                    j = 0;
                } else {
                    j++;
                }
            }

            boolean kmp = KMP(playMelody.toString(), pattern);

            if (kmp) {
                if (time > playTime) {
                    name = musicInfo[2];
                    playTime = time;
                }
            }
        }

        return name;
    }

    public static int getTime(String[] time) {
         return (Integer.parseInt(time[0]) * 60) + Integer.parseInt(time[1]);
    }

    public static String format(String melody) {
        String[] melodyWords = melody.split("");

        for (int i = 0; i < melody.length(); i++) {
            if (melodyWords[i].equals("#")) {
                melodyWords[i - 1] = melodyWords[i - 1].toLowerCase();
            }
        }

        return String.join("", melodyWords).replace("#", "");
    }

    public static boolean KMP(String parent, String pattern) {
        List<Integer> failureTable = failureFunction(pattern);

        int j = 0;
        for (int i = 0; i < parent.length(); i++) {
            char strParent = parent.charAt(i);
            char strPattern = pattern.charAt(j);

            while (j > 0 && strParent != strPattern) {
                j = failureTable.get(j - 1);
                strPattern = pattern.charAt(j);
            }

            if (strParent == strPattern) {
                if (j == pattern.length() - 1) {
                    return true;
                } else {
                    j++;
                }
            }
        }

        return false;
    }

    public static List<Integer> failureFunction(String pattern) {

        ArrayList<Integer> failureTable = new ArrayList<>();
        failureTable.add(0);

        int j = 0;
        for (int i = 1; i < pattern.length(); i++) {
            char str1 = pattern.charAt(j);
            char str2 = pattern.charAt(i);

            while(j > 0 && str1 != str2) {
                j = failureTable.get(j - 1);
                str1 = pattern.charAt(j);
            }

            if (str1 == str2) {
                j++;
                failureTable.add(i, j);
            } else {
                failureTable.add(0);
            }
        }

        return failureTable;
    }

}
