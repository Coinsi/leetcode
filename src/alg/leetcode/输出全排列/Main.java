package alg.leetcode.输出全排列;

import java.util.Scanner;

public class Main {
    static int n;
    static int[] a = new int[10];
    static int[] used = new int[10];
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        dfs(0);
    }


    static void dfs(int d){
        if(d==n){
            for (int i = 0; i < n; i++) {
                System.out.print(a[i]+" ");

            }
            System.out.println();

        }
        for (int i = 1; i <= n; i++) {
            if(used[i]==0){
                a[d] = i;
                used[i] = 1;
                dfs(d+1);
                used[i] = 0;
            }
        }
    }
}
