package alg.yong.two;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    static class Event {
        int start;
        int end;
        int num;
        Event (int start, int end, int num) {
            this.start = start;
            this.end = end;
            this.num = num;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        int n = sc.nextInt();
        int[] rc = new int[m];
        for (int i = 0; i < m; i++) {
            rc[i] = sc.nextInt();
        }
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int start = sc.nextInt();
            int end = sc.nextInt();
            int num = sc.nextInt();
            events.add(new Event(start, end, num));

        }
        List<List<Event>> roomsche = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            roomsche.add(new ArrayList<>());
        }
        events.sort(Comparator.comparingInt(o -> o.start));
        for (Event event : events) {
            boolean flag = false;
            for (int i = 0; i < m; i++) {
                if(rc[i] >= event.num && check(roomsche.get(i), event)){
                    roomsche.get(i).add(event);
                    flag = true;
                    break;
                }
            }
            if(!flag){
                System.out.println(false);
                return;
            }

        }
        System.out.println(true);


    }
    private static boolean check(List<Event> sche,Event event){
        for (Event e : sche) {
            if(!(event.end<=e.start || event.start>=e.end)){
                return false;
            }
        }
        return true;
    }
}
