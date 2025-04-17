package alg.t;

public class Test {

    public static int getValue(int i) {

        try {
            throw new Exception("try");
        } catch (Exception e) {
            System.out.println("catch");
            i++;
            return i;
        } finally {
            System.out.println("finally");
            ++i;
        }

    }

    public static void main(String[] args) {
        System.out.println(getValue(0));

    }
}