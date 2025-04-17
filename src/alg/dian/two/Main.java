package alg.dian.two;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x =sc.nextInt();
        int y =sc.nextInt();
        int z =sc.nextInt();
        int res = 0;
        if((x%10)%2 == 0 || (x/10) %2 == 0) res++;
        if((y%10)%2 == 0 || (x/10) %2 == 0) res++;
        if((z%10)%2 == 0 || (x/10) %2 == 0) res++;



        System.out.println(res);

    }
}