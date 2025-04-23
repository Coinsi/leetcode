package alg.meituan.one;
import java.util.*;
public class Main {

    static class Pair{
        int value;
        int index;
        Pair(int value,int index){
            this.value = value;
            this.index = index;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T =sc.nextInt();
        while(T-- >0){
            int n =sc.nextInt();
            int[] a = new int[n];
            Pair[] pairs = new Pair[n];

            for(int i =0;i<n;i++){
                a[i] =sc.nextInt();
                pairs[i] = new Pair(a[i],i+1);

            }
            Arrays.sort(pairs, Comparator.comparingInt(o -> o.value));
            int f =0;
            int b =0;
            for(int i =0;i<n-1;i++){
               int  from = pairs[i].index;
                int to = pairs[i+1].index;
                if(to>from) f++;
                else b++;

            }
            System.out.println(f+" "+b);
        }

    }

}
