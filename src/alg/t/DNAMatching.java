package alg.t;
import java.util.Scanner;

/*
DNA匹配
时间限制： C/C++语言 1000MS； 其他语言 3000MS
内存限制： C/C++语言 65536KB； 其他语言 589824KB
题目描述：
有一种特殊的DNA，仅仅由核酸A和T组成，长度为n，顺次连接

科学家有一种新的手段，可以改变这种DNA。每一次，科学家可以交换该DNA上两个核酸的位置，也可以将某个特定位置的核酸修改为另一种核酸。

现在有一个DNA，科学家希望将其改造成另一种DNA，希望你计算最少的操作次数。



输入描述
输入包含两行，第一行为初始的DNA，第二行为目标DNA，保证长度相同。

输出描述
        输出最少的操作次数


样例输入
        ATTTAA
        TTAATT

        样例输出
        3


提示
对于100%的数据，DNA长度小于等于100000
样例解释：
        1.首先修改第一个位置的核酸（从A修改为T）
        2.交换3和5位置的核酸
        3.交换4和6位置的核酸
*/

public class DNAMatching {
    public static int minOperations(String dna1, String dna2) {
        int cntA = 0, cntT = 0;

        // 计算需要修改的 A -> T 和 T -> A 数量
        for (int i = 0; i < dna1.length(); i++) {
            char a = dna1.charAt(i);
            char b = dna2.charAt(i);
            if (a == 'A' && b == 'T') {
                cntA++;
            } else if (a == 'T' && b == 'A') {
                cntT++;
            }
        }

        // 交换匹配的对数 + 剩余的替换
        return Math.min(cntA, cntT) + Math.abs(cntA - cntT);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String dna1 = scanner.nextLine().trim();
        String dna2 = scanner.nextLine().trim();
        scanner.close();

        System.out.println(minOperations(dna1, dna2));
    }
}
