package alg.qunhe.one;
import java.util.*;
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n=sc.nextInt();
        int[][] numbers=new int[n][n];
        for(int i=0;i<n;i++){
            for (int j = 0; j < n; j++) {
                numbers[i][j]= sc.nextInt();
            }
        }
        int res=0;
        int l =2;
        while(l<=n){
            for(int i =0;i<=n-l;i++){
                for(int j =0;j<=n-l;j++){
                   if(function(numbers,i,j,l)) res++;

                }
            }
            l+=2;

        }

        System.out.println(res);



    }
    public static boolean function(int[][] nums,int start,int col,int l){
        int num_0=0;
        int num_1=0;
        for(int i=start;i<start+l;i++){
            for(int j =col;j<col+l;j++){
                if(nums[i][j]==0) num_0++;
                else num_1++;
            }
        }
        return num_0 == num_1;


    }
}
