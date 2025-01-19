package Programmers;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 프로그래머스 176962번 문제 (과제 진행하기)
 */
public class 과제_진행하기 {

    private static final int hour = 60 * 60 * 1000;
    private static final int second = 60 * 1000;

    public static void main(String[] args) {
        String[][] plans = {
                {"science", "12:40", "50"},
                {"music", "12:20", "40"},
                {"history", "14:00", "30"},
                {"computer", "12:30", "100"}
        };

        List<String> result = new ArrayList<>();

        // 하던 과제
        Stack<Task> stack = new Stack<>();

        // 과제 순서 정렬
        List<Task> list = Arrays.stream(plans).map(o1 -> {
                    String[] split = o1[1].split(":");
                    long i = Long.parseLong(split[0]) * hour;
                    long j = Long.parseLong(split[1]) * second;
                    long time = Long.parseLong(o1[2]) * second;

                    return new Task(o1[0], i + j, time);
                }).sorted(Comparator.comparingLong(o -> o.start))
                .collect(Collectors.toList());
        LinkedList<Task> queue = new LinkedList<>(list);

        Task current = queue.poll();
        while (!queue.isEmpty()) {
            Task next = queue.poll();

            long timeLeft  = next.start - current.start;    // 다음 과제시작까지 남은 시간
            long diffTime = timeLeft - current.playtime;     // 양수는 시간 남음, 음수는 시간 없음.

            if (diffTime >= 0) {
                result.add(current.name);

                while (!stack.isEmpty()) {
                    Task task = stack.pop();
                    if (diffTime >= task.playtime) {
                        diffTime -= task.playtime;
                        result.add(task.name);
                    } else {
                        task.playtime -= diffTime;
                        stack.add(task);
                        break;
                    }
                }
            } else {
                current.playtime = current.playtime - timeLeft ;
                stack.add(current);
            }
            current = next;
        }

        result.add(current.name);
        while(!stack.isEmpty()) {
            Task task = stack.pop();
            result.add(task.name);
        }

        System.out.println("result = " + result);
    }

    static class Task {
        public String name;
        public long start;
        public long playtime;

        public Task(String name, long start, long time) {
            this.name = name;
            this.start = start;
            this.playtime = time;
        }
    }
}

