public class Test {

    public static void main(String[] args) {
        //判断输入的数为几位数
        int num = 123456;
        int count = 0;
        while (num != 0) {
            num /= 10;
            count++;
        }
        System.out.println("输入的数是" + count + "位数");
    }


    public static int getSum(int num) {
        int sum = 0;
        while (num != 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }
}
