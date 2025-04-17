package alg.yong.four;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    static class Item{
        int s,e,b;
        public Item(int s, int t, int b) {
            this.s = s;
            this.e = s+t;
            this.b = b;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
            int n  = Integer.parseInt(sc.nextLine());
            int[] s  = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int[] t  = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int[] b  = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            Item[] items = new Item[n];
            for (int i = 0; i < n; i++) {
                items[i] = new Item(s[i],t[i],b[i]);


            }
            Arrays.sort(items, Comparator.comparingInt(o -> o.e));

            int[] dp = new int[n];
            dp[0] = items[0].b;
            for (int i = 1; i < n; i++) {
                int l = 0, r = i-1,index = -1;
                while (l <= r){
                    int mid = (l+r)/2;
                    if(items[mid].e <= items[i].s){
                        index = mid;
                        l = mid + 1;
                    }else{
                        r = mid - 1;
                    }
                }
                dp[i] = Math.max(dp[i-1], index == -1 ? items[i].b : dp[index] + items[i].b);


            }
            System.out.println(dp[n-1]);




    }
}
