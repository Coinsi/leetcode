package alg.main360;
import java.util.*;
public class Main1 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n =  scanner.nextInt();
        //创建一个数组
        int [] keys = new int[n];//记录输入编号

        for(int i = 0; i < n; i++){
            keys[i] = scanner.nextInt();
        }

        int[] o = new int[n];
        boolean[] unlocked = new boolean[n+1];
        int last = 0;
        for(int i = 0; i < n; i++){
            int key = keys[i];
            unlocked[key] = true;
            while(last + 1 <= n && unlocked[last+1]){
                last++;
                o[last-1] = i + 1;

            }
        }
        for(int i = 0; i < n; i++){
            System.out.print(o[i]+" ");
        }
        scanner.close();
    }
}
