package alg.qunhe.two;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] numbers = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                numbers[i][j] = sc.nextInt();
            }
        }
        int res = 0;
        int l = 2; // 子矩阵的边长从 2 开始
        while (l <= n) { // 确保子矩阵的边长不超过 n
            for (int i = 0; i <= n - l; i++) { // 遍历所有可能的起始行
                for (int j = 0; j <= n - l; j++) { // 遍历所有可能的起始列
                    if (function(numbers, i, j, l)) res++; // 检查子矩阵是否满足条件
                }
            }
            l += 2; // 子矩阵边长每次增加 2
        }
        System.out.println(res);
    }

    public static boolean function(int[][] nums, int startRow, int startCol, int l) {
        int num_0 = 0;
        int num_1 = 0;
        for (int i = startRow; i < startRow + l; i++) {
            for (int j = startCol; j < startCol + l; j++) {
                if (nums[i][j] == 0) num_0++;
                else num_1++;
            }
        }
        return num_0 == num_1; // 判断 0 和 1 的数量是否相等
    }
}