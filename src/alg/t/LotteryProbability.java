package alg.t;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class LotteryProbability {
    private static Map<String, Double> memo = new HashMap<>();

    public static double probabilityAWins(int n, int m) {
        if (n == 0) return 0.0; // 没有中奖票，A 无法获胜
        if (m == 0) return 1.0; // 没有不中奖票，A 直接获胜

        String key = n + "," + m;
        if (memo.containsKey(key)) return memo.get(key);

        double total = n + m;
        double p_A_win = (double) n / total; // A 直接抽中奖票的概率
        double p_A_lose = (double) m / total; // A 抽到不中奖票的概率

        // B 抽到中奖票，A 失败
        double p_B_win = (total - 1 > 0) ? (double) n / (total - 1) : 0;

        // B 抽到不中奖票，并且丢弃票的情况
        double p_discard_lost = (m - 1 > 0) ? ((double) (m - 1) / (total - 1)) * probabilityAWins(n, m - 2) : 0;
        double p_discard_won = (n > 0) ? ((double) n / (total - 1)) * probabilityAWins(n - 1, m - 1) : 0;

        double result = p_A_win + p_A_lose * (1 - p_B_win) * (p_discard_lost + p_discard_won);
        memo.put(key, result);
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        scanner.close();

        System.out.printf("%.4f\n", probabilityAWins(n, m));
    }
}
